/* global window */
/* Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('NavbarController',
  function($scope, Principal, $mdSidenav, $document, $timeout,
    LanguageService, Auth, $state, $location, $translate) {
    $scope.isAuthenticated = Principal.isAuthenticated;

    //For toggle buttons
    $scope.isProjectMenuOpen = false;
    $scope.isAdminMenuOpen = false;
    $scope.isAccountMenuOpen = false;

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
      if (encodedUrl.includes('data-sets')) {
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

    //Functions for toggling buttons.
    $scope.toggleAccountMenu = function() {
      $scope.isAccountMenuOpen = !$scope.isAccountMenuOpen;
    };

    $scope.toggleAdminMenu = function() {
      $scope.isAdminMenuOpen = !$scope.isAdminMenuOpen;
    };

    $scope.close = function() {
      if (!$mdSidenav('SideNavBar').isLockedOpen()) {
        $timeout($mdSidenav('SideNavBar').toggle, 200);
      }
    };

    $scope.focusContent = function() {
      $document.find('.fdz-content')[0].focus();
    };

    //Set Languages
    $scope.changeLanguage = function(languageKey) {
      LanguageService.setCurrent(languageKey);
    };

    //Goto Logout Page
    $scope.logout = function() {
      Auth.logout();
      $state.go('search', {
        lang: LanguageService.getCurrentInstantly()
      });
      $scope.close();
    };
  });
