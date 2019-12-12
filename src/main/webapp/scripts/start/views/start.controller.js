(function() {
  'use strict';

  function StartController(
    $scope,
    StudyResource,
    LanguageService) {
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
      en: 'DZHW Survey Series of School Leavers 2008',
      de: 'DZHW-Studienberechtigtenbefragungen'
    };
    function loadStudy(id) {
      StudyResource.get({id: id})
        .$promise
        .then(function(data) {
          $scope.study = data;
        });
    }

    loadStudy(id);
  }

  angular
    .module('metadatamanagementApp')
    .controller('StartController', StartController);
})();
