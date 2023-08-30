'use strict';

angular.module('metadatamanagementApp').config([
  '$stateProvider',

  function($stateProvider) {
    $stateProvider.state('disclosure', {
      parent: 'site',
      url: '/disclosure',
      reloadOnSearch: false,
      data: {
        authorities: []
      },
      views: {
        'content@': {
          templateUrl: 'scripts/disclosure/views/' +
            'disclosure.html.tmpl',
          controller: 'DisclosureController'
        }
      },
      onEnter: function($document, $timeout) {
        $timeout(function() {
          var top = $document.find('#top')[0];
          top.scrollIntoView();
        });
      }
    });
  }]);
