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

    var isUpdateAllowed = function(project, type) {
      var test = project || CurrentProjectService.getCurrentProject();
      var isTypeReady = false;
      if (type && Principal.hasAuthority('ROLE_DATA_PROVIDER')) {
        var conf = test.configuration;
        switch (type) {
          case 'studies':
            isTypeReady = (conf.studiesState.dataProviderReady ||
                conf.studiesState.publisherReady);
            break;
          case 'surveys':
            isTypeReady = (conf.surveysState.dataProviderReady ||
                conf.surveysState.publisherReady);
            break;
          case 'instruments':
            isTypeReady = (conf.instrumentsState.dataProviderReady ||
                conf.instrumentsState.publisherReady);
            break;
          case 'data_sets':
            isTypeReady = (conf.dataSetsState.dataProviderReady ||
                conf.dataSetsState.publisherReady);
            break;
          case 'questions':
            isTypeReady = (conf.questionsState.dataProviderReady ||
                conf.questionsState.publisherReady);
            break;
          case 'variables':
            isTypeReady = (conf.variablesState.dataProviderReady ||
                conf.variablesState.publisherReady);
            break;
        }
      }
      return !isTypeReady && isProjectSelected(test) &&
        isProjectUnreleased(test) && isMemberOfAssignedGroup(test);
    };

    return {
      isUpdateAllowed: isUpdateAllowed
    };

  });
