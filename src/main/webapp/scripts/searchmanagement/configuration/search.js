'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('search', {
      parent: 'site',
      url: '/search?{page}{size}{type}{data-set}' +
      '{instrument}{panel-identifier}{derived-variables-identifier}{survey}' +
      '{data-package}{variable}{question}{related-publication}{access-way}' +
      '{query}{study-series-de}{study-series-en}{concept}{study-series}{tags}',
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
