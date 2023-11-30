import { Injectable } from '@angular/core';
import { downgradeInjectable, getAngularJSGlobal } from '@angular/upgrade/static';
import { BoolQuery, Query, QueryBody, QueryFragment, QueryFragmentType } from './search.interfaces';

const DEFAULT_SORTING = 
    [
        '_score',
        {
            'id': {
                'order': 'asc'
            }
        }
    ];

const DATA_ACQUISITION_PROJECT_INDEX_NAME = 'data_acquisition_projects';

/**
 * Service for creating Elasticsearch queries for data acquisition projects
 */
@Injectable({
    providedIn: 'root'
})
export class DataacquisitionprojectSearchService {

    constructor() {}

    /**
     * Creates an empty Elasticsearch bool query.
     */
    createBoolQuery() {
        const query: BoolQuery = {
            bool: {
                filter: [],
                must: [],
                must_not: [],
                should: [],
            },
        };
        return query;
    }

    /**
     * Method creating a filter fragment of an Elasticsearch query according to
     * the given type.
     * @param queryFragmentType the fragment type
     * @param base the base object usually consisting of a field name and a value
     * @returns the fragment
     */
    createFilterFragment(queryFragmentType: QueryFragmentType, base: Object) {
        let fragment : QueryFragment = {};
        switch (queryFragmentType) {
            case QueryFragmentType.term:
                fragment.term = base;
                break;
            case QueryFragmentType.terms:
                fragment.terms = base;
                break;
            case QueryFragmentType.exists:
                fragment.exists = base;
                break;
            case QueryFragmentType.match:
                fragment.match = base;
                break;
            default:
                throw new Error(`Unkown Queryfragment type ${queryFragmentType}!`);
        }
        return fragment;
    }

    /**
     * Method returning default sorting option for Elasticsearch. Default sorting
     * sorts by score and the project ID in ascending order.
     * @returns a partial json for sorting consisting of a list of sorting options
     */
    getDefaultSorting() {
        return DEFAULT_SORTING;
    }

    /**
     * Method assembling a filter query for searching data acquisition projects based on the set filters.
     * Available filters are:
     *    - project id
     *    - project type (data package or analysis package)
     *    - assigned user group
     *    - release state
     *    - if variables, questions, publications an/or concepts are required (data packages only)
     *    - if additional remarks for the user service are given (data packages only)
     * @param pageIndex the index of the currently requested page
     * @param pageSize the number of items per page
     * @param projectType  the type of the project (dataPackages or analysisPackages)
     * @param assignedProjectsOnly if only projects assigned to the given user should be returned
     * @param projectId the ID of a project
     * @param assigneeGroup the user group the project must be assigned to (DATA_PROVIDER or PUBLISHER)
     * @param releaseState the release state (released or unreleased)
     * @param filterDataPackages a list of data package parts required in the project
     * @param additionalInfo if additional remarks for the user service are given
     * @param loginName the login name of the user
     * @returns an Elasticsearch query
     */
    createFilterQueryForProjects(pageIndex: number, 
        pageSize: number,
        projectType: String | null,
        assignedProjectsOnly: boolean,
        projectId: String | null, 
        assigneeGroup: String | null, 
        releaseState: String | null, 
        filterDataPackages: String[] | null,
        additionalInfo: String | null,
        loginName?: String): Query {
        
        let boolQuery = this.createBoolQuery();
        
        // add no shadows filter
        let noShadowBase = {'shadow': false};
        let noShadowsFilter = this.createFilterFragment(QueryFragmentType.term, noShadowBase);
        boolQuery.bool.filter?.push(noShadowsFilter);

        // dataproviders should only see assigned data
        if (assignedProjectsOnly && loginName) {
            let assignedOnlyBase = {'configuration.dataProviders': loginName};
            let assignedOnlyFilter = this.createFilterFragment(QueryFragmentType.term, assignedOnlyBase);
            boolQuery.bool.filter?.push(assignedOnlyFilter);
        }

        if (projectType) {
            let requirement = 'configuration.requirements.is' + projectType.charAt(0).toUpperCase() + projectType.slice(1) + 'Required'
            let typeBase = {[requirement]: true};
            let typeFilter = this.createFilterFragment(QueryFragmentType.term, typeBase);
            boolQuery.bool.filter?.push(typeFilter);
        }

        if (projectId) {
            let projectBase = {'id': projectId};
            let projectFilter = this.createFilterFragment(QueryFragmentType.term, projectBase)
            boolQuery.bool.filter?.push(projectFilter);
        }

        if (assigneeGroup) {
            let assigneeGroupBase = {'assigneeGroup': assigneeGroup};
            let assigneeGroupFilter = this.createFilterFragment(QueryFragmentType.term, assigneeGroupBase)
            boolQuery.bool.filter?.push(assigneeGroupFilter);
        }

        if (releaseState) {
            let releaseStateBase = {'field': 'release'};
            let releaseStateFilter = this.createFilterFragment(QueryFragmentType.exists, releaseStateBase)
            if (releaseState === "true") {
                boolQuery.bool.must?.push(releaseStateFilter);
            } else {
                boolQuery.bool.must_not?.push(releaseStateFilter);
            }
        }

        if (filterDataPackages && filterDataPackages.length > 0) {
            for (let dpFilter of filterDataPackages) {
                let fieldName = 'configuration.requirements.is' + dpFilter + 'Required';
                let dpFilterBase = {[fieldName]: true};
                let dpFilterFilter = this.createFilterFragment(QueryFragmentType.term, dpFilterBase);
                boolQuery.bool.filter?.push(dpFilterFilter);
            }
        }

        if (additionalInfo) {
            let additionalBase = {'hasUserServiceRemarks': additionalInfo === 'true' ? true : false};
            let additionalFilter = this.createFilterFragment(QueryFragmentType.term, additionalBase);
            boolQuery.bool.filter?.push(additionalFilter);
        }

        let queryBody : QueryBody = {
            track_total_hits: true,
            query: boolQuery,
            from: (pageIndex) * pageSize,
            size: pageSize,
            sort: this.getDefaultSorting()
        };

        let query : Query = {
            index: DATA_ACQUISITION_PROJECT_INDEX_NAME,
            body: queryBody
        };
        return query;
    }

