'use strict';

angular.module('metadatamanagementApp').service('SearchDao',
    function(ElasticSearchProperties, Language, ElasticSearchClient,
        CleanJSObjectService) {
        return {
            search: function(queryterm, pageNumber, currentProject,
              elasticsearchType, pageSize) {
              var query = {};
              var projectFilter;
              var studiesFilter;
              query.index = 'metadata_' + Language.getCurrentInstantly();
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
                    'must': [
                      {'match_all': {}}
                    ],
                  }
                };
              }
              //define from
              query.body.from = (pageNumber - 1) * pageSize;
              //define size
              query.body.size = pageSize;
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
              //filter by projectId
              if (!CleanJSObjectService.isNullOrEmpty(currentProject)) {
                projectFilter = {
                  'term': {'dataAcquisitionProjectId': currentProject.id}
                };
                studiesFilter = {
                  'term': {'studyIds': currentProject.id}
                };
                if (!query.body.query.bool.filter) {
                  query.body.query.bool.filter = {};
                  query.body.query.bool.filter.bool = {};
                  query.body.query.bool.filter.bool.should = [];
                }
                query.body.query.bool.filter.bool.should.push(projectFilter);
                query.body.query.bool.filter.bool.should.push(studiesFilter);
              }

              return ElasticSearchClient.search(query);
            }
          };
      });
