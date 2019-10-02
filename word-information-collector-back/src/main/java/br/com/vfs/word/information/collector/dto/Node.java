package br.com.vfs.word.information.collector.dto;

import br.com.vfs.word.information.collector.entity.Word;
import br.com.vfs.word.information.collector.enums.Color;
import lombok.Data;

@Data
public class Node {

    private String id;
    private String name;
    private String faveColor;

    public Node(Word word) {
        this.id = word.getValue();
        this.name = word.getValue();
        this.faveColor = Color.indexValue(word.getDepth());
    }
}
