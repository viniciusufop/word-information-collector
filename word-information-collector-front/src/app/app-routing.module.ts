import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { WordResultComponent } from './word-result/word-result.component';


const routes: Routes = [
  {
    path: '',
    component: WordResultComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {onSameUrlNavigation: 'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
