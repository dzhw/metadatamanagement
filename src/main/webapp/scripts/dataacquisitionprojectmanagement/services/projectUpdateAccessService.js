'use strict';
angular.module('metadatamanagementApp').service(
  'ProjectUpdateAccessService',
  function(CurrentProjectService, Principal) {

    var isProjectSelected = function(project) {
      return project !== undefined && project !== null;
    };

    var isProjectUnreleased = function(project) {
      return !project.release;
    };

    var isMemberOfAssignedGroup = function(project) {
      switch (project.assigneeGroup) {
        case 'PUBLISHER':
          return Principal.hasAuthority('ROLE_PUBLISHER');
        case 'DATA_PROVIDER':
          return Principal.hasAuthority('ROLE_DATA_PROVIDER');
        default:
          return false;
      }
    };

    var isUpdateAllowed = function(project) {
      var test = project || CurrentProjectService.getCurrentProject();
      return isProjectSelected(test) &&
        isProjectUnreleased(test) &&
        isMemberOfAssignedGroup(test);
    };

    return {
      isUpdateAllowed: isUpdateAllowed
    };

  });
