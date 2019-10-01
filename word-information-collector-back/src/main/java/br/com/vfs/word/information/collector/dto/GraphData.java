package br.com.vfs.word.information.collector.dto;

import br.com.vfs.word.information.collector.entity.Word;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class GraphData {

    private Set<DataNode> nodes;
    private Set<DataEdge> edges;

    public GraphData(Set<Word> words) {
        nodes = words.stream()
                .map(DataNode::new)
                .collect(Collectors.toSet());
        edges = words.stream()
                .map(word ->
                        word.getSynonyms().stream()
                                .filter(s -> nodes.stream()
                                        .map(DataNode::getData)
                                        .map(Node::getId)
                                        .anyMatch(s::equals))
                                .map(s -> new DataEdge(word.getValue(), s))
                                .collect(Collectors.toSet())
                ).reduce((a, b) -> {
                    a.addAll(b);
                    return a;
                })
                .orElse(Collections.EMPTY_SET);

    }
}