'use strict';

angular.module('metadatamanagementApp').directive('fdzOnload',
    function($parse) {
      return {
        restrict: 'A',
        link: function(scope, element, attributes) {
          var fn = attributes.fdzOnload && $parse(attributes.fdzOnload);
          element.on('load', function(ev) {
            var src = this.src;
            if (fn) {
              scope.$apply(function() {
                fn(scope, {$event: ev, $src: src});
              });
            }
          });
        }
      };
    });
