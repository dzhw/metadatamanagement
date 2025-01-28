import { ChangeDetectionStrategy, Component, Inject, Injectable } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialog } from "@angular/material/dialog";


/**
 * The type of item passed to the dialog service the InstrumentDetailController.
 * @see eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentCitationDetails
 */
interface CitationItem {
    hint: string; // the citation details as a string
    details: any; // the citation details object (TODO create interface for citation details)
}

/**
 * A service for opening the instrument attachment citation dialog.
 */
@Injectable()
export class InstrumentAttachmentCitationDialogService {
    constructor(private dialog: MatDialog) {}
    openDialog(citationItems: string[]) {
        this.dialog.open(InstrumentAttachmentCitationDialogComponent, {
            autoFocus: false,
            data: citationItems
        });
    }
}

/**
 * A dialog that lists all instrument attachment citation hints where users can
 * copy them to clipboard or download them in BibTex format.
 */
@Component({
    selector: "fdz-instrument-citation-dialog",
    templateUrl: "./instrument-attachment-citation-dialog.component.html",
    styleUrls: ["./instrument-attachment-citation-dialog.component.scss"],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class InstrumentAttachmentCitationDialogComponent {

    constructor(
        @Inject(MAT_DIALOG_DATA) public citationItems: CitationItem[] = [],
        @Inject("SimpleMessageToastService") public simpleMessageToastService: any,
        @Inject("CitationHintGeneratorService") public citationHintGeneratorService: any,
    ) {}

    /**
     * Selects and copies the text content of the
     * element denoted by 'id' to the clipboard.
     * @param id the ID of the element
     */
    copyToClipboard(id: string) {
        const el = document.querySelector(id);
        let success = false;
        if (el) {
            console.log(document.getSelection());
            window.getSelection()?.selectAllChildren(el);
            success = document.execCommand("copy");
        }
        if (success) {
            this.simpleMessageToastService.openSimpleMessageToast(
                "shopping-cart.detail.citation-success-copy-to-clipboard");
        }
    }

    /**
     * Formats a date to a string.
     * @param date the date to be formatted
     * @returns the formatted string
     */
    private getDateString(date: Date): string {
        const fmt = (value: number) => (value < 10 ? "0" : "") + value;
        const yyyy = "" + date.getFullYear();
        const MM = fmt(date.getMonth() + 1); // zero-based
        const dd = fmt(date.getDate());
        const HH = fmt(date.getHours());
        const mm = fmt(date.getMinutes());
        return `${MM}/${dd}/${yyyy} at ${HH}:${mm}`;
    }

    /**
     * Creates a BibTex document out of the provided citation details and
     * triggers its download for the user.
     * @param item the citation item that includes the details
     */
    downloadBibTex(item: CitationItem) {

        const bibtex = this.citationHintGeneratorService.generateBibtexForInstrumentAttachment(item.details) as string;

        const result = bibtex.match(/{(.*),/);
        if (!result) {
            throw "Unable to extract key from BibTex data";
        }
        const bibtexKey = result[1];

        const timestamp = this.getDateString(new Date());
        const newline = navigator.appVersion.indexOf("Win") !== -1 ? "\r\n" : "\n";
        const content = `% This file was created on ${timestamp}${newline}${newline}${bibtex}`;

        const url = window.URL.createObjectURL(new Blob([content], { type: "text/plain; charset=utf-8" }));
        const link = document.createElement("a");
        link.style.display = "none";
        link.href = url;
        link.download = bibtexKey + ".bib"; // file name
        document.body.appendChild(link);
        link.click();
        window.URL.revokeObjectURL(url);
        link.remove();
    }
}
