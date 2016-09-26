'use strict';

angular.module('metadatamanagementApp').factory('DataSetSearchResource',
function(Language, ElasticSearchClient) {
  var query = {};
  query.index = 'metadata_' + Language.getCurrentInstantly();
  query.type = 'data_sets';
  query.body = {};

  var findDataSetsByVariableId = function(variableId) {
    console.log(variableId);
    query.filterPath = '';
    query.body.query = {
      'bool': {
        'must': [
          {
            'match_all': {}
          }
        ],
        'filter': [
          {
            'term': {
              'variableIds': variableId
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  return {
    findDataSetsByVariableId: findDataSetsByVariableId
  };
});
