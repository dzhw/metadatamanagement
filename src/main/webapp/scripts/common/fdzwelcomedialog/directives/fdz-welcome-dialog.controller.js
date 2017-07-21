/* global window */
/* Author Daniel Katzberg */
'use strict';
angular.module('metadatamanagementApp').controller('FdzWelcomeDialogController',
  function($scope, $mdDialog, localStorageService, bowser, $location,
      $translate) {
    $scope.bowser = bowser;
    $scope.closeWelcomeDialogForever = false;

    $scope.currentUrl = function() {

      var domainManagement = 'crosscutting';
      var encodedUrl = window.encodeURIComponent($location.absUrl());
      if (encodedUrl.includes('studies')) {
        domainManagement = 'studymanagement';
      }
      if (encodedUrl.includes('surveys')) {
        domainManagement = 'surveymanagement';
      }
      if (encodedUrl.includes('instruments')) {
        domainManagement = 'instrumentmanagement';
      }
      if (encodedUrl.includes('questions')) {
        domainManagement = 'questionmanagement';
      }
      if (encodedUrl.includes('data-sets') ||
        encodedUrl.includes('data_sets')) {
        domainManagement = 'datasetmanagement';
      }
      if (encodedUrl.includes('variables')) {
        domainManagement = 'variablemanagement';
      }
      if (encodedUrl.includes('publications')) {
        domainManagement = 'relatedpublicationmanagement';
      }

      var sourceLang = $translate
        .instant('global.navbar-feedback.source');
      var feedbackUrl = 'https://github.com/dzhw/' +
      'metadatamanagement/issues/new?' +
      'body=%0A---------------------------------%0A' + sourceLang + '%3A%0A' +
      encodedUrl +
      '%0A---------------------------------' +
      '&labels[]=type:bug' +
      '&labels[]=category:' + domainManagement +
      '&labels[]=prio:?';
      return feedbackUrl;
    };

    $scope.closeDialog = function() {
      if ($scope.closeWelcomeDialogForever) {
        localStorageService.set('closeWelcomeDialogForever', true);
      }
      $mdDialog.hide();
    };
  });
