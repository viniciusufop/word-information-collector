package br.com.vfs.word.information.colector.controller;

import br.com.vfs.word.information.colector.service.WordColectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("colector")
@RequiredArgsConstructor
public class ColectorController {

    private final WordColectorService service;

    @PostMapping("/actuator")
    public ResponseEntity actuator() {
        log.info("m=actuator, atualizando dicionario");
        service.actuator();
        return ResponseEntity.ok().build();
    }
}
