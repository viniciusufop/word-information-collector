import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WordService {
  private baseUrl: string;
  private searchNameProductsUrl = '/word';
  private httpOptions;
  constructor(private http: HttpClient) {
    // this.baseUrl = environment.APIEndpoint + '/products/public';
    this.baseUrl = 'http://172.34.0.103:8080';
    this.httpOptions = {
      responseType: 'text',
      headers: new HttpHeaders({ })
    };
  }

  searchWord(word: string): Observable<Object> {
    console.log(word);
    var url = this.baseUrl + this.searchNameProductsUrl + '/' + word;
    return this.http.get<Object>(url);
  }

}
