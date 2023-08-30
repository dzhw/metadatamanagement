'use strict';

angular.module('metadatamanagementApp').directive(
  'hasAnyAuthority', [
    'Principal',
    function(Principal) {
      return {
        restrict: 'A',
        link: function(scope, element, attrs) { // jshint ignore:line
          var setVisible = function() {
            element.removeClass('hidden');
          };
          var setHidden = function() {
            element.addClass('hidden');
          };
          var authorities = attrs.hasAnyAuthority
            .replace(/\s+/g, '').split(',');
          var defineVisibility = function(reset) {
            var result;
            if (reset) {
              setVisible();
            }

            result = Principal.hasAnyAuthority(authorities);
            if (result) {
              setVisible();
            } else {
              setHidden();
            }
          };

          if (authorities.length > 0) {
            defineVisibility(true);
          }
        }
      };
    }
  ]).directive('hasAuthority', ['Principal', function(Principal) {
  return {
    restrict: 'A',
    link: function(scope, element, attrs) { // jshint ignore:line
      var setVisible = function() {
        element.removeClass('hidden');
      };
      var setHidden = function() {
        element.addClass('hidden');
      };
      var authority = attrs.hasAuthority.replace(/\s+/g, '');

      var defineVisibility = function(reset) {

        if (reset) {
          setVisible();
        }

        if (Principal.hasAuthority(authority)) {
          setVisible();
        } else {
          setHidden();
        }
      };

      if (authority.length > 0) {
        defineVisibility(true);
      }
    }
  };
}]);
