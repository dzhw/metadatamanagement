import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DataacquisitionprojectSearchService } from './dataacquisitionproject.search.service';
import { UpgradeModule } from '@angular/upgrade/static';
import { ProjectCockpitComponent } from './project-cockpit/project-cockpit.component';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatIconModule} from '@angular/material/icon';



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
    MatIconModule
  ],
  providers: [
    DataacquisitionprojectSearchService
  ]
})
export class DataacquisitionprojectmanagementModule {}