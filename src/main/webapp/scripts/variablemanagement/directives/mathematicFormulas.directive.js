/* global document */
'use strict';

angular.module('metadatamanagementApp').directive('mathFormula',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/variablemanagement/directives/' +
        'mathematicFormulas.html.tmpl',
      scope: {
        expressions: '='
      },
      link: function(scope, element) {
        var script = document.createElement('script');
        script.src = 'bower_components/MathJax/MathJax.js?config=AM_CHTML-full';
        element.prepend(script);
      },
      controller: function() {

      }
    };
  });
