'use strict';

angular.module('metadatamanagementApp')
  .run(['$templateCache', function($templateCache) {
    $templateCache.put('partials/menu-toggle.tmpl.html',
      '<md-button class="md-button-toggle {{section.icon}}"\n' +
      '  ng-click="toggle()"\n' +
      '  aria-controls="docs-menu-{{section.name | nospace}}"\n' +
      '  flex layout="row"\n' +
      '  aria-expanded="{{isOpen()}}">\n' +
      '  {{section.name | translate}}\n' +
      '  <span aria-hidden="true" \n' +
      ' class="pull-right fa fa-chevron-down md-toggle-icon"\n' +
      '  ng-class="{\'toggled\' : isOpen()}"></span>\n' +
      '</md-button>\n' +
      '<ul ng-show="isOpen()" id="docs-menu-{{section.name | nospace}}"' +
      ' class="menu-toggle-list">\n' +
      '  <li ng-repeat="page in section.pages">\n' +
      '    <menu-link section="page"></menu-link>\n' +
      '  </li>\n' +
      '</ul>\n' +
      '');
  }])
  .directive('menuToggle', ['$timeout', function() {
    return {
      scope: {
        section: '='
      },
      templateUrl: 'partials/menu-toggle.tmpl.html',
      link: function(scope, element) {
        var controller = element.parent().controller();

        scope.isOpen = function() {
          return controller.isOpen(scope.section);
        };
        scope.toggle = function() {
          controller.toggleOpen(scope.section);
        };

        var parentNode = element[0].parentNode.parentNode.parentNode;
        if (parentNode.classList.contains('parent-list-item')) {
          var heading = parentNode.querySelector('h2');
          element[0].firstChild.setAttribute('aria-describedby', heading.id);
        }
      }
    };
  }]);
