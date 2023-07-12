/* Author: Daniel Katzberg */
'use strict';
angular.module('metadatamanagementApp').service('CurrentProjectService',
  function($rootScope, $location, localStorageService,
    DataAcquisitionProjectResource) {

    //Define current Project field with get and set methods

    var currentProject;

    var setCurrentProject = function(chosenProject) {
      if (!angular.equals(currentProject, chosenProject)) {
        currentProject = chosenProject;
        if (currentProject) {
          localStorageService.set('currentProject', currentProject);
        } else {
          localStorageService.remove('currentProject');
        }
        $rootScope.$broadcast('current-project-changed', currentProject);
      }
    };
    var getCurrentProject = function() {
      return currentProject;
    };

    // see: ProjectCockpitController
    if (!/\/projects\/[a-zA-Z0-9]+/.test($location.path())) {
      currentProject = localStorageService.get('currentProject');
      if (currentProject) {
        DataAcquisitionProjectResource.get({
          id: currentProject.id
        }).$promise.then(
          function(project) {
            if (project) {
              setCurrentProject(project);
            } else {
              setCurrentProject(undefined);
            }
          }).catch(function() {
            setCurrentProject(undefined);
          });
      } else {
        setCurrentProject(undefined);
      }
    } else {
      setCurrentProject(undefined);
    }

    $rootScope.$on('user-logged-out', function() {
      setCurrentProject(undefined);
    });

    //make the methods global
    return {
      setCurrentProject: setCurrentProject,
      getCurrentProject: getCurrentProject
    };
  });
