import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { K6Component } from './k6/k6.component';
import { ApacheComponent } from './apache/apache.component';

const routes: Routes = [
  { path: 'apache' , component: ApacheComponent },
  { path: 'k6', component: K6Component },
  { path: '', redirectTo: 'apache', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
