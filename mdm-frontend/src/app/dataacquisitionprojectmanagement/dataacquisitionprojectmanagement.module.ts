import { Injector, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DataacquisitionprojectSearchService } from './dataacquisitionproject.search.service';
import { UpgradeModule } from '@angular/upgrade/static';
import { ProjectCockpitComponent } from './project-cockpit/project-cockpit.component';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatIconModule } from '@angular/material/icon';
import { DataacquisitionprojectDataService } from './dataacquisitionproject.data.service';
import { HttpClientModule } from '@angular/common/http';



@NgModule({
  declarations: [
    ProjectCockpitComponent
  ],
  imports: [
    CommonModule,
    UpgradeModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatTabsModule,
    MatIconModule,
    HttpClientModule
  ],
  providers: [
    DataacquisitionprojectSearchService,
    DataacquisitionprojectDataService
  ]
})
export class DataacquisitionprojectmanagementModule {}