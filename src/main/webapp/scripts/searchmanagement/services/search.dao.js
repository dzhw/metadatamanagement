'use strict';

angular.module('metadatamanagementApp').service('SearchDao',
    function(ElasticSearchProperties, Language, ElasticSearchClient,
        CleanJSObjectService) {
        return {
            search: function(queryterm, pageNumber, elasticsearchType) {
                var query = {};
                query.index = 'metadata_' + Language.getCurrentInstantly();
                query.type = elasticsearchType;
                query.body = {};

                //a query term
                if (!CleanJSObjectService.isNullOrEmpty(queryterm)) {
                  query.body.query = {
                      'bool': {
                          'should': [{
                              'match': {
                                  '_all': {
                                      'query': queryterm,
                                      'type': 'boolean',
                                      'operator': 'AND',
                                      'zero_terms_query': 'NONE'
                                    }
                                }
                            }, {
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
                      'match_all': {}
                    };
                }

                //define from
                query.body.from = (pageNumber - 1) *
                  ElasticSearchProperties.pageSize;

                //define size
                query.body.size = ElasticSearchProperties.pageSize;

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
                return ElasticSearchClient.search(query);
              }
          };
      });
