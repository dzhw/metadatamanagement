/* global $, document */
'use strict';

angular.module('metadatamanagementApp')
  .controller('EditPeopleController',
    function($scope, $rootScope, $timeout, $element) {
      var $ctrl = this;
      $scope.bowser = $rootScope.bowser;
      $ctrl.$onInit = function() {
        $ctrl.people = $ctrl.people || [];
        if ($ctrl.people.length === 0) {
          $ctrl.people.push({
            firstName: '',
            lastName: ''
          });
        }
        $scope.form = $ctrl.currentForm;
      };

      $ctrl.deletePerson = function(index) {
        $ctrl.people.splice(index, 1);
        $ctrl.currentForm.$setDirty();
      };

      $ctrl.addPerson = function() {
        $ctrl.people.push({
          firstName: '',
          lastName: ''
        });
        $timeout(function() {
          $element.find('input[name="' + $ctrl.peopleId + 'FirstName_' +
              ($ctrl.people.length - 1) + '"]')
            .focus();
        });
      };

      $ctrl.setCurrentPerson = function(index, event) {
        $ctrl.currentPersonInputName = event.target.name;
        $ctrl.currentPersonIndex = index;
      };

      var timeoutActive = null;
      $ctrl.deleteCurrentPerson = function(event) {
        if (timeoutActive) {
          $timeout.cancel(timeoutActive);
        }
        timeoutActive = $timeout(function() {
          timeoutActive = false;
          // msie workaround: inputs unfocus on button mousedown
          if (document.activeElement &&
            $(document.activeElement).parents('#move-' +
              $ctrl.peopleId + '-container').length) {
            return;
          }
          if (event.relatedTarget && (
              event.relatedTarget.id === 'move-' +
                $ctrl.peopleId + '-up-button' ||
              event.relatedTarget.id === 'move-' +
                $ctrl.peopleId + '-down-button')) {
            return;
          }
          delete $ctrl.currentPersonIndex;
          timeoutActive = null;
        }, 500);
      };

      $ctrl.moveCurrentPersonUp = function() {
        var a = $ctrl.people[$ctrl.currentPersonIndex - 1];
        $ctrl.people[$ctrl.currentPersonIndex - 1] =
          $ctrl.people[$ctrl.currentPersonIndex];
        $ctrl.people[$ctrl.currentPersonIndex] = a;
        $ctrl.currentPersonInputName = $ctrl.currentPersonInputName
          .replace('_' + $ctrl.currentPersonIndex,
            '_' + ($ctrl.currentPersonIndex - 1));
        $element.find('input[name="' +
          $ctrl.currentPersonInputName + '"]')
          .focus();
        $ctrl.currentForm.$setDirty();
      };

      $ctrl.moveCurrentPersonDown = function() {
        var a = $ctrl.people[$ctrl.currentPersonIndex + 1];
        $ctrl.people[$ctrl.currentPersonIndex + 1] =
          $ctrl.people[$ctrl.currentPersonIndex];
        $ctrl.people[$ctrl.currentPersonIndex] = a;
        $ctrl.currentPersonInputName = $ctrl.currentPersonInputName
          .replace('_' + $ctrl.currentPersonIndex,
            '_' + ($ctrl.currentPersonIndex + 1));
        $element.find('input[name="' +
          $ctrl.currentPersonInputName + '"]')
          .focus();
        $ctrl.currentForm.$setDirty();
      };
    }
  );
