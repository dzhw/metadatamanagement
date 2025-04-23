import { NgModule } from "@angular/core";
import { InstrumentAttachmentTypesEn } from "./domain/instrument-attachment-types";
import { CommonModule } from "@angular/common";
import { downgradeInjectable, getAngularJSGlobal } from "@angular/upgrade/static";
import {
    InstrumentAttachmentCitationDialogComponent,
    InstrumentAttachmentCitationDialogService
} from "./views/instrument-citation-dialog/instrument-attachment-citation-dialog.component";
import { MatButtonModule } from "@angular/material/button";
import { MatDividerModule } from "@angular/material/divider";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatIconModule } from "@angular/material/icon";
import { MatDialogModule } from "@angular/material/dialog";
import { TranslatePipe } from "../common/services/translate.pipe";

getAngularJSGlobal().module("metadatamanagementApp")
    .constant("InstrumentAttachmentTypesEn", InstrumentAttachmentTypesEn)
    .factory("InstrumentAttachmentCitationDialogService",
        downgradeInjectable(InstrumentAttachmentCitationDialogService));

@NgModule({
    declarations: [
        InstrumentAttachmentCitationDialogComponent
    ],
    imports: [
        CommonModule,
        TranslatePipe,
        MatButtonModule,
        MatDividerModule,
        MatTooltipModule,
        MatIconModule,
        MatDialogModule
    ],
    providers: [
        {
            provide: "SimpleMessageToastService",
            useFactory: ($injector: any) => $injector.get("SimpleMessageToastService"),
            deps: ["$injector"]
        },
        {
            provide: "CitationHintGeneratorService",
            useFactory: ($injector: any) => $injector.get("CitationHintGeneratorService"),
            deps: ["$injector"]
        },
        InstrumentAttachmentCitationDialogService
    ]
})
export class InstrumentManagementModule {}
