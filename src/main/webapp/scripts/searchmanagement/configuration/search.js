'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('search', {
      parent: 'site',
      url: '/search?{page}{type}{sort-by}{data-set}' +
      '{instrument}{panel-identifier}{survey}{study}{not}',
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
