'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('search', {
      parent: 'site',
      url: '/search?{page}{type}{data-set}' +
      '{instrument}{panel-identifier}{derived-variables-identifier}{survey}' +
      '{study}{variable}{question}{related-publication}{access-way}{query}' +
      '{survey-series-de}{survey-series-en}',
      reloadOnSearch: false,
      data: {
        authorities: []
      },
      views: {
        'content@': {
          templateUrl: 'scripts/searchmanagement/views/search.html.tmpl',
          controller: 'SearchController'
        }
      }
    });
  });
