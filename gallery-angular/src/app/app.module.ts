import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {CoreModule} from "./core/core.module";
import { ExplorePageComponent } from './modules/gallery/pages/explore-page/explore-page.component';
import { UploadPageComponent } from './modules/gallery/pages/upload-page/upload-page.component';
import { MatGridListModule} from "@angular/material/grid-list";
import {HttpClientModule} from "@angular/common/http";
import {MatPaginator} from "@angular/material/paginator";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {LayoutModule} from "@angular/cdk/layout";
import {MatCard, MatCardContent} from "@angular/material/card";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
@NgModule({
  declarations: [
    AppComponent,
    ExplorePageComponent,
    UploadPageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CoreModule,
    MatGridListModule,
    HttpClientModule,
    MatPaginator,
    FormsModule,
    LayoutModule,
    ReactiveFormsModule,
    MatCard,
    MatCardContent,
    MatFormField,
    MatInput,
    MatButton,
    MatLabel
  ],
  providers: [
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
