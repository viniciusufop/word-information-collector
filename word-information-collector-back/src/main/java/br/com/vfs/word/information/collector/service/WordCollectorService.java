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
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.collect.ConcurrentHashMultiset;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Mono<InformationProcess> search(String searchWord){
        final ConcurrentHashMultiset<Word> words = ConcurrentHashMultiset.create();

        Flux<Word> wordFlux = processWord(words, Optional.of(searchWord), 0);
        processWord(words, Optional.of(searchWord), 0);
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


                Flux<Word> fluxWords = totalWords
                        .parallelStream()
                        .map(word -> Pair.of(word, wordRepository.findById(word)))
                        .map(pair ->
                                pair.getSecond()
                                        .switchIfEmpty(searchWeb(pair.getFirst())))
                        .map(Mono::flux)
                        .reduce(Flux::concatWith)
                        .orElse(Flux.empty());

                totalWords
                        .parallelStream()
                        .map(word -> Pair.of(word, wordRepository.findById(word)))
                        .forEach(pair ->
                            pair.getSecond().switchIfEmpty(searchWeb(pair.getFirst())));

            }
        } catch (IOException e) {
            log.error("m=actuatorLetter, erro ao executar o jsoup", e);
        }
    }

    private Flux<Word> processWord(ConcurrentHashMultiset<Word> words, Optional<String> optionalWord, int depth){
        return optionalWord.map(word -> {
            log.info("m=processWord, processando a palavra {}", word);
            final Mono<Word> wordMono = wordRepository.findById(word)
                    .switchIfEmpty(searchWeb(word));
            return wordMono.flatMapMany(entity -> processWord2(words, entity, depth));
        }).orElse(Flux.empty());
    }

    private Flux<Word> processWord2 (ConcurrentHashMultiset<Word> words,Word entity, int depth){
        entity.setDepth(depth);
        //avaliar se outro processo paralelo incluiu
        words.stream()
                .filter(element -> element.getValue().equalsIgnoreCase(entity.getValue())
                        && element.getDepth() > entity.getDepth())
                .findFirst()
                .ifPresent(element -> element.setDepth(entity.getDepth()));
        //adiciona o outro elemento de pesquisa
        words.add(entity);
        final int nextdepth = depth + 1;
        if (nextdepth < SEARCH_DEPTH) {
            //busco os sinominos aqui
            return entity.getSynonyms().parallelStream()
                    .filter(newWord -> words.stream().map(Word::getValue).noneMatch(value -> value.equals(newWord)))
                    .map(Optional::of)
                    .map(newWord -> this.processWord(words, newWord, nextdepth))
                    .reduce(Flux::concatWith)
                    .orElse(Flux.empty());
        }
        return Flux.empty();
    }

    private Mono<Word> searchWeb(String word){
        try {
            log.info("m=processWord, processando a palavra {}", word);
            final String url = String
                    .format("%s%s", urlBase, word);
            final Document doc = Jsoup.connect(url).get();
            final Word newEntity = Word.builder()
                    .value(word)
                    .signification(getSignification(doc))
                    .synonyms(getSynonyms(doc))
                    .build();
            return wordRepository.save(newEntity);
        } catch (IOException e) {
            log.error("m=actuatorLetter, erro ao executar o jsoup", e);
        }
        return Mono.empty();
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
