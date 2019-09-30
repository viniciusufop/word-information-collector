package br.com.vfs.word.information.collector.dto;

import br.com.vfs.word.information.collector.entity.Word;
import lombok.Data;

@Data
public class DataNode {

    private Node data;

    public DataNode(Word word) {
        this.data = new Node(word);
    }
}
