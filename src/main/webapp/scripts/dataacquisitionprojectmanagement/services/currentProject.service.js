/* Author: Daniel Katzberg */
'use strict';
angular.module('metadatamanagementApp').service('CurrentProjectService',
  function($rootScope, localStorageService) {

    //Define current Project field with get and set methods
    var currentProject = localStorageService.get('currentProject');
    var setCurrentProject = function(chosenProject) {
      if (!angular.equals(currentProject, chosenProject)) {
        currentProject = chosenProject;
        localStorageService.set('currentProject', currentProject);
        $rootScope.$broadcast('current-project-changed', currentProject);
      }
    };
    var getCurrentProject = function() {
      return currentProject;
    };

    //make the methods global
    return {
      setCurrentProject: setCurrentProject,
      getCurrentProject: getCurrentProject
    };
  });
