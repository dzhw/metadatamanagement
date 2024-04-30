/**
 * Exports data interfaces for type savety
 */


export interface DataAcquisitionProject {
    id: string;
    assigneeGroup: string;
    configuration: ProjectConfiguration;
    createdBy: string;
    createdDate: string;
    hasBeenReleasedBefore: boolean;
    hasUserServiceRemarks: boolean;
    hidden: boolean;
    lastAssigneeGroupMassage: string;
    lastModifiedBy: string;
    listModifiedDate: string;
    masterId: string;
    shadow: boolean;
    version: number;
    release?: Release;
}

export interface ProjectConfiguration {
    publishers: [];
    dataproviders: [];
    requirements: ProjectRequirements;
    dataPackagesState?: ProjectPartState;
    analysisPackagesState?: ProjectPartState;
    datasetsState?: ProjectPartState;
    instrumentsState?: ProjectPartState;
    surveysState?: ProjectPartState;

}

export interface ProjectRequirements {
    analysisPackagesRequired: boolean;
    conceptsRequired: boolean;
    dataPackagesRequired: boolean;
    dataSetsRequired: boolean;
    instrumentsRequired: boolean;
    publicationsRequired: boolean;
    questionsRequired: boolean;
    surveysRequired: boolean;
    variablesRequired: boolean;
}

export interface ProjectPartState {
    publisherReady: boolean;
    dataproviderReady: boolean;
}

export interface Release {
    version: string;
    lastDate: Date;
    firstDate: Date;
    pinToStartPage: boolean;
    doiPageLanguage: boolean;
}