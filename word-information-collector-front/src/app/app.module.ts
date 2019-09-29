import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { WordResultComponent } from './word-result/word-result.component';
import { CytoscapeModule } from 'ngx-cytoscape'; // <= Add this TS import

@NgModule({
  declarations: [
    AppComponent,
    WordResultComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CytoscapeModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
