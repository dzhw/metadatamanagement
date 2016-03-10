'use strict';

angular.module('metadatamanagementApp')
    .controller('WaitController', function($scope, $timeout, $element) {
      var showElement;
      var hideElement;
      hideElement = function() {
        $element.css('display', 'none');
        $timeout(showElement, 1000);
      };
      showElement = function() {
        $element.css('display', 'inline');
        $timeout(hideElement, 1000);
      };
      showElement();
    });
