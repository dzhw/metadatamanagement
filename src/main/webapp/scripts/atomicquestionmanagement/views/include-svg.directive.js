'use strict';

angular.module('metadatamanagementApp').directive('includeSvg', ['$compile',
  function($compile) {
    return {
      restrict: 'E',
      scope: {
        content: '='
      },
      link: function($scope, $element) {
        $element.replaceWith($compile($scope.content)
          ($scope.$parent));
      }
    };
  }
]);
