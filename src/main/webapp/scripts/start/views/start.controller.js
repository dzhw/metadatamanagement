(function() {
  'use strict';

  function StartController($scope, StudyResource) {
    // TODO: Change hardcoded id
    var id = 'stu-gra2005$';

    $scope.study = {};

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
