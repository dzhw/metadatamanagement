/* global _ */

'use strict';
angular.module('metadatamanagementApp').service(
  'ProjectUpdateAccessService',
  function(CurrentProjectService, Principal, SimpleMessageToastService) {

    var messagePrefix = 'data-acquisition-project-management.error.project' +
      '-update-access.';

    var errorList = {
      projectSelected: messagePrefix + 'project-selected',
      typeUpdateAllowed: messagePrefix + 'type-update-allowed',
      projectUnreleased: messagePrefix + 'project-released',
      memberOfAssignedGroup: messagePrefix + 'member-of-assigned-group',
      assignedToProject: messagePrefix + 'assigned-to-project',
      isNotRequired: messagePrefix + 'not-required'
    };

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

    var isTypeRequired = function(project, type) {
      if (type) {
        type = type === 'data_sets' ? 'dataSets' : type;
        return _.get(project, 'configuration.requirements.' + type +
          'Required');
      } else {
        return false;
      }
    };

    var isPublisherReady = function(project, type) {
      var isTypeReady = false;
      if (type) {
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

    var isAssignedToProject = function(project) {
      var userLogin = Principal.loginName();
      if (Principal.hasAuthority('ROLE_PUBLISHER')) {
        return project.configuration.publishers.indexOf(userLogin) !== -1;
      }

      if (Principal.hasAuthority('ROLE_DATA_PROVIDER')) {
        return project.configuration.dataProviders.indexOf(userLogin) !== -1;
      }

      return false;
    };

    var isUpdateAllowed = function(project, type, notify) {
      var test = project || CurrentProjectService.getCurrentProject();
      var isValid = true;

      var projectSelected = isProjectSelected.bind(null, test);
      projectSelected.canContinue = false;
      projectSelected.errorKey = errorList.projectSelected;

      var assignedToProject = isAssignedToProject.bind(null, test);
      assignedToProject.canContinue = false;
      assignedToProject.errorKey = errorList.assignedToProject;

      var typeRequired = isTypeRequired.bind(null, test, type);
      typeRequired.canContinue = true;
      typeRequired.errorKey = errorList.isNotRequired;

      var memberOfAssignedGroup = isMemberOfAssignedGroup.bind(null, test);
      memberOfAssignedGroup.canContinue = true;
      memberOfAssignedGroup.errorKey = errorList.memberOfAssignedGroup;

      var publisherReady = isPublisherReady.bind(null, test, type);
      publisherReady.canContinue = true;
      publisherReady.errorKey = errorList.typeUpdateAllowed;

      var projectUnreleased = isProjectUnreleased.bind(null, test);
      projectUnreleased.canContinue = true;
      projectUnreleased.errorKey = errorList.projectUnreleased;

      var validations = [projectSelected, assignedToProject, typeRequired,
        memberOfAssignedGroup, publisherReady, projectUnreleased];

      for (var i = 0; i < validations.length; i++) {
        validations[i].isValid = validations[i]();
        isValid = isValid && validations[i].isValid;
        if (!validations[i].isValid && !validations[i].canContinue) {
          break;
        }
      }

      if (notify) {
        validations.forEach(function(validation) {
          if (validation.isValid === false) {
            SimpleMessageToastService
              .openAlertMessageToast(validation.errorKey);
          }
        });
      }

      return isValid;
    };

    return {
      isUpdateAllowed: isUpdateAllowed
    };

  });
