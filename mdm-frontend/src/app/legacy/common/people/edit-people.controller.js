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

      var initInstitutionRors = function(person) {
        person.institutions = person.institutions || [];
        person.institutionRors = person.institutions
          .map(function(institution) {
            return institution && institution.ror;
          })
          .filter(function(ror) {
            return !!ror;
          });
      };

      $ctrl.mapInstitutionRorsToInstitutions = function(person) {
        person.institutions = person.institutions || [];
        var selectedRors = person.institutionRors || [];
        var selectedRorSet = {};
        selectedRors.forEach(function(ror) {
          selectedRorSet[ror] = true;
        });
        person.institutions = ($ctrl.institutions || [])
          .filter(function(institution) {
            return !!institution.ror && !!selectedRorSet[institution.ror];
          });
        $ctrl.currentForm.$setDirty();
      };

      $ctrl.$onInit = function() {
        $ctrl.people = $ctrl.people || [];
        if ($ctrl.people.length === 0) {
          $ctrl.people.push({
            firstName: '',
            lastName: '',
            institutions: []
          });
        }
        $ctrl.people.forEach(function(person) {
          initInstitutionRors(person);
        });
        $scope.form = $ctrl.currentForm;
        $scope.isQuestionnaire = $ctrl.attachmentMetadataType === "Questionnaire"
          || $ctrl.attachmentMetadataType === "Variable Questionnaire";
      };

      $ctrl.$onChanges = function(changesObj) {
        if (changesObj.institutions && $ctrl.people && $ctrl.people.length) {
          $ctrl.people.forEach(function(person) {
            initInstitutionRors(person);
          });
        }
      };

      $ctrl.deletePerson = function(index) {
        $ctrl.people.splice(index, 1);
        $ctrl.currentForm.$setDirty();
      };

      $ctrl.addPerson = function() {
        $ctrl.people.push({
          firstName: '',
          lastName: '',
          institutions: []
        });
        initInstitutionRors($ctrl.people[$ctrl.people.length - 1]);
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
            multiple: true,
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

      // Show error for questionnaire mandatory fields firstName and lastName,
      // whenever user entered input for some fields (firstName or lastName or
      // middleName or orcid) and either firstName or lastName is null.
      $scope.questionnaireFieldsInvalid = function(person) {
        return (!!person.firstName && !person.lastName) ||
          (!person.firstName && !!person.lastName) ||
          !!person.middleName ||
          !!person.orcid
      }
      
    }]);

