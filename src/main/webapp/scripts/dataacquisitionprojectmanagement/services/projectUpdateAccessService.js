/* global _ */

'use strict';
angular.module('metadatamanagementApp').service(
  'ProjectUpdateAccessService',
  function(CurrentProjectService, Principal, SimpleMessageToastService,
    $log, $q, SearchDao) {

    var messagePrefix = 'data-acquisition-project-management.error.project' +
      '-update-access.';

    var errorList = {
      projectSelected: messagePrefix + 'project-selected',
      typeUpdateAllowed: messagePrefix + 'type-update-allowed',
      projectUnreleased: messagePrefix + 'project-released',
      memberOfAssignedGroup: messagePrefix + 'member-of-assigned-group',
      assignedToProject: messagePrefix + 'assigned-to-project',
      isNotRequired: messagePrefix + 'not-required',
      prerequisiteMissing: messagePrefix + 'prerequisite-missing',
      updateForPublisherAllowed: messagePrefix +
        'update-for-publishers-allowed',
      updateForDataProviderAllowed: messagePrefix +
        'update-for-data-providers-allowed'
    };

    var isProjectSelected = function(project) {
      return project !== undefined && project !== null;
    };

    var isProjectUnreleased = function(project) {
      return !project.release;
    };

    var isTypeRequired = function(project, type) {
      if (type) {
        type = type === 'data_sets' ? 'dataSets' : type;
        type = type === 'data_packages' ? 'dataPackages' : type;
        type = type === 'analysis_packages' ? 'analysisPackages' : type;
        return _.get(project, 'configuration.requirements.' + type +
          'Required');
      } else {
        return false;
      }
    };

    var getProjectConfigStateAttributeForType = function(type) {
      switch (type) {
        case 'data_packages':
          return 'dataPackagesState';
        case 'analysis_packages':
          return 'analysisPackagesState';
        case 'surveys':
          return 'surveysState';
        case 'instruments':
          return 'instrumentsState';
        case 'data_sets':
          return 'dataSetsState';
        case 'questions':
          return 'questionsState';
        case 'variables':
          return 'variablesState';
        case 'publications':
          return 'publicationsState';
        case 'concepts':
          return 'conceptsState';
      }
    };

    var isPublisherUpdateAllowedForType = function(project, type) {
      var stateAttribute = getProjectConfigStateAttributeForType(type);
      return !_.get(project.configuration, stateAttribute + '.publisherReady');
    };

    var isDataProviderUpdateAllowedForType = function(project, type) {

      if (isPublisherUpdateAllowedForType(project, type)) {
        var stateAttribute = getProjectConfigStateAttributeForType(type);
        return !_.get(project.configuration, stateAttribute +
          '.dataProviderReady');
      } else {
        return false;
      }
    };

    var isAssignedToProject = function(project, role) {
      if (!project) {
        return false;
      }
      role = role || '';
      var userLogin = Principal.loginName();
      var isAssigned = false;
      if (role === '' || role === 'publishers') {
        if (Principal.hasAuthority('ROLE_PUBLISHER')) {
          isAssigned = isAssigned ||
            project.configuration.publishers.indexOf(userLogin) !== -1;
        }
      }
      if (role === '' || role === 'dataProviders') {
        if (Principal.hasAuthority('ROLE_DATA_PROVIDER')) {
          isAssigned = isAssigned ||
            project.configuration.dataProviders.indexOf(userLogin) !== -1;
        }
      }
      return isAssigned;
    };

    var isMemberOfAssignedGroup = function(project) {
      switch (project.assigneeGroup) {
        case 'PUBLISHER':
          return isAssignedToProject(project, 'publishers');
        case 'DATA_PROVIDER':
          return isAssignedToProject(project, 'dataProviders');
        default:
          return false;
      }
    };

    var isUpdateAllowed = function(project, type, notify) {
      if (type) {
        type = type.replace(/([a-z])([A-Z])/g,
          function($1, $2, $3) {  // jshint ignore:line
            return $2 + '_' + $3.toLowerCase();});
      }

      if (!_.includes(['data_packages', 'analysis_packages',
        'surveys', 'instruments',
        'data_sets', 'questions',
        'variables', 'publications',
        'concepts', undefined, null], type)) {
        return false;
      }

      if (type === 'publications' || type === 'concepts') {
        return true;
      }

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

      var updateForRoleAllowed;

      if (Principal.hasAuthority('ROLE_PUBLISHER')) {
        updateForRoleAllowed = isPublisherUpdateAllowedForType.bind(null, test,
          type);
        updateForRoleAllowed.canContinue = true;
        updateForRoleAllowed.errorKey = errorList.updateForPublisherAllowed;
      } else if (Principal.hasAuthority('ROLE_DATA_PROVIDER')) {
        updateForRoleAllowed = isDataProviderUpdateAllowedForType.bind(null,
          test, type);
        updateForRoleAllowed.canContinue = true;
        updateForRoleAllowed.errorKey = errorList.updateForDataProviderAllowed;
      } else {
        $log.warn('User has no supported authority (ROLE_PUBLISHER,' +
          'ROLE_DATA_PROVIDER). Validation will fail');
      }

      var projectUnreleased = isProjectUnreleased.bind(null, test);
      projectUnreleased.canContinue = true;
      projectUnreleased.errorKey = errorList.projectUnreleased;

      var validations = [projectSelected, assignedToProject, typeRequired,
        memberOfAssignedGroup, updateForRoleAllowed, projectUnreleased];

      for (var i = 0; i < validations.length; i++) {
        try {
          validations[i].isValid = validations[i]();
        } catch (e) {
          isValid = false;
          $log.debug('error during validation:', e);
          break;
        }
        isValid = isValid && validations[i].isValid;
        if (!validations[i].isValid && !validations[i].canContinue) {
          break;
        }
      }
      if (notify) {
        var notification = [];
        validations.forEach(function(validation) {
          if (validation.isValid === false) {
            notification.push(validation.errorKey);
          }
        });

        if (notification.length) {
          SimpleMessageToastService.openAlertMessageToasts(notification.map(
            function(not) {
              return {messageId: not};
            }
          ));
        }
      }

      return isValid;
    };

    var isPrerequisiteFulfilled = function(project, type) {
      var deferred = $q.defer();

      if (!project || !type) {
        deferred.reject(false);
        return deferred.promise;
      }

      type = type.replace(/([a-z])([A-Z])/g,
        function($1, $2, $3) {  // jshint ignore:line
          return $2 + '_' + $3.toLowerCase();});

      var prereq = '';
      if (type === 'instruments') {
        prereq = 'surveys';
      }
      if (type === 'data_sets') {
        prereq = 'surveys';
      }
      if (type === 'publications') {
        prereq = 'data_packages';
      }

      if (!prereq) {
        deferred.resolve(true);
        return deferred.promise;
      }

      var check = function(count) {
        if (count >= 1) {
          deferred.resolve(true);
        } else {
          SimpleMessageToastService.openAlertMessageToast(
            errorList.prerequisiteMissing + '-' + prereq);
          deferred.reject(false);
        }
      };

      SearchDao.search('', 1, project.id, {}, undefined, 0, undefined)
        .then(function(data) {
            var bucket  = _.find(data.aggregations.countByType.buckets,
              {key: prereq});
            check(_.get(bucket, 'doc_count', 0));
          });

      return deferred.promise;
    };

    return {
      isAssignedToProject: isAssignedToProject,
      isUpdateAllowed: isUpdateAllowed,
      isPrerequisiteFulfilled: isPrerequisiteFulfilled
    };

  });
