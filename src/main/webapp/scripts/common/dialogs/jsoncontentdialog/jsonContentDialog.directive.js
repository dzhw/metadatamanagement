/* global document*/
/* global _*/
/* global bowser*/
'use strict';

angular.module('metadatamanagementApp')
  .directive('jsonContentDialog', function($mdDialog) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/common/dialogs/jsoncontentdialog/' +
        'jsonContentDialog.directive.html.tmpl',
      scope: {
        content: '<',
        headerTitle: '<',
        excludeFields: '<?'
      },
      link: function($scope) {

        $scope.bowser = bowser;

        function DialogController($scope, $mdDialog, content, headerTitle) {
          $scope.cancel = $mdDialog.hide;
          $scope.content = content;
          $scope.headerTitle = headerTitle;
        }

        var filteredContent;

        if (_.isArray($scope.excludeFields)) {
          filteredContent = _.omit($scope.content, $scope.excludeFields);
        } else {
          filteredContent = $scope.content;
        }

        $scope.open = function() {
          $mdDialog.show({
            controller: DialogController,
            templateUrl: 'scripts/common/dialogs/jsoncontentdialog/' +
              'jsonContentDialog.html.tmpl',
            parent: angular.element(document.body),
            locals: {content: filteredContent, headerTitle: $scope.headerTitle}
          });
        };
      }
    };
  });
