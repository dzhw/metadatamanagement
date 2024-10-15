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

    // constructor() {}

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
    createFilterFragment(queryFragmentType: QueryFragmentType, base: { [key: string]: unknown; } | undefined) {
        const fragment : QueryFragment = {};
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
     * @param releaseState the release state (released, unreleased or pre-released)
     * @param filterDataPackages a list of data package parts required in the project
     * @param additionalInfo if additional remarks for the user service are given
     * @param loginName the login name of the user
     * @returns an Elasticsearch query
     */
    createFilterQueryForProjects(pageIndex: number, 
        pageSize: number,
        projectType: string | null,
        assignedProjectsOnly: boolean,
        projectId: string | null, 
        assigneeGroup: string | null, 
        releaseState: string | null, 
        filterDataPackages: string[] | null,
        additionalInfo: string | null,
        loginName?: string): Query {
        
        const boolQuery = this.createBoolQuery();
        
        // add no shadows filter
        const noShadowBase = {'shadow': false};
        const noShadowsFilter = this.createFilterFragment(QueryFragmentType.term, noShadowBase);
        boolQuery.bool.filter?.push(noShadowsFilter);

        // dataproviders should only see assigned data
        if (assignedProjectsOnly && loginName) {
            const assignedOnlyBase = {'configuration.dataProviders': loginName};
            const assignedOnlyFilter = this.createFilterFragment(QueryFragmentType.term, assignedOnlyBase);
            boolQuery.bool.filter?.push(assignedOnlyFilter);
        }

        if (projectType) {
            const requirement = 'configuration.requirements.is' + projectType.charAt(0).toUpperCase() 
                + projectType.slice(1) + 'Required';
            const typeBase = {[requirement]: true};
            const typeFilter = this.createFilterFragment(QueryFragmentType.term, typeBase);
            boolQuery.bool.filter?.push(typeFilter);
        }

        if (projectId) {
            const projectBase = {'id': projectId};
            const projectFilter = this.createFilterFragment(QueryFragmentType.term, projectBase);
            boolQuery.bool.filter?.push(projectFilter);
        }

        if (assigneeGroup) {
            const assigneeGroupBase = {'assigneeGroup': assigneeGroup};
            const assigneeGroupFilter = this.createFilterFragment(QueryFragmentType.term, assigneeGroupBase);
            boolQuery.bool.filter?.push(assigneeGroupFilter);
        }

        if (releaseState) {
            const releaseStateBase = {'field': 'release'};
            const releaseStateFilter = this.createFilterFragment(QueryFragmentType.exists, releaseStateBase);
            if (releaseState === "released") {
                boolQuery.bool.must?.push(releaseStateFilter);
                boolQuery.bool.must?.push({
                    "term": {
                        "release.isPreRelease": false
                    }
                });
            } 
            if (releaseState === "unreleased") {
                boolQuery.bool.must_not?.push(releaseStateFilter);
            }
            if (releaseState === "pre-released") {
                boolQuery.bool.must?.push(releaseStateFilter);
                boolQuery.bool.must?.push({
                    "term": {
                        "release.isPreRelease": true
                    }
                });
            }
        }

        if (filterDataPackages && filterDataPackages.length > 0) {
            for (const dpFilter of filterDataPackages) {
                const fieldName = 'configuration.requirements.is' + dpFilter + 'Required';
                const dpFilterBase = {[fieldName]: true};
                const dpFilterFilter = this.createFilterFragment(QueryFragmentType.term, dpFilterBase);
                boolQuery.bool.filter?.push(dpFilterFilter);
            }
        }

        if (additionalInfo) {
            const additionalBase = {'hasUserServiceRemarks': additionalInfo === 'true' ? true : false};
            const additionalFilter = this.createFilterFragment(QueryFragmentType.term, additionalBase);
            boolQuery.bool.filter?.push(additionalFilter);
        }

        const queryBody : QueryBody = {
            track_total_hits: true,
            query: boolQuery,
            from: (pageIndex) * pageSize,
            size: pageSize,
            sort: this.getDefaultSorting()
        };

        const query : Query = {
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
        projectType: string | null,
        assignedProjectsOnly: boolean,
        searchTerm: string | null,
        loginName?: string | null) : Query {
        
        const boolQuery = this.createBoolQuery();
    
        // add no shadows filter
        const noShadowBase = {'shadow': false};
        const noShadowsFilter = this.createFilterFragment(QueryFragmentType.term, noShadowBase);
        boolQuery.bool.filter?.push(noShadowsFilter);

        // dataproviders should only see assigned data
        if (assignedProjectsOnly && loginName) {
            const assignedOnlyBase = {'configuration.dataProviders': loginName};
            const assignedOnlyFilter = this.createFilterFragment(QueryFragmentType.term, assignedOnlyBase);
            boolQuery.bool.filter?.push(assignedOnlyFilter);
        }

        if (projectType) {
            const requirement = 'configuration.requirements.is' + projectType.charAt(0).toUpperCase() 
                + projectType.slice(1) + 'Required';
            const typeBase = {[requirement]: true};
            const typeFilter = this.createFilterFragment(QueryFragmentType.term, typeBase);
            boolQuery.bool.filter?.push(typeFilter);
        }

        const searchTermBase = {
            "id.ngrams": {
              "query": searchTerm || '',
              "operator": "AND",
              "minimum_should_match": "100%",
              "zero_terms_query": "ALL"
          }
        };
        const projectFilter = this.createFilterFragment(QueryFragmentType.match, searchTermBase);
        boolQuery.bool.filter?.push(projectFilter);
        const queryBody : QueryBody = {
            track_total_hits: true,
            query: boolQuery,
            from: 0,
            size: 10000,
            sort: this.getDefaultSorting()
        };

        const query : Query = {
            index: DATA_ACQUISITION_PROJECT_INDEX_NAME,
            body: queryBody
        };
        return query;
    }

    /**
     * Method assembling a search query to find a specific project by its id.
     * @param projectType the type of the project (dataPackages or analysisPackages)
     * @param projectId the project id
     * @returns an Elasticsearch query
     */
    getProjectByIdQuery(
        projectType: string | null,
        projectId: string | null) : Query {
        
        const boolQuery = this.createBoolQuery();
    
        // add no shadows filter
        const noShadowBase = {'shadow': false};
        const noShadowsFilter = this.createFilterFragment(QueryFragmentType.term, noShadowBase);
        boolQuery.bool.filter?.push(noShadowsFilter);

        if (projectType) {
            const requirement = 'configuration.requirements.is' + projectType.charAt(0).toUpperCase() 
                + projectType.slice(1) + 'Required';
            const typeBase = {[requirement]: true};
            const typeFilter = this.createFilterFragment(QueryFragmentType.term, typeBase);
            boolQuery.bool.filter?.push(typeFilter);
        }

        const projectFilter = this.createFilterFragment(QueryFragmentType.term, {id: projectId});
        boolQuery.bool.must?.push(projectFilter);
        const queryBody : QueryBody = {
            track_total_hits: true,
            query: boolQuery,
            from: 0,
            size: 10,
            sort: this.getDefaultSorting()
        };
        console.log(queryBody)

        const query : Query = {
            index: DATA_ACQUISITION_PROJECT_INDEX_NAME,
            body: queryBody
        };
        return query;
    }
}

// necessary for using service in AngularJS
getAngularJSGlobal()
    .module('metadatamanagementApp')
    .factory('dataAcquisitionProjectSearchService', 
        downgradeInjectable(DataacquisitionprojectSearchService) as unknown);
