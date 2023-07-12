'use strict';

angular.module('metadatamanagementApp').directive('fdzSelectSearchInput',
function() {
  return {
    restrict: 'A',
    controllerAs: '$selectSearch',
    bindToController: {},
    controller: function controller($element, $scope) {
      this.$postLink = function $postLink() {
        var menu = $element.closest('.md-select-menu-container');
        $scope.$watch(function focusSearchCheck() {
          return menu.hasClass('md-active') ? menu[0].id : undefined;
        }, function focusSearch(activeMenuId) {
          if (activeMenuId) {
            $element.focus();
          }
        });

        var topOption = null;
        var upEventListener = function onOptionNavigate(event) {
          var KEYCODE_UP = 38;
          if (event.keyCode === KEYCODE_UP) {
            var target = angular.element(event.target);

            var prevIsHeader = target.prev().is('md-select-header');

            if (!prevIsHeader) {
              prevIsHeader = target.prev()[0] == null &&
                target.parent().is('md-content') &&
                target.parent().prev().is('md-select-header');
            }
            if (prevIsHeader) {
              $element.focus();
              event.stopPropagation();
            }
          }
        };

        $scope.$watchCollection(function() {
          return menu.find('md-option')
           .toArray()
            .map(function(option) {
              return option.id;
            });
        }, function(ids) {
          if (ids.length > 0) {
            var jTop = menu.find('md-option').eq(0);
            var top = jTop[0];
            if (topOption && topOption !== top) {
              angular.element(topOption).off('keydown', upEventListener);
            }
            topOption = top;
            jTop.on('keydown', upEventListener);
          }
        });
      };
    }
  };
});
