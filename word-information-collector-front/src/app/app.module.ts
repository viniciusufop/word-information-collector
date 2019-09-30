import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { WordResultComponent } from './word-result/word-result.component';
import { CytoscapeModule } from 'ngx-cytoscape'; // <= Add this TS import
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    WordResultComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CytoscapeModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
