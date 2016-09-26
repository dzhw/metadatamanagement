/* global html_beautify */
/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionDetailController',

    function($scope, $rootScope, localStorageService,
      ShoppingCartService, $stateParams, DialogService,
      QuestionReferencedResource, blockUI, entity, $q, $state,
      StudyReferencedResource) {

      $scope.predecessors = [];
      $scope.successors = [];

      /* function to load only texts and ids for successors and predecessors */
      var loadSuccessorsQuestionTextOnly = function(items) {
        var deferred = $q.defer();
        var itemsAsString = '"' + items + '"';
        itemsAsString = itemsAsString.replace(/[\[\]'"]/g, '');
        QuestionReferencedResource.findByIdIn({
            ids: itemsAsString
          })
          .$promise.then(function(successors) {
            deferred.resolve(successors);
          });
        return deferred.promise;
      };
      /* function to load only texts and ids for successors and predecessors */
      var loadPredecessorsQuestionTextOnly = function(id) {
        var deferred = $q.defer();
        QuestionReferencedResource.findBySuccessorsContaining({
            id: id
          })
          .$promise.then(function(predecessors) {
            deferred.resolve(predecessors);
          });
        return deferred.promise;
      };

      var checkInvalidQuestionIds = function(allIds, customItems) {
        var tempQuestions;
        try {
          tempQuestions = [];
          allIds.forEach(function(id) {
            for (var i = 0; i < customItems.length; i++) {
              if (customItems[i].id === id) {
                tempQuestions.push(customItems[i]);
                break;
              } else {
                if (i === (customItems.length - 1)) {
                  var notFoundQuestion = {
                    id: id,
                    name: 'notFoundQuestion',
                    number: '-',
                    questionText: 'not-found'
                  };
                  tempQuestions.push(notFoundQuestion);
                }
              }
            }
          });
        } catch (e) {
          tempQuestions = [];
        }
        return tempQuestions;
      };
      entity.$promise.then(function(question) {
        $scope.question = question;

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
        loadSuccessorsQuestionTextOnly($scope.question.successors)
          .then(function(customSuccesors) {
            $scope.successors = checkInvalidQuestionIds(
              $scope.question.successors, customSuccesors._embedded.questions
            );
          });
        loadPredecessorsQuestionTextOnly($scope.question.id)
          .then(function(predecessors) {
            $scope.predecessors = predecessors._embedded.questions;
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
