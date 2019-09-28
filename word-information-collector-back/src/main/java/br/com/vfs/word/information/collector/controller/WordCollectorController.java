package br.com.vfs.word.information.collector.controller;

import br.com.vfs.word.information.collector.service.WordCollectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("collector")
@RequiredArgsConstructor
public class WordCollectorController {

    private final WordCollectorService service;

    @PostMapping("/actuator")
    public ResponseEntity actuator() {
        log.info("m=actuator, atualizando dicionario");
        service.actuator();
        return ResponseEntity.ok().build();
    }
}
