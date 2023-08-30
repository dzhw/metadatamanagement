/* global $, document */
'use strict';

angular.module('metadatamanagementApp')
  .controller('EditPeopleController', [
  '$scope',
  '$rootScope',
  '$timeout',
  '$element',
  'ORCIDSearchResource',
  '$mdDialog',
    function($scope, $rootScope, $timeout, $element, ORCIDSearchResource,
             $mdDialog) {
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

      $ctrl.deleteCurrentPerson = function(index, event) {
        if (document.activeElement &&
          $(document.activeElement).parents('#' +
            $ctrl.peopleId + '-' + index).length) {
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

      $ctrl.searchORCID = function(firstName, lastName, personIndex, event) {
        ORCIDSearchResource.get({
          firstName: firstName ? firstName : '*',
          lastName: lastName ? lastName : '*',
        }).$promise.then(function(response) {
          $mdDialog.show({
            controller: 'ChooseORCIDController',
            templateUrl: 'scripts/common/people/' +
              'choose-orcid.html.tmpl',
            clickOutsideToClose: false,
            fullscreen: true,
            locals: {
              firstName: firstName,
              lastName: lastName,
              orcidResponse: response
            },
            targetEvent: event
          }).then(function(selection) {
            if (selection.orcid) {
              $ctrl.people[personIndex].orcid = selection.orcid;
              $ctrl.currentForm.$setDirty();
            }
          });
        });
      };

      $ctrl.deleteORCID = function(personIndex) {
        delete $ctrl.people[personIndex].orcid;
        $ctrl.currentForm.$setDirty();
      };
    }]);

