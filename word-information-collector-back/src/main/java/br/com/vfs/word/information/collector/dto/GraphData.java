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
    //    nodes: [
//    {data: {id: 'j', name: 'Jerry', faveColor: '#6FB1FC', faveShape: 'triangle'}},
//    {data: {id: 'e', name: 'Elaine', faveColor: '#EDA1ED', faveShape: 'ellipse'}},
//    {data: {id: 'k', name: 'Kramer', faveColor: '#86B342', faveShape: 'octagon'}},
//    {data: {id: 'g', name: 'George', faveColor: '#F5A45D', faveShape: 'rectangle'}}
//    ],
//    edges: [
//    {data: {source: 'j', target: 'e', faveColor: '#6FB1FC'}},
//    {data: {source: 'j', target: 'k', faveColor: '#6FB1FC'}},
//    {data: {source: 'j', target: 'g', faveColor: '#6FB1FC'}},
//
//    {data: {source: 'e', target: 'j', faveColor: '#EDA1ED'}},
//    {data: {source: 'e', target: 'k', faveColor: '#EDA1ED'}},
//
//    {data: {source: 'k', target: 'j', faveColor: '#86B342'}},
//    {data: {source: 'k', target: 'e', faveColor: '#86B342'}},
//    {data: {source: 'k', target: 'g', faveColor: '#86B342'}},
//
//    {data: {source: 'g', target: 'j', faveColor: '#F5A45D'}}
//    ]
}
