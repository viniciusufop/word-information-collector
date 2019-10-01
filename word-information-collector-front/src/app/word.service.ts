import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WordService {
  private baseUrl: string;
  private searchNameProductsUrl = '/word';
  private httpOptions;
  constructor(private http: HttpClient) {
    this.baseUrl = environment.APIEndpoint;
    this.httpOptions = {
      responseType: 'text',
      headers: new HttpHeaders({ })
    };
  }

  searchWord(word: string): Observable<Object> {
    var url = this.baseUrl + this.searchNameProductsUrl + '/' + word;
    return this.http.get<Object>(url);
  }

}
