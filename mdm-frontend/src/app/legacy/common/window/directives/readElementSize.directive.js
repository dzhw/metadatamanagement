/* global Event */
'use strict';
/* Author: Daniel Katzberg*/
/* This directive read the width and height of an element an set this to
  global variables. */

angular
  .module('metadatamanagementApp')
  .directive('readElementSize', function($timeout, $window) {

    function link($scope, element) {
      var container = element[0];

      $scope.$watchGroup([
        function() {
          return container.offsetWidth;
        },
        function() {
          return container.offsetHeight;
        }
      ], function(values) {
        // values[0] = width, values[1] = height
        $scope.windowWidth = values[0];
        $scope.windowHeight = values[1];
        $timeout(function() {
          // send resize event for jkcarousel
          $window.dispatchEvent(new Event('resize'));
        });
      });
    }

    return {
      link: link,
      restrict: 'A'
    };
  });
