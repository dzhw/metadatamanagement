/* global  _*/
'use strict';

angular.module('metadatamanagementApp').factory('VariableSearchService',
  function(ElasticSearchClient, $q, SearchHelperService,
    CleanJSObjectService, LanguageService) {
      var createQueryObject = function(type) {
        type = type || 'variables';
        return {
          index: type,
          type: type
        };
      };

      var createTermFilters = function(filter, dataAcquisitionProjectId, type) {
        type = type || 'variables';
        var termFilter;
        if (!CleanJSObjectService.isNullOrEmpty(filter) ||
          !CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
          termFilter = [];
        }
        if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
          var projectFilter = {
            term: {
              dataAcquisitionProjectId: dataAcquisitionProjectId
            }
          };
          termFilter.push(projectFilter);
        }
        if (!CleanJSObjectService.isNullOrEmpty(filter)) {
          termFilter = _.concat(termFilter,
            SearchHelperService.createTermFilters(type, filter));
        }
        return termFilter;
      };

      var findOneById = function(id) {
        var deferred = $q.defer();
        var query = createQueryObject();
        query.id = id;
        ElasticSearchClient.getSource(query, function(error, response) {
            if (error) {
              deferred.reject(error);
            } else {
              deferred.resolve(response);
            }
          });
        return deferred;
      };

      var findByQuestionId = function(questionId, selectedAttributes, from,
        size) {
        var query = createQueryObject();
        query.body = {};
        query.body.from = from;
        query.body.size = size;
        query.body._source = selectedAttributes;
        query.body.query = {
          'bool': {
            'must': [{
              'match_all': {}
            }],
            'filter': [{
              'term': {
                'relatedQuestions.questionId': questionId
              }
            }]
          }
        };
        return ElasticSearchClient.search(query);
      };
      var findByDataSetId = function(dataSetId, selectedAttributes, from,
        size) {
        var query = createQueryObject();
        query.body = {};
        query.body.from = from;
        query.body.size = size;
        query.body._source = selectedAttributes;
        query.body.query = {
          'bool': {
            'must': [{
              'match_all': {}
            }],
            'filter': [{
              'term': {
                'dataSetId': dataSetId
              }
            }]
          }
        };
        return ElasticSearchClient.search(query);
      };
      var findByDataSetIdAndIndexInDataSet = function(dataSetId, indexInDataSet,
        selectedAttributes) {
        var query = createQueryObject();
        query.body = {};
        query.body._source = selectedAttributes;
        query.body.query = {
          'bool': {
            'must': [{
              'match_all': {}
            }],
            'filter': [{
              'term': {
                'dataSetId': dataSetId
              }
            },{
              'term': {
                'indexInDataSet': indexInDataSet
              }
            }]
          }
        };
        return ElasticSearchClient.search(query);
      };
      var countBy = function(term, value, dataSetId) {
        var query = createQueryObject();
        query.body = {};
        query.body.query = {};
        query.body.query = {
          'bool': {
            'must': [{
              'match_all': {}
            }],
            'filter': []
          }
        };
        var mustTerm = {
          'term': {}
        };
        mustTerm.term[term] = value;
        query.body.query.bool.filter.push(mustTerm);
        if (dataSetId) {
          query.body.query.bool.filter.push({
            'term': {
              'dataSetId': dataSetId
            }});
        }
        return ElasticSearchClient.count(query);
      };

      var findAccessWays = function(term, filter, dataAcquisitionProjectId,
        queryterm) {
        var query = createQueryObject();
        var termFilters = createTermFilters(filter, dataAcquisitionProjectId);
        query.size = 0;
        query.body = {
          'aggs': {
              'accessWays': {
                  'terms': {
                    'field': 'accessWays',
                    'include': '.*' + term.toLowerCase() + '.*',
                    'size': 100,
                    'order': {
                      '_key': 'asc'
                    }
                  }
                }
            }
        };

        if (termFilters) {
          query.body.query = {
            bool: {
            }
          };
          query.body.query.bool.filter = termFilters;
        }

        SearchHelperService.addQuery(query, queryterm);

        SearchHelperService.addReleaseFilter(query);

        return ElasticSearchClient.search(query).then(function(result) {
          return result.aggregations.accessWays.buckets;
        });
      };

      var findPanelIdentifiers = function(term, filter,
        dataAcquisitionProjectId, queryterm) {
        var query = createQueryObject();
        var termFilters = createTermFilters(filter, dataAcquisitionProjectId);
        query.size = 0;
        query.body = {
          'aggs': {
              'panelIdentifiers': {
                  'terms': {
                    'field': 'panelIdentifier',
                    'include': '.*' + term.toLowerCase() + '.*',
                    'size': 100,
                    'order': {
                      '_key': 'asc'
                    }
                  }
                }
            }
        };

        if (termFilters) {
          query.body.query = {
            bool: {
            }
          };
          query.body.query.bool.filter = termFilters;
        }

        SearchHelperService.addQuery(query, queryterm);

        SearchHelperService.addReleaseFilter(query);

        return ElasticSearchClient.search(query).then(function(result) {
          return result.aggregations.panelIdentifiers.buckets;
        });
      };

      var findDerivedVariablesIdentifiers = function(term, filter,
        dataAcquisitionProjectId, queryterm) {
        var query = createQueryObject();
        var termFilters = createTermFilters(filter, dataAcquisitionProjectId);
        query.size = 0;
        query.body = {
          'aggs': {
              'derivedVariablesIdentifiers': {
                  'terms': {
                    'field': 'derivedVariablesIdentifier',
                    'include': '.*' + term.toLowerCase() + '.*',
                    'size': 100,
                    'order': {
                      '_key': 'asc'
                    }
                  }
                }
            }
        };

        if (termFilters) {
          query.body.query = {
            bool: {
            }
          };
          query.body.query.bool.filter = termFilters;
        }

        SearchHelperService.addQuery(query, queryterm);

        SearchHelperService.addReleaseFilter(query);

        return ElasticSearchClient.search(query).then(function(result) {
          return result.aggregations.derivedVariablesIdentifiers.buckets;
        });
      };

      var findVariableLabels = function(searchText, filter, type,
        queryterm, dataAcquisitionProjectId) {
        var language = LanguageService.getCurrentInstantly();
        var query = createQueryObject(type);
        query.size = 0;
        query.body = {};
        var termFilters = createTermFilters(filter, dataAcquisitionProjectId,
          type);
        var prefix = (type === 'variables' || !type)  ? ''
          : 'nestedVariables.';
        var aggregation = {
            'aggs': {
              'label': {
                'filter': {
                  'bool': {
                    'must': [{
                      'match': {

                      }
                    }]
                  }
                },
                'aggs': {
                  'id': {
                    'terms': {
                      'field': prefix + 'id',
                      'size': 100
                    },
                    'aggs': {
                      'labelDe': {
                        'terms': {
                          'field': prefix + 'label.de',
                          'size': 100
                        },
                        'aggs': {
                          'labelEn': {
                            'terms': {
                              'field': prefix + 'label.en',
                              'size': 100
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          };
        var nestedAggregation = {
          'aggs': {
              'variables': {
                'nested': {
                  'path': prefix.replace('.', '')
                }
              }
            }
        };

        aggregation.aggs.label.filter.bool.must[0]
        .match[prefix + 'completeTitle.' + language] =  {
          'query': searchText,
          'operator': 'AND',
          'minimum_should_match': '100%',
          'zero_terms_query': 'ALL'
        };

        if (prefix !== '') {
          nestedAggregation.aggs.variables.aggs =
            aggregation.aggs;
          query.body.aggs = nestedAggregation.aggs;
        } else {
          query.body.aggs = aggregation.aggs;
        }

        if (termFilters) {
          query.body.query = query.body.query || {};
          query.body.query.bool = query.body.query.bool || {};
          query.body.query.bool.filter = termFilters;
        }

        SearchHelperService.addQuery(query, queryterm);

        SearchHelperService.addReleaseFilter(query);

        return ElasticSearchClient.search(query).then(function(result) {
          var labels = [];
          var labelElement = {};
          var buckets = [];
          if (prefix !== '') {
            buckets = result.aggregations.variables.label.id.buckets;
          } else {
            buckets = result.aggregations.label.id.buckets;
          }
          buckets.forEach(function(bucket) {
              labelElement = {
                label: {
                  de: bucket.labelDe.buckets[0].key,
                  en: bucket.labelDe.buckets[0].labelEn.buckets[0].key
                },
                id: bucket.key,
                count: bucket.doc_count
              };
              labels.push(labelElement);
            });
          return labels;
        });
      };

      return {
        findOneById: findOneById,
        findByQuestionId: findByQuestionId,
        findByDataSetId: findByDataSetId,
        countBy: countBy,
        findAccessWays: findAccessWays,
        findPanelIdentifiers: findPanelIdentifiers,
        findDerivedVariablesIdentifiers: findDerivedVariablesIdentifiers,
        findByDataSetIdAndIndexInDataSet: findByDataSetIdAndIndexInDataSet,
        findVariableLabels: findVariableLabels
      };
    });