    /**
     * Method assembling a search query for searching data acquisition projects based on their ID.
     * This query can be used in a 'search as you type' scenario.
     * Furthermore, the query can filter for projects of a certain type (data packages or analysis packages),
     * and handles user permissions as DATAPROVIDERS are only able to see their assigned projects.
     * @param projectType the type of the project (dataPackages or analysisPackages)
     * @param assignedProjectsOnly if only projects assigned to the given user should be returned
     * @param searchTerm the search term to use for matching across project IDs
     * @param loginName the login name of the user
     * @returns an Elasticsearch query
     */
    createSearchQueryForProjectsById(
        projectType: String | null,
        assignedProjectsOnly: boolean,
        searchTerm: String | null,
        loginName?: String) : Query {
        
        let boolQuery = this.createBoolQuery();
    
        // add no shadows filter
        let noShadowBase = {'shadow': false};
        let noShadowsFilter = this.createFilterFragment(QueryFragmentType.term, noShadowBase);
        boolQuery.bool.filter?.push(noShadowsFilter);

        // dataproviders should only see assigned data
        if (assignedProjectsOnly && loginName) {
            let assignedOnlyBase = {'configuration.dataProviders': loginName};
            let assignedOnlyFilter = this.createFilterFragment(QueryFragmentType.term, assignedOnlyBase);
            boolQuery.bool.filter?.push(assignedOnlyFilter);
        }

        if (projectType) {
            let requirement = 'configuration.requirements.is' + projectType.charAt(0).toUpperCase() + projectType.slice(1) + 'Required'
            let typeBase = {[requirement]: true};
            let typeFilter = this.createFilterFragment(QueryFragmentType.term, typeBase);
            boolQuery.bool.filter?.push(typeFilter);
        }

        let searchTermBase = {
            "id.ngrams": {
              "query": searchTerm || '',
              "operator": "AND",
              "minimum_should_match": "100%",
              "zero_terms_query": "ALL"
          }
          }
        let projectFilter = this.createFilterFragment(QueryFragmentType.match, searchTermBase)
        boolQuery.bool.filter?.push(projectFilter);
        let queryBody : QueryBody = {
            track_total_hits: true,
            query: boolQuery,
            from: 0,
            size: 10000,
            sort: this.getDefaultSorting()
        };

        let query : Query = {
            index: DATA_ACQUISITION_PROJECT_INDEX_NAME,
            body: queryBody
        };
        return query;
    }
}

// necessary for using service in AngularJS
getAngularJSGlobal()
    .module('metadatamanagementApp')
    .factory('dataAcquisitionProjectSearchService', downgradeInjectable(DataacquisitionprojectSearchService) as any);
