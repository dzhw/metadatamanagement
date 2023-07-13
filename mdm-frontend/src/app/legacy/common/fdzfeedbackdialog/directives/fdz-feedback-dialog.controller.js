/* global window */
/* Author Daniel Katzberg */
'use strict';
angular.module('metadatamanagementApp')
  .controller('FdzFeedbackDialogController', [
  '$scope',
  '$mdDialog',
  'bowser',
  '$location',
  '$translate',
  function($scope, $mdDialog, bowser, $location,
      $translate) {
    $scope.bowser = bowser;

    var checkDomainManagement = function() {
      var domainManagement = 'crosscutting';
      var encodedUrl = window.encodeURIComponent($location.absUrl());
      if (encodedUrl.includes('data-packages')) {
        domainManagement = 'datapackagemanagement';
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

      return domainManagement;
    };

    var sourceLang = $translate
      .instant('global.navbar-feedback.source');
    var categoryLang = $translate
      .instant('global.navbar-feedback.category');
    $scope.mailBody = '';
    $scope.mailSubject = $translate
      .instant('global.feedback-dialog.mail-subject');
    $scope.mailBody =
      '%0A---------------------------------%0A' +
      sourceLang + '%3A%0A' +
      window.encodeURIComponent($location.absUrl()) + '%0A' +
      categoryLang + '%3A ' + checkDomainManagement() +
      '%0A---------------------------------';

    $scope.currentUrl = function() {
      var encodedUrl = window.encodeURIComponent($location.absUrl());
      var feedbackUrl = 'https://github.com/dzhw/' +
      'metadatamanagement/issues/new?' +
      'body=%0A---------------------------------%0A' + sourceLang + '%3A%0A' +
      encodedUrl +
      '%0A---------------------------------' +
      '&labels[]=type:bug' +
      '&labels[]=category:' + checkDomainManagement() +
      '&labels[]=prio:?';
      return feedbackUrl;
    };

    $scope.closeDialog = function() {
      $mdDialog.hide();
    };
  }]);

