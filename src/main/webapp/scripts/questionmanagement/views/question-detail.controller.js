/* global html_beautify */
/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionDetailController',

    function($scope, $rootScope, localStorageService,
      ShoppingCartService, $stateParams, DialogService,
      QuestionReferencedResource, blockUI, entity, $q, $state,
      StudyReferencedResource, QuestionSearchResource) {

      $scope.predecessors = [];
      $scope.successors = [];

      entity.$promise.then(function(question) {
        $scope.question = question;
        console.log(question);
        QuestionSearchResource.findPredeccessors(question.id)
        .then(function(predecessors) {
          $scope.predecessors = predecessors.hits.hits;
        });
        QuestionSearchResource.findSuccessors(question.successors)
        .then(function(successors) {
          $scope.successors = successors.docs;
        });
        if ($scope.question.technicalRepresentation) {
          //default value is no beautify
          $scope.technicalRepresentationBeauty =
          $scope.question.technicalRepresentation.source;

          //beautify xml, html, xhtml files.
          if ($scope.question.technicalRepresentation.language === 'xml' ||
          $scope.question.technicalRepresentation.language === 'xhtml' ||
          $scope.question.technicalRepresentation.language === 'html') {
            $scope.technicalRepresentationBeauty =
            html_beautify($scope.question.technicalRepresentation.source); //jscs:ignore
          }
        }

        StudyReferencedResource
          .getReferencedStudy({
            id: $scope.question.dataAcquisitionProjectId
          })
          .$promise.then(function(study) {
            $scope.study = study;
          });
      });

      /* function to open dialog for variables */
      $scope.showVariables = function() {
        blockUI.start();
        DialogService.showDialog($scope.question.id, 'variable');
      };

      $scope.showStudy = function() {
        $state.go('studyDetail', {
          id: $scope.question.dataAcquisitionProjectId
        });
      };
      /* add new  item to localStorage */
      $scope.addToNotepad = function() {
        ShoppingCartService
          .addToShoppingCart($scope.question.dataAcquisitionProjectId);
      };
    });
