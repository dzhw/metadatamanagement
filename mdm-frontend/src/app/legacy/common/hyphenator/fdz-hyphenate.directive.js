/* global Hyphenator, _ */
'use strict';

angular.module('metadatamanagementApp').directive('fdzHyphenate', ['$timeout', 
    function($timeout) {
        var link = function(scope, element) { // jshint unused:true
            // we have to wait until other directives have done its job
            // like displayi18nString
            $timeout(function() {
              if (element[0].lang && element[0].lang !== '' &&
                 !(_.startsWith(element[0].lang, '{{') &&
                    _.endsWith(element[0].lang, '}}'))) {
                element.addClass('fdz-hyphenate');
                Hyphenator.hyphenate(element[0], element[0].lang);
              }
            });
          };
        return {
          restrict: 'A',
          link: link
        };
      }]);

