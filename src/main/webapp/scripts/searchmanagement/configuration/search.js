'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('search', {
      parent: 'site',
      url: '/search?{access-way}{concept}{data-package}{data-set}' +
        '{derived-variables-identifier}{instrument}{page}' +
        '{query}{question}{related-publication}' +
        '{repeated-measurement-identifier}{size}{study-series}' +
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
