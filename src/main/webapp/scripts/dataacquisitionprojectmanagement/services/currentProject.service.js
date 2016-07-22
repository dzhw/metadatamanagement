/* Author: Daniel Katzberg */
'use strict';
angular.module('metadatamanagementApp').service('CurrentProjectService',
  function($rootScope) {

    //Define current Project field with get and set methods
    var currentProject = null;
    var setCurrentProject = function(chosenProject) {
      currentProject = chosenProject;
      $rootScope.$broadcast('current-project-changed', currentProject);
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
