package br.com.vfs.word.information.collector.dto;

import br.com.vfs.word.information.collector.enums.Color;
import lombok.Data;

@Data
public class Edge {

    private String source;
    private String target;
    private String faveColor;

    public Edge(String source, String target) {
        this.source = source;
        this.target = target;
        this.faveColor = Color.indexValue(0);
    }
}
