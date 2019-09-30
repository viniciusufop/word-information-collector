package br.com.vfs.word.information.collector.dto;

import br.com.vfs.word.information.collector.entity.Word;
import lombok.Data;

@Data
public class Node {

    private String id;
    private String name;
    private String faveColor = "#6FB1FC";
    private String faveShape = "triangle";

    public Node(Word word) {
        this.id = word.getValue();
        this.name = word.getValue();
    }
}
