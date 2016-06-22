/* Author: Daniel Katzberg */
'use strict';
angular.module('metadatamanagementApp').service('CurrentProjectService',
  function() {

    //Define current Project field with get and set methods
    var currentProject = {};
    var setCurrentProject = function(chosenProject) {
      currentProject = chosenProject;
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
