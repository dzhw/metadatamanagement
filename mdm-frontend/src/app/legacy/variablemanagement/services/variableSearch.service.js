/* global  _*/
'use strict';

angular.module('metadatamanagementApp').factory('VariableSearchService', ['ElasticSearchClient', '$q', 'SearchHelperService', 'CleanJSObjectService', 'LanguageService', 'Principal', 
  function(ElasticSearchClient, $q, SearchHelperService,
    CleanJSObjectService, LanguageService, Principal) {
      var createQueryObject = function(type) {
        type = type || 'variables';
        return {
          index: type
        };
      };

      var createNestedTermFilters = function(filter, prefix) {
        var result = [];
        if (!prefix || prefix === '' ||
            CleanJSObjectService.isNullOrEmpty(filter)) {
          return result;
        }
        if (filter['data-set']) {
          result.push({
            term: {
              'nestedVariables.dataSetId': filter['data-set']
            }
          });
        }
        if (filter.survey) {
          result.push({
            term: {
              'nestedVariables.surveyIds': filter.survey
            }
          });
        }
        return result;
      };

      var createTermFilters = function(filter, dataAcquisitionProjectId, type) {
        type = type || 'variables';
        var termFilter;
        if (!CleanJSObjectService.isNullOrEmpty(filter) ||
          !CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
          termFilter = [];
        }
        if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId) &&
          !_.includes(['related_publications', 'concepts'], type)) {
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

      var findOneById = function(id, attributes, excludes) {
        var deferred = $q.defer();
        var query = createQueryObject();
        query.id = id;
        if (attributes) {
          query._source = attributes;
        }
        if (excludes) {
          query._source_excludes = excludes.join(',');
        }
        ElasticSearchClient.getSource(query, function(error, response) {
            if (error) {
              deferred.reject(error);
            } else {
              deferred.resolve(response);
            }
          });
        return deferred;
      };

      var findShadowByIdAndVersion = function(id, version, excludes) {
        var query = {};
        _.extend(query, createQueryObject(),
          SearchHelperService.createShadowByIdAndVersionQuery(id, version));
        if (excludes) {
          query.body._source = {
            'excludes': excludes
          };
        }
        var deferred = $q.defer();
        ElasticSearchClient.search(query).then(function(result) {
          if (result.hits.hits.length === 1) {
            deferred.resolve(result.hits.hits[0]._source);
          } else {
            return deferred.resolve(null);
          }
        }, deferred.reject);
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
      var countBy = function(term, value, dataSetId, version) {
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
        if (term) {
          var mustTerm = {
            'term': {}
          };
          mustTerm.term[term] = value;
          query.body.query.bool.filter.push(mustTerm);
        }
        if (dataSetId) {
          query.body.query.bool.filter.push({
            'term': {
              'dataSetId': dataSetId
            }});
        }

        var isAnonymous = Principal.loginName() === null;

        query.body.query.bool.filter.push({term: {
          shadow: isAnonymous
        }});

        if (isAnonymous) {
          if (version) {
            query.body.query.bool.filter.push({
              term: {
                'release.version': version
              }
            });
          } else {
            query.body.query.bool.must_not = [{
              exists: {
                field: 'successorId'
              }
            }];
          }
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
        } else {
          _.set(query, 'body.query.bool.filter', []);
        }

        if (Principal.loginName()) {
          query.body.query.bool.filter.push({
            'term': {
              'shadow': false
            }
          });
        } else {
          query.body.query.bool.filter.push({
            'term': {
              'shadow': true
            }
          });
          if (_.isEmpty(termFilters)) {
            _.set(query, 'body.query.bool.must_not[0].exists.field',
              'successorId');
          }
        }

        SearchHelperService.addQuery(query, queryterm);
        SearchHelperService.addFilter(query);

        return ElasticSearchClient.search(query).then(function(result) {
          return result.aggregations.accessWays.buckets;
        });
      };

      var findRepeatedMeasurementIdentifiers = function(term, filter,
        dataAcquisitionProjectId, queryterm) {
        var query = createQueryObject();
        var termFilters = createTermFilters(filter, dataAcquisitionProjectId);
        query.size = 0;
        query.body = {
          'aggs': {
              'repeatedMeasurementIdentifiers': {
                  'terms': {
                    'field': 'repeatedMeasurementIdentifier',
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
        SearchHelperService.addFilter(query);
        SearchHelperService.addShadowCopyFilter(query, filter);

        return ElasticSearchClient.search(query).then(function(result) {
          return result.aggregations.repeatedMeasurementIdentifiers.buckets;
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
        SearchHelperService.addFilter(query);
        SearchHelperService.addShadowCopyFilter(query, filter);

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
                      'masterId': {
                        'terms': {
                          'field': prefix + 'masterId',
                          'size': 100
                        }
                      },
                      'labelDe': {
                        'terms': {
                          'field': prefix + 'label.de',
                          'size': 100
                        }
                      },
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
          'query': searchText || '',
          'operator': 'AND',
          'minimum_should_match': '100%',
          'zero_terms_query': 'ALL'
        };

        if (termFilters) {
          query.body.query = query.body.query || {};
          query.body.query.bool = query.body.query.bool || {};
          query.body.query.bool.filter = termFilters;

          aggregation.aggs.label.filter.bool.must = _.concat(
            aggregation.aggs.label.filter.bool.must,
            createNestedTermFilters(filter, prefix));
        }

        SearchHelperService.addNestedShadowCopyFilter(
          aggregation.aggs.label.filter.bool, prefix, type);

        if (prefix !== '') {
          nestedAggregation.aggs.variables.aggs =
            aggregation.aggs;
          query.body.aggs = nestedAggregation.aggs;
        } else {
          query.body.aggs = aggregation.aggs;
        }

        SearchHelperService.addQuery(query, queryterm);
        SearchHelperService.addFilter(query);
        if (!_.includes(['related_publications', 'concepts'], type)) {
          SearchHelperService.addShadowCopyFilter(query, filter);
        }

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
                  de: bucket.labelDe.buckets[0] ?
                    bucket.labelDe.buckets[0].key : '',
                  en: bucket.labelEn.buckets[0] ?
                    bucket.labelEn.buckets[0].key : ''
                },
                id: bucket.key,
                masterId: bucket.masterId.buckets[0].key,
                count: bucket.doc_count
              };
              labels.push(labelElement);
            });
          return labels;
        });
      };

      var countByMultiple = function(map) {
        var query =  createQueryObject();
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
        _.forEach(map, function(value, key) {
          var mustTerm = {
            'term': {}
          };
          mustTerm.term[key] = value;
          query.body.query.bool.filter.push(mustTerm);
        });
        return ElasticSearchClient.count(query);
      };

      return {
        findOneById: findOneById,
        findByQuestionId: findByQuestionId,
        findByDataSetId: findByDataSetId,
        findShadowByIdAndVersion: findShadowByIdAndVersion,
        countBy: countBy,
        countByMultiple: countByMultiple,
        findAccessWays: findAccessWays,
        findRepeatedMeasurementIdentifiers: findRepeatedMeasurementIdentifiers,
        findDerivedVariablesIdentifiers: findDerivedVariablesIdentifiers,
        findByDataSetIdAndIndexInDataSet: findByDataSetIdAndIndexInDataSet,
        findVariableLabels: findVariableLabels
      };
    }]);

