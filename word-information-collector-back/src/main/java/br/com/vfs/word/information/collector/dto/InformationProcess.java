package br.com.vfs.word.information.collector.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InformationProcess {

    private List<String> significations;
    private GraphData graphData;
}
