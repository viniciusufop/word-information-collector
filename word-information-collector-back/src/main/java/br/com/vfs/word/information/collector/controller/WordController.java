package br.com.vfs.word.information.collector.controller;

import br.com.vfs.word.information.collector.dto.InformationProcess;
import br.com.vfs.word.information.collector.service.WordCollectorService;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("word")
@RequiredArgsConstructor
public class WordController {

    private static final String REGEX = "[^0-9a-zàáâãèéêëìíîòóôõùúû]+";

    private final WordCollectorService service;

    @CrossOrigin
    @GetMapping("/{word}")
    public ResponseEntity<Mono<InformationProcess>> get(
            @PathVariable("word") String word){
        try {
            //decoder
            word = URLDecoder.decode(word, StandardCharsets.UTF_8.toString());
            //torna tudo minusculo
            word = word.toLowerCase();
        } catch (UnsupportedEncodingException e) {
            log.error("M=get erro ao decoda a string", e);
            return ResponseEntity.badRequest().build();
        }
        log.info("m=get, pesquisando pela palavra {}", word);
        if (Objects.isNull(word)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity
                .ok(service.search(word.replaceAll(REGEX, "")));
    }

}
