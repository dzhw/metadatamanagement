/* global _ */

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

    var isTypeUpdateAllowed = function(project, type) {
      var isTypeReady = false;
      if (type && _.includes(project.configuration.dataProviders,
        Principal.loginName())) {
        var conf = project.configuration;
        switch (type) {
          case 'studies':
            isTypeReady = _.get(conf, 'studiesState.dataProviderReady') ||
              _.get(conf, 'studiesState.publisherReady');
            break;
          case 'surveys':
            isTypeReady = _.get(conf, 'surveysState.dataProviderReady') ||
              _.get(conf, 'surveysState.publisherReady');
            break;
          case 'instruments':
            isTypeReady = _.get(conf, 'instrumentsState.dataProviderReady') ||
              _.get(conf, 'instrumentsState.publisherReady');
            break;
          case 'data_sets':
            isTypeReady = _.get(conf, 'dataSetsState.dataProviderReady') ||
              _.get(conf, 'dataSetsState.publisherReady');
            break;
          case 'questions':
            isTypeReady = _.get(conf, 'questionsState.dataProviderReady') ||
              _.get(conf, 'questionsState.publisherReady');
            break;
          case 'variables':
            isTypeReady = _.get(conf, 'variablesState.dataProviderReady') ||
              _.get(conf, 'variablesState.publisherReady');
            break;
        }
      }
      return !isTypeReady;
    };

    var isUpdateAllowed = function(project, type) {
      var test = project || CurrentProjectService.getCurrentProject();

      return isProjectSelected(test) && isTypeUpdateAllowed(test, type) &&
        isProjectUnreleased(test) && isMemberOfAssignedGroup(test);
    };

    return {
      isUpdateAllowed: isUpdateAllowed
    };

  });
