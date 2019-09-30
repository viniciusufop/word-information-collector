package br.com.vfs.word.information.collector.dto;

import lombok.Data;

@Data
public class DataEdge {

    private Edge data;

    public DataEdge(String source, String target) {
        this.data = new Edge(source, target);
    }
}
