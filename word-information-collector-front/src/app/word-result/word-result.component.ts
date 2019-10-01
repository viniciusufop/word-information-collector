import { Component, OnInit } from '@angular/core';
import { WordService } from '../word.service';

@Component({
  selector: 'app-word-result',
  templateUrl: './word-result.component.html',
  styleUrls: ['./word-result.component.css']
})
export class WordResultComponent implements OnInit {
  //layout escolhido para o grafo
  layout: any = {
    name: 'cose'
  }
  // dados do grafo
  graphData: any = {};
  
  //cytoscape
  cy: any;
  
  constructor(private service: WordService) {}
  
  ngOnInit() {
  }

  search(word: string ) {
    this.service.searchWord(word).subscribe(
      data => {
        //atualiza os dados
        this.graphData = data;
        //nula o objecto para ser recriado
        this.cy = null;
      },
      error => {
        console.log(error);
      }
    );;

  }
}
