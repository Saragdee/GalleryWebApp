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
    HttpClientModule
  ],
  providers: [
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
