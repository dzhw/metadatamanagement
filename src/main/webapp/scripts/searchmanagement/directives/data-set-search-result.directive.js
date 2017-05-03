/* global _*/
'use strict';

angular.module('metadatamanagementApp').directive('datasetSearchResult',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'data-set-search-result.html.tmpl',
      link: function(scope) {
        var dataSet = {
          'id': scope.searchResult.id,
          'number': scope.searchResult.number,
          'description': scope.searchResult.description,
          'type': scope.searchResult.type,
          'surveys': scope.searchResult.surveys,
          'accessWays': _.map(scope.searchResult.subDataSets,
            'accessWay').join(', '),
          'maxOfNumberOfObservations': _.maxBy(scope.searchResult.subDataSets,
            function(subDataSet) {
              return subDataSet.numberOfObservations;})
              .numberOfObservations
        };
        scope.searchResult = dataSet;
      },
      scope: {
        searchResult: '=',
        currentLanguage: '=',
        bowser: '=',
        addMargin: '='
      }
    };
  });
