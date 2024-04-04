import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ExplorePageComponent} from "./pages/explore-page/explore-page.component";
import {UploadPageComponent} from "./pages/upload-page/upload-page.component";

const routes: Routes = [
  {path: 'explore', component: ExplorePageComponent},
  {path: 'upload', component: UploadPageComponent},
  {path: '', redirectTo: '/explore', pathMatch: 'full'},
  {path: '**', redirectTo: '/explore'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GalleryRoutingModule { }
