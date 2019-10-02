package br.com.vfs.word.information.collector.service;

import br.com.vfs.word.information.collector.dto.GraphData;
import br.com.vfs.word.information.collector.dto.InformationProcess;
import br.com.vfs.word.information.collector.entity.Word;
import br.com.vfs.word.information.collector.enums.Letter;
import br.com.vfs.word.information.collector.repository.WordRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.ConcurrentHashMultiset;

@Slf4j
@Service
public class WordCollectorService {

    private final String urlBase;

    private final String index;

    private final WordRepository wordRepository;

    private static final int SEARCH_DEPTH = 3;

    public WordCollectorService(WordRepository wordRepository){
        this.wordRepository = wordRepository;
        urlBase = "http://dicionariocriativo.com.br/";
        index = "indice-de-palavras/";
    }

    public InformationProcess search(String searchWord){
        final ConcurrentHashMultiset<Word> words = ConcurrentHashMultiset.create();
        proccessSynonymsWord(words, Optional.of(searchWord), 0);
        Set<Word> wordSet = words.elementSet();
        return InformationProcess.builder()
                .significations(wordSet.stream()
                        .filter(s -> s.getValue().equalsIgnoreCase(searchWord))
                        .map(Word::getSignification)
                        .findFirst()
                        .orElse(new ArrayList<>()))
                .graphData(new GraphData(wordSet))
                .build();
    }

    @Async
    public void actuatorLetter(Letter letter){
        try {
            log.info("m=actuatorLetter, processando a letra {}", letter);
            final String url = String.format("%s%s%s", urlBase, index, letter);
            final Document doc = Jsoup.connect(url).get();

            final Element indexList = doc.getElementById("indexList");
            if (Objects.nonNull(indexList)) {
                final List<String> words = new ArrayList<>();
                indexList.getElementsByTag("li")
                        .forEach(newWord -> words.add(newWord.text()));
                //tratar espacos
                // removendo duplicacoes e palavras que nao inicial com a letra
                final Set<String> totalWords = words.stream()
                        .map(s -> s.split(" "))
                        .map(Arrays::asList)
                        .map(list -> list.stream()
                                .filter(s -> s.substring(0, 1).equalsIgnoreCase(letter.name()))
                                .collect(Collectors.toSet()))
                        .reduce((a, b) -> {
                            a.addAll(b);
                            return a;
                        })
                        .orElse(Collections.EMPTY_SET);

                totalWords.parallelStream()
                        .filter(word -> !wordRepository.findById(word).isPresent())
                        .forEach(this::searchWeb);
            }
        } catch (IOException e) {
            log.error("m=actuatorLetter, erro ao executar o jsoup", e);
        }
    }

    private void proccessSynonymsWord(ConcurrentHashMultiset<Word> words, Optional<String> optionalWord, int depth){
        if (depth >= SEARCH_DEPTH) {
            return;
        }
        optionalWord.ifPresent(word -> {
            log.info("m=proccessSynonymsWord, processando a palavra {}", word);
            final Word entity = wordRepository.findById(word).orElseGet(() -> searchWeb(word).orElse(new Word()));
            if (!StringUtils.isEmpty(entity.getValue())) {
                entity.setDepth(depth);
                //avaliar se outro processo paralelo incluiu
                words.stream()
                        .filter(element -> element.getValue().equalsIgnoreCase(entity.getValue())
                                && element.getDepth() > entity.getDepth())
                        .findFirst()
                        .ifPresent(element -> element.setDepth(entity.getDepth()));
                //adiciona o outro elemento de pesquisa
                words.add(entity);

                //busco os sinominos aqui
                entity.getSynonyms().parallelStream()
                        .filter(newWord -> words.stream().map(Word::getValue).noneMatch(value -> value.equals(newWord)))
                        .map(Optional::of)
                        .forEach(newWord -> this.proccessSynonymsWord(words, newWord, depth + 1));
            }
        });

    }

    private Optional<Word> searchWeb(String word){
        try {
            log.info("m=proccessSynonymsWord, processando a palavra {}", word);
            final String url = String
                    .format("%s%s", urlBase, word);
            final Document doc = Jsoup.connect(url).get();
            final Word newEntity = Word.builder()
                    .value(word)
                    .signification(getSignification(doc))
                    .synonyms(getSynonyms(doc))
                    .build();
            wordRepository.save(newEntity);
            return Optional.of(newEntity);
        } catch (IOException e) {
            log.error("m=actuatorLetter, erro ao executar o jsoup", e);
        }
        return Optional.empty();
    }

    private List<String> getSignification(Document doc){
        final Element signification = doc.getElementById("significado");
        final List<String> list = new ArrayList<>();

        if (Objects.nonNull(signification)) {
            signification.getElementsByTag("li").forEach(
                    value -> list.add(value.text()));

        }
        return list;
    }

    private Set<String> getSynonyms(Document doc){
        final Element sinant = doc.getElementById("sinant");
        final Set<String> synonyms = new HashSet<>();
        if (Objects.nonNull(sinant)) {
            sinant.getElementsByClass("c_primary_hover")
                    .forEach(newWord -> synonyms.add(newWord.text()));
        }
        return synonyms;
    }

}
