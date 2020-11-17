'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('search', {
      parent: 'site',
      url: '/search?{access-way}{concept}{data-package}{data-set}' +
        '{derived-variables-identifier}{instrument}{page}{panel-identifier}' +
        '{query}{question}{related-publication}{size}{study-series}' +
        '{study-series-de}{study-series-en}{survey}{tags}{type}{variable}',
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
