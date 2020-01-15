(function() {
  'use strict';

  function StartController(
    $scope, LanguageService, Principal, $state, PageTitleService) {
    PageTitleService.setPageTitle('start.data-search');

    if (Principal.isAuthenticated()) {
      $state.go('search');
      return;
    }

    $scope.study = {};
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
