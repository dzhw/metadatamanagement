'use strict';

angular.module('metadatamanagementApp').config([
  '$stateProvider',

  function($stateProvider) {
    $stateProvider.state('search', {
      parent: 'site',
      url: '/search?{access-way}{access-ways}{concept}{concepts}' +
        '{data-package}' +
        '{data-set}{derived-variables-identifier}{instrument}{institution-de}' +
        '{institutions}{institution-en}{page}' +
        '{query}{question}{related-publication}' +
        '{repeated-measurement-identifier}{size}{sponsors}{sponsor-de}' +
        '{sponsor-en}{study-series}{survey-data-types}' +
        '{study-series-de}{study-series-en}{survey}{survey-method-de}' +
        '{survey-method-en}{tags}{type}{variable}',
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
  }]);
