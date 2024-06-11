import { DoBootstrap, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { UpgradeModule } from '@angular/upgrade/static';
import { DataacquisitionprojectmanagementModule } from 
  './dataacquisitionprojectmanagement/dataacquisitionprojectmanagement.module';

// import { AppRoutingModule } from './app-routing.module';
// import { AppComponent } from './app.component';

// import * as angular from 'angular';
// import { setAngularJSGlobal } from '@angular/upgrade/static';
// setAngularJSGlobal(angular);

// import { appModule } from './app';

@NgModule({
  // declarations: [
  //   AppComponent
  // ],
  imports: [
    BrowserModule,
    //AppRoutingModule
    UpgradeModule,
    DataacquisitionprojectmanagementModule
  ],
  // providers: [],
  // bootstrap: [AppComponent]
})
export class AppModule implements DoBootstrap {
  constructor(private upgrade: UpgradeModule) { }
  ngDoBootstrap() {
    this.upgrade.bootstrap(document.documentElement, ['metadatamanagementApp'], { strictDi: true });
  }
}
