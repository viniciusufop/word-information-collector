import { Component, OnInit } from '@angular/core';
import { WordService } from '../word.service';
import { CytoscapeComponent } from 'ngx-cytoscape';

@Component({
  selector: 'app-word-result',
  templateUrl: './word-result.component.html',
  styleUrls: ['./word-result.component.css']
})
export class WordResultComponent implements OnInit {  
  constructor(private service: WordService) {}
  
  ngOnInit() {
  }

  search(word: string, graph: CytoscapeComponent) {
    this.service.searchWord(word).subscribe(
      data => {
        console.log(data['significations']);
        //atualiza os dados
        graph.layout = {
          name: 'circle'
        };
        graph.elements = data['graphData'];
        graph.cy = null;
        graph.ngOnChanges();
      },
      error => {
        console.log(error);
      }
    );
  }
}
