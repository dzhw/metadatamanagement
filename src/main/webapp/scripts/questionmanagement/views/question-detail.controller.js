/* global html_beautify */
/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionDetailController',

    function($scope, StudyReferencedResource,
      ShoppingCartService, VariableSearchDialogService, entity, $state,
      SimpleMessageToastService, QuestionSearchResource, CleanJSObjectService,
      RelatedPublicationSearchDialogService) {

      $scope.predecessors = [];
      $scope.successors = [];

      entity.$promise.then(function(question) {
        $scope.question = question;
        $scope.questionIdAsArray = question.id.split(',');
        QuestionSearchResource.findPredeccessors(question.id)
          .then(function(predecessors) {
            if (!CleanJSObjectService.isNullOrEmpty(predecessors)) {
              $scope.predecessors = predecessors.hits.hits;
            }
          });
        QuestionSearchResource.findSuccessors(question.successors)
          .then(function(successors) {
            if (!CleanJSObjectService.isNullOrEmpty(successors)) {
              $scope.successors = successors.docs;
            }
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
        VariableSearchDialogService
          .findByQuestionId($scope.question.id);
      };

      $scope.showStudy = function() {
        $state.go('studyDetail', {
          id: $scope.question.dataAcquisitionProjectId
        });
      };

      $scope.showRelatedPublications = function() {
        RelatedPublicationSearchDialogService
        .findByQuestionId($scope.question.id);
      };
      /* add new  item to localStorage */
      $scope.addToNotepad = function() {
        ShoppingCartService
          .addToShoppingCart($scope.question.dataAcquisitionProjectId);
      };

      $scope.openSuccessCopyToClipboardToast = function() {
        SimpleMessageToastService.openSimpleMessageToast(
          'question-management.log-messages.question.' +
          'technical-representation-success-copy-to-clipboard', []
        );
      };
    });
