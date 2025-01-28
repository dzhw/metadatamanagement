import { Inject, Pipe, PipeTransform } from "@angular/core";

// Bridge for using legacy angular-translate in regular Angular templates
// Documentation: https://angular-translate.github.io/docs/#/guide/03_using-translate-service

/**
 * Provides a pipe to translate text using the keys defined in each
 * translation file. This might fail if translations have not been
 * configured yet with the translation provider.
 * 
 * Usage:
 * ```
 * <p>{{ "module.message" | translate }}</p>
 * ```
 */
@Pipe({
    name: "translate",
    standalone: true
})
export class TranslatePipe implements PipeTransform {

    constructor(@Inject("$translate") private translate: any) {}

    transform(key: string): Promise<string> {
        return this.translate.instant(key);
    }
}

/**
 * Provides an asynchronous pipe to translate text using the keys defined in
 * each translation file. Not sure if we ever need this one, but just in case.
 * 
 * Usage:
 * ```
 * <p>{{ "module.message" | translateAsync | async }}</p>
 * ```
 */
@Pipe({
    name: "translateAsync",
    standalone: true
})
export class AsyncTranslatePipe implements PipeTransform {

    constructor(@Inject("$translate") private translate: any) {}

    async transform(key: string): Promise<string> {
        return await new Promise((resolve) => this.translate(key).then((result: string) => resolve(result)));
    }
}
