package br.com.vfs.word.information.collector.dto;

import lombok.Data;

@Data
public class Edge {

    private String source;
    private String target;
    private String faveColor = "#6FB1FC";

    public Edge(String source, String target) {
        this.source = source;
        this.target = target;
    }
}
