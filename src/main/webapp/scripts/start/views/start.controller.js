(function() {
  'use strict';

  function StartController(
    $scope, LanguageService, Principal, $state, PageMetadataService,
    PinnedDataPackagesService) {
    PageMetadataService.setPageTitle('start.data-search');
    PageMetadataService.setPageDescription('start.fdz-text');

    if (Principal.isAuthenticated()) {
      $state.go('search');
      return;
    }

    PinnedDataPackagesService.getPinnedDataPackage().then(
      function(response) {
      $scope.pinnedDataPackage = response.data;
    });

    $scope.lang = LanguageService.getCurrentInstantly();
    $scope.socialSurvey = {
      en: 'Social Survey',
      de: 'Sozialerhebung'
    };
    $scope.graduateSurvey = {
      en: 'DZHW Graduate Survey Series',
      de: 'DZHW-Absolventenstudien'
    };

    $scope.schoolSurvey = {
      en: 'DZHW Survey Series of School Leavers',
      de: 'DZHW-Studienberechtigtenbefragungen'
    };
  }

  angular
    .module('metadatamanagementApp')
    .controller('StartController', StartController);
})();
