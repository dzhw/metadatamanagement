/* global _*/
'use strict';

angular.module('metadatamanagementApp').service('SearchDao',
  function(ElasticSearchProperties, LanguageService, ElasticSearchClient,
    CleanJSObjectService) {
    var keyMapping = {
      'variables': {'data-set': 'dataSetIds'}
    };
    return {
      search: function(queryterm, pageNumber, dataAcquisitionProjectId, filter,
        elasticsearchType, pageSize) {
        var query = {};
        var projectFilter;
        var studiesFilter;
        query.index = 'metadata_' + LanguageService.getCurrentInstantly();
        query.type = elasticsearchType;
        query.body = {};
        //a query term
        if (!CleanJSObjectService.isNullOrEmpty(queryterm)) {
          query.body.query = {
            'bool': {
              'must': [{
                'match': {
                  'allStringsAsNgrams': {
                    'query': queryterm,
                    'type': 'boolean',
                    'operator': 'AND',
                    'minimum_should_match': '100%',
                    'zero_terms_query': 'NONE'
                  }
                }
              }]
            }
          };
          //no query term
        } else {
          query.body.query = {
            'bool': {
              'must': [{
                'match_all': {}
              }],
            }
          };
        }
        //define from
        query.body.from = (pageNumber - 1) * pageSize;
        //define size
        query.body.size = pageSize;
        query.body.query.bool.filter = {};
        query.body.query.bool.filter.bool = {};
        //aggregations if user is on the all tab
        if (CleanJSObjectService.isNullOrEmpty(elasticsearchType)) {
          //define aggregations
          query.body.aggs = {
            'countByType': {
              'terms': {
                'field': '_type'
              }
            }
          };
        }
        // this filter section should be refactored
        // filter by projectId
        if (dataAcquisitionProjectId && CleanJSObjectService
          .isNullOrEmpty(filter)) {
          projectFilter = {
            'term': {
              'dataAcquisitionProjectId': dataAcquisitionProjectId
            }
          };
          studiesFilter = {
            'term': {
              'studyIds': dataAcquisitionProjectId
            }
          };
          query.body.query.bool.filter.bool.should = [];
          query.body.query.bool.filter.bool.should.push(projectFilter);
          query.body.query.bool.filter.bool.should.push(studiesFilter);
        }

        if (!CleanJSObjectService.isNullOrEmpty(filter)) {
          projectFilter = {
            'term': {
              'dataAcquisitionProjectId': dataAcquisitionProjectId
            }
          };
          query.body.query.bool.filter.bool.must = [];
          _.each(filter, function(value, key) {
            var filterKeyValue = {'term': {}};
            if (elasticsearchType) {
              try {
                var subKeyMapping = keyMapping[elasticsearchType];
                key = subKeyMapping[key];
                filterKeyValue.term[key] = value;
                query.body.query.bool.filter.bool.must.push(filterKeyValue);
              } catch (e) {
                //SimpleMessage
                console.log(key);
              }
            }
          });
        }
        return ElasticSearchClient.search(query);
      }
    };
  });
