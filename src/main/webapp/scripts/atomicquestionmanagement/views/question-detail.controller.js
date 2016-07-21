/*global document */
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionDetailController', ['$scope', 'entity', '$mdDialog',
  '$mdMedia',
    function($scope, entity, $mdDialog, $mdMedia) {
      $scope.question = entity;
      $scope.status = '  ';
      $scope.customFullscreen = $mdMedia('xs') || $mdMedia('sm');
      $scope.showQuestion = function(ev) {
        /*$mdDialog.show(
        $mdDialog.alert()
        .parent(angular.element(document.querySelector('#popupContainer')))
        .clickOutsideToClose(true)
        .title('This is an alert title')
        .textContent('You can specify some description text in here.')
        .ariaLabel('Alert Dialog Demo')
        .ok('schließen')
        .targetEvent(ev)
      );*/
        var parentEl = angular
        .element(document.querySelector('#popupContainer'));
        $mdDialog.show({
          parent: parentEl,
          targetEvent: ev,
          clickOutsideToClose: true,
          template: '<md-dialog aria-label="List dialog">' +
          '  <md-dialog-content>' +
          '          <img src="/assets/images/se19-01.png"' +
          ' alt="image caption"/>' +
          '  </md-dialog-content>' +
          '  <md-dialog-actions>' +
          '    <md-button ng-click="closeDialog()" class="md-primary">' +
          '      Schließen' +
          '    </md-button>' +
          '  </md-dialog-actions>' +
          '</md-dialog>',
          controller: function($scope, $mdDialog) {
            $scope.closeDialog = function() {
              $mdDialog.hide();
            };
          }
        });
      };
    }
  ]);
