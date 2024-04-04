import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GalleryRoutingModule } from './gallery-routing.module';
import { ImageDialogComponent } from './dialog/image-dialog/image-dialog.component';
import {MatDialogContent, MatDialogTitle} from "@angular/material/dialog";


@NgModule({
  declarations: [
    ImageDialogComponent
  ],
  imports: [
    CommonModule,
    GalleryRoutingModule,
    MatDialogContent,
    MatDialogTitle
  ]
})
export class GalleryModule { }
