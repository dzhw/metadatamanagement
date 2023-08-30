(function() {
  'use strict';

  function StartController(
    $scope, LanguageService, Principal, $state, PageMetadataService,
    PinnedDataPackagesService, SearchDao) {
    PageMetadataService.setPageTitle('start.data-search');
    PageMetadataService.setPageDescription('start.fdz-text');

    if (Principal.isAuthenticated()) {
      $state.go('search');
      return;
    }
    SearchDao.search('', 0, null, null, 'data_packages', 0, null, null, null,
      'relevance', false, 'analysis_packages').then(function(data) {
      $scope.dataPackagesCount = data.responses[0].hits.total.value;
      $scope.analysisPackagesCount = data.responses[1].hits.total.value;
    });

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
    .controller('StartController', [
      '$scope',
      'LanguageService',
      'Principal',
      '$state',
      'PageMetadataService',
      'PinnedDataPackagesService',
      'SearchDao',
      StartController
    ]);
})();
