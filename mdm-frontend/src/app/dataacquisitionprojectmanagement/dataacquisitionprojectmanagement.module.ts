import { Injector, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DataacquisitionprojectSearchService } from './dataacquisitionproject.search.service';
import { UpgradeModule } from '@angular/upgrade/static';
import { ProjectCockpitComponent } from './project-cockpit/project-cockpit.component';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { MatChipsModule } from '@angular/material/chips';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatIconModule } from '@angular/material/icon';
import { DataacquisitionprojectDataService } from './dataacquisitionproject.data.service';
import { HttpClientModule } from '@angular/common/http';
import { ReleaseStatusBadgeComponent } from './release-status-badge/release-status-badge.component';
import { AssigneeStatusBadgeComponent } from './assignee-status-badge/assignee-status-badge.component';
import { ReleaseButtonComponent } from './release-button/release-button.component';



@NgModule({
  declarations: [
    ProjectCockpitComponent,
    ReleaseStatusBadgeComponent,
    AssigneeStatusBadgeComponent,
    ReleaseButtonComponent
  ],
  imports: [
    CommonModule,
    UpgradeModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatTabsModule,
    MatIconModule,
    MatChipsModule,
    MatButtonModule,
    MatTooltipModule,
    HttpClientModule
  ],
  providers: [
    DataacquisitionprojectSearchService,
    DataacquisitionprojectDataService
  ]
})
export class DataacquisitionprojectmanagementModule {}