package br.com.vfs.word.information.collector.service;

import br.com.vfs.word.information.collector.entity.Word;
import br.com.vfs.word.information.collector.repository.WordRepository;
import com.google.common.collect.ConcurrentHashMultiset;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WordCollectorService {

    private final ConcurrentHashMultiset<Word> words;
    private final String urlBase;
    private final WordRepository wordRepository;
    private static final int SEARCH_DEPTH = 3;

    public WordCollectorService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
        words = ConcurrentHashMultiset.create();
        urlBase = "http://dicionariocriativo.com.br/";
    }

    public Set<Word> search(String searchWord) {
        proccessSynonymsWord(Optional.of(searchWord), 1);
        return new HashSet<>(words);
    }

    public void actuator() {
        proccessSynonymsWord(Optional.of("amor"), 1);
        words.forEach(word -> log.info("m=actuator, palavras pesquisadas: {}", word));
    }

    private void proccessSynonymsWord(Optional<String> optionalWord, int depth) {
        if(depth > SEARCH_DEPTH) {
            return;
        }
        optionalWord.ifPresent(word -> {
            log.info("m=proccessSynonymsWord, processando a palavra {}", word);
            final Word entity = wordRepository.findById(word).orElseGet(() -> searchWeb(word).orElse(new Word()));
            if(!StringUtils.isEmpty(entity.getValue())){
                words.add(entity);
                //busco os sinominos aqui
                entity.getSynonyms().parallelStream()
                        .filter(newWord -> words.stream().map(Word::getValue).noneMatch(value -> value.equals(newWord)))
                        .map(Optional::of)
                        .forEach(newWord -> this.proccessSynonymsWord(newWord, depth+1));
            }
        });

    }

    private Optional<Word> searchWeb(String word) {
        try {
            log.info("m=proccessSynonymsWord, processando a palavra {}", word);

            final Document doc = Jsoup.connect(String.format("%s%s", urlBase, word))
                    .validateTLSCertificates(false)
                    .get();
            final Element sinant = doc.getElementById("sinant");
            if(Objects.nonNull(sinant)){
                final List<String> synonyms = new ArrayList<>();
                sinant.getElementsByClass("c_primary_hover")
                        .forEach(newWord -> synonyms.add(newWord.text()));
                Word newEntity = Word.builder()
                        .value(word)
                        .synonyms(synonyms)
                        .build();
                wordRepository.save(newEntity);
                return Optional.of(newEntity);
            }
        } catch (IOException e) {
            log.error("m=actuator, erro ao executar o jsoup", e);
        }
        return Optional.empty();
    }

}
