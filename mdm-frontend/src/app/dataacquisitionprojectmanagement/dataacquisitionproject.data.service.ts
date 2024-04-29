import { Inject, Injectable } from "@angular/core";
import { DataAcquisitionProject } from "./data/dataacquisitionprojectmanagement.data";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { lastValueFrom } from "rxjs";
import { environment } from "src/environments/dev/environment";

export interface TokenResponse {
  access_token: string,
  token_type: string,
  refresh_token: string,
  scope: string
}

/**
 * Service for handling REST requests
 */
@Injectable({
    providedIn: 'root'
})
export class DataacquisitionprojectDataService {

    readonly apiPath = environment.api.url;
    readonly urlDataacquisitionProjectsResource = environment.projects.urlDataacquisitonProjectsResource;

    constructor(private http: HttpClient) {}

    /**
     * Fetches DataacquisitionProject dataset by id
     * @param id Project id
     * @returns DataacquisitionProject
     */
    async fetchProjectById(id: string): Promise<DataAcquisitionProject> {
        let t : TokenResponse | null = null;
        const value = localStorage.getItem("metadatamanagementApp.token");
        if (value) {
          t = JSON.parse(value);
        }
        const url = this.apiPath + this.urlDataacquisitionProjectsResource + "/" + id;
        const httpOptions = {
            headers: new HttpHeaders({
              'Content-Type':  'application/json',
              'Authorization': 'Bearer ' + t?.access_token
            })
          };
          
        return await lastValueFrom(this.http.get<DataAcquisitionProject>(url, httpOptions));
    }
}