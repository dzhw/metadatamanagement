/* global Hyphenator */
'use strict';

angular.module('metadatamanagementApp').directive('fdzHyphenate',
    function($timeout) {
        var link = function(scope, element) { // jshint unused:true
            // we have to wait until other directives have done its job
            // like displayi18nString
            $timeout(function() {
              element.addClass('fdz-hyphenate');
              Hyphenator.hyphenate(element[0], element[0].lang);
            });
          };
        return {
          restrict: 'A',
          link: link
        };
      });
