package br.com.vfs.word.information.collector.controller;

import br.com.vfs.word.information.collector.dto.GraphData;
import br.com.vfs.word.information.collector.service.WordCollectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("word")
@RequiredArgsConstructor
public class WordController {

    private final WordCollectorService service;

    @CrossOrigin
    @GetMapping("/{word}")
    public ResponseEntity<GraphData> get(
            @PathVariable("word") String word) {
        log.info("m=get, pesquisando pela palavra {}", word);
        return ResponseEntity.ok(new GraphData(service.search(word)));
    }
}
