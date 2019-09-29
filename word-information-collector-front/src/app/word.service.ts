import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WordService {
  private baseUrl: string;
  private searchNameProductsUrl = '/word';

  constructor(private http: HttpClient) {
    // this.baseUrl = environment.APIEndpoint + '/products/public';
    this.baseUrl = 'http://localhost:8080';
  }

  public searchWord(word: string): Observable<string[]> {
    return this.http.get<string[]>(this.baseUrl + this.searchNameProductsUrl + '/' + word);
  }

}
