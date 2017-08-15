'use strict';

angular.module('metadatamanagementApp').directive('fdzClearInput',
  function() {
        return {
            restrict: 'A',
            compile: function(element, attrs) {
                var action = attrs.ngModel + ' = \'\'';
                element.after(
                    '<md-button tabindex="-1" class="md-icon-button"' +
                    'ng-show="' + attrs.ngModel + '" ng-click="' + action +
                    '"' +
                    'style="position: absolute; top: 0px; right: -6px;' +
                    ' margin: 0px;">' +
                    '<md-icon md-font-set="material-icons" ' +
                    'style="font-size: 22px;">close</md-icon>' +
                    '</md-button>');
              }
          };
      });
