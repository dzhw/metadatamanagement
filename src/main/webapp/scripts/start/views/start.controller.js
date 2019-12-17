(function() {
  'use strict';

  function StartController(
    $scope,
    StudySearchService,
    LanguageService, Principal, $state) {
    if (Principal.isAuthenticated()) {
      $state.go('search');
      return;
    }
    // TODO: Change hardcoded id
    var id = 'stu-gra2005$';

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
    function loadStudy(id) {
      var excludes = ['nested*','variables','questions',
        'surveys','instruments', 'dataSets', 'relatedPublications',
        'concepts'];
      StudySearchService.findShadowByIdAndVersion(id, null, excludes)
        .promise.then(function(data) {
          $scope.study = data;
        });
    }

    loadStudy(id);
  }

  angular
    .module('metadatamanagementApp')
    .controller('StartController', StartController);
})();
