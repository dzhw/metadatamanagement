/* global html_beautify */
/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionDetailController',

    function($scope, StudyReferencedResource,
      VariableSearchDialogService, entity, $state,
      SimpleMessageToastService, QuestionSearchResource, CleanJSObjectService,
      RelatedPublicationSearchDialogService, VariableSearchResource,
      RelatedPublicationSearchResource) {

      $scope.predecessors = [];
      $scope.successors = [];
      $scope.counts = {};

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
        VariableSearchResource.getCounts('questionId',
            $scope.question.id).then(function(variablesCount) {
                $scope.counts.variablesCount = variablesCount.count;
              });
        RelatedPublicationSearchResource.getCounts('questionIds',
            $scope.question.id).then(function(publicationsCount) {
                $scope.counts.publicationsCount = publicationsCount.count;
              });
      });

      /* function to open dialog for variables */
      $scope.showVariables = function() {
        VariableSearchDialogService.findVariables('findByQuestionId',
        $scope.question.id, $scope.counts.variablesCount);
      };

      $scope.showStudy = function() {
        $state.go('studyDetail', {
          id: $scope.question.dataAcquisitionProjectId
        });
      };

      $scope.showRelatedPublications = function() {
        RelatedPublicationSearchDialogService.
        findRelatedPublications('findByQuestionId', $scope.question.id,
        $scope.counts.publicationsCount);
      };

      $scope.openSuccessCopyToClipboardToast = function() {
        SimpleMessageToastService.openSimpleMessageToast(
          'question-management.log-messages.question.' +
          'technical-representation-success-copy-to-clipboard', []
        );
      };
    });
