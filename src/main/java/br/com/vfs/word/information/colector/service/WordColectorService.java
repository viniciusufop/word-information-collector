package br.com.vfs.word.information.colector.service;

import com.google.common.collect.ConcurrentHashMultiset;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class WordColectorService {

    private final ConcurrentHashMultiset<String> words;
    private final String urlBase;
    private static final int SEARCH_DEPTH = 10;
    public WordColectorService() {
        words = ConcurrentHashMultiset.create();
        urlBase = "http://dicionariocriativo.com.br/";
    }

    public void actuator() {
        proccessSynonymsWord(Optional.of("amor"), 1);
        words.forEach(word -> log.info("m=actuator, palavras pesquisadas: {}", word));

    }

    private void proccessSynonymsWord(Optional<String> optionalWord, int profundidade) {
        if(profundidade > SEARCH_DEPTH) {
            return;
        }
        optionalWord.ifPresent(word -> {
            try {
                log.info("m=proccessSynonymsWord, processando a palavra {}", word);
                words.add(word, 1);

                final Document doc = Jsoup.connect(String.format("%s%s", urlBase, word))
                        .validateTLSCertificates(false)
                        .get();
                final Element sinant = doc.getElementById("sinant");
                if(Objects.nonNull(sinant)){
                    final List<String> synonyms = new ArrayList<>();

                    sinant.getElementsByClass("c_primary_hover")
                            .forEach(newWord -> synonyms.add(newWord.text()));

                    synonyms.parallelStream()
                            .filter(newWord -> !words.contains(newWord))
                            .map(Optional::of)
                            .forEach(newWord -> this.proccessSynonymsWord(newWord, profundidade+1));

                }

            } catch (IOException e) {
                log.error("m=actuator, erro ao executar o jsoup", e);
            }
        });

    }
}
