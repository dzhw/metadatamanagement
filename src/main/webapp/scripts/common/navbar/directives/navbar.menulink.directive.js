'use strict';

angular.module('metadatamanagementApp')
  .run(['$templateCache', function($templateCache) {
    $templateCache.put('partials/menu-link.tmpl.html',
      '<md-button class="md-button md-default-theme {{section.icon}}"' +
      ' ui-sref-active="active" \n' +
      '  ui-sref="{{section.state}}" ng-click="focusSection()">\n' +
      '  {{section.name | translate}}\n' +
      '  <span  class="md-visually-hidden ng-if="isSelected()">\n' +
      '    current page\n' +
      '  </span>\n' +
      '</md-button>\n' +
      '');
  }])
  .directive('menuLink', function() {
    return {
      scope: {
        section: '='
      },
      templateUrl: 'partials/menu-link.tmpl.html',
      link: function($scope, $element) {
        var controller = $element.parent().controller();

        $scope.focusSection = function() {
          // set flag to be used later when
          // $locationChangeSuccess calls openPage()
          controller.autoFocusContent = true;
        };
      }
    };
  });
