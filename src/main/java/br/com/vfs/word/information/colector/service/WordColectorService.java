package br.com.vfs.word.information.colector.service;

import com.google.common.collect.ConcurrentHashMultiset;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WordColectorService {

    private final ConcurrentHashMultiset<String> words;
    private final String urlBase;
    public WordColectorService() {
        words = ConcurrentHashMultiset.create();
        urlBase = "https://dicionariocriativo.com.br/";
    }

    public void actuator() {
        proccessSynonymsWord(Optional.of("amor"));
        words.forEach(word -> log.info("m=actuator, palavras pesquisadas: {}", word));

    }

    private void proccessSynonymsWord(Optional<String> optionalWord) {
        optionalWord.ifPresent(word -> {
            try {
                log.info("m=proccessSynonymsWord, processando a palavra {}", word);
                words.add(word, 1);

                final Document doc = Jsoup.connect(String.format("https://dicionariocriativo.com.br/%s", word)).get();
                final Element sinant = doc.getElementById("sinant");

                final List<String> synonyms = new ArrayList<>();

                sinant.getElementsByClass("c_primary_hover")
                        .forEach(newWord -> synonyms.add(newWord.text()));

                synonyms.stream()
                        .filter(newWord -> !words.contains(newWord))
                        .map(Optional::of)
                        .forEach(this::proccessSynonymsWord);

            } catch (IOException e) {
                log.error("m=actuator, erro ao executar o jsoup", e);
            }
        });

    }
}
