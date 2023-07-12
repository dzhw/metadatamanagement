/* global _*/
'use strict';

angular.module('metadatamanagementApp').directive('fdzTable',
  function($rootScope, $timeout) {
    var setLastRowStyle = function(element) {
      $timeout(function() {
        var rows = element[0].
        getElementsByClassName('fdz-table-last-row');
        if (element.hasClass('fdz-table-wide')) {
          _.forEach(rows, function(value) {
            value.style.borderBottom = '3px solid #ddd';
          });
        } else {
          _.forEach(rows, function(value) {
            value.style.borderRight = '3px solid #ddd !important';
          });
        }
      });
    };
    return {
        scope: true,
        link: function(scope, element) {
          if ($rootScope.bowser.mobile) {
            element.addClass('fdz-table-rotated');
            setLastRowStyle(element);
          }else {
            element.addClass('fdz-table-wide');
            setLastRowStyle(element);
          }
          scope.$watchCollection(function() {
            return element.width();
          }, function() {
            if (!$rootScope.bowser.mobile) {
              $timeout(function() {
                if (element.hasClass('fdz-table-rotated')) {
                  element.removeClass('fdz-table-rotated');
                  element.addClass('fdz-table-wide');
                }
                if ((element.width()) > (element.parent().width())) {
                  element.removeClass('fdz-table-wide');
                  element.addClass('fdz-table-rotated');
                }
                setLastRowStyle(element);
              }, 1000);
            }
          });
        }
      };
  });
