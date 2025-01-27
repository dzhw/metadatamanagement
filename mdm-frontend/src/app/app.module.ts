import { DoBootstrap, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { UpgradeModule } from '@angular/upgrade/static';
import { DataacquisitionprojectmanagementModule } from 
  './dataacquisitionprojectmanagement/dataacquisitionprojectmanagement.module';
import { InstrumentManagementModule } from "./instrumentmanagement/instrument-management.module";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

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
    BrowserAnimationsModule,
    //AppRoutingModule
    UpgradeModule,
    DataacquisitionprojectmanagementModule,
    InstrumentManagementModule,
  ],
  providers: [
    {
      provide: "$translate",
      useFactory: ($injector: any) => $injector.get("$translate"),
      deps: ["$injector"]
    }
  ],
  // bootstrap: [AppComponent]
})
export class AppModule implements DoBootstrap {
  constructor(private upgrade: UpgradeModule) { }
  ngDoBootstrap() {
    this.upgrade.bootstrap(document.documentElement, ['metadatamanagementApp'], { strictDi: true });
  }
}
