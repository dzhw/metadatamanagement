'use strict';

angular.module('metadatamanagementApp').config(
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
          var top = angular.element($document.find('#top'));
          var scrollContainer = angular.element(
            $document.find('md-content[du-scroll-container]'));
          scrollContainer.scrollToElementAnimated(top);
        });
      }
    });
  });
