package br.com.vfs.word.information.collector.controller;

import br.com.vfs.word.information.collector.enums.Letter;
import br.com.vfs.word.information.collector.service.WordCollectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("collector/lettet/")
@RequiredArgsConstructor
public class WordCollectorController {

    private final WordCollectorService service;

    @PostMapping("/{letter}")
    public ResponseEntity actuator(@PathVariable("letter") Letter letter) {
        actuatorLetter(letter);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/total")
    public ResponseEntity actuatorTotal() {
        log.info("m=actuatorLetter, atualizando todas as palavras");
        Arrays.stream(Letter.values()).forEach(this::actuatorLetter);
        return ResponseEntity.ok().build();
    }

    private void actuatorLetter(Letter letter) {
        log.info("m=actuatorLetter, atualizando palavras iniciadas com a letra {}", letter);
        service.actuatorLetter(letter);
    }

}