/*global _*/
'use strict';

angular.module('metadatamanagementApp').factory('ToolbarHeaderService',
  function($rootScope) {
    var searchItem;
    var translationStringsMap = {
      'questionDetail': {
        'type': 'question-management.detail.question',
        'translateString': 'global.tooltips.toolbarHeader.question',
        'iconType': 'svg',
        'icon': 'assets/images/icons/question.svg'
      },
      'variableDetail': {
        'type': 'variable-management.detail.variable',
        'translateString': 'global.tooltips.toolbarHeader.variable',
        'iconType': 'svg',
        'icon': 'assets/images/icons/variable.svg'
      },
      'instrumentDetail': {
        'type': 'instrument-management.detail.instrument',
        'translateString': 'global.tooltips.toolbarHeader.instrument',
        'iconType': 'svg',
        'icon': 'assets/images/icons/instrument.svg'
      },
      'dataSetDetail': {
        'type': 'data-set-management.detail.data-set',
        'translateString': 'global.tooltips.toolbarHeader.data-set',
        'iconType': 'svg',
        'icon': 'assets/images/icons/data-set.svg'
      },
      'disclosure': {
        'type': 'disclosure.title'
      },
      'relatedPublicationDetail': {
        'type': 'related-publication-management.detail.publication',
        'translateString': 'global.tooltips.toolbarHeader.publication',
        'iconType': 'svg',
        'icon': 'assets/images/icons/related-publication.svg'
      },
      'search': {
        'type': 'search-management.detail.search',
        'translateString': 'global.tooltips.toolbarHeader.search',
        'tab': 'search-management.tabs.',
        'iconType': 'font',
        'icon': 'search'
      },
      'studyDetail': {
        'type': 'study-management.detail.study',
        'translateString': 'global.tooltips.toolbarHeader.study',
        'iconType': 'svg',
        'icon': 'assets/images/icons/study.svg'
      },
      'surveyDetail': {
        'type': 'survey-management.detail.survey',
        'types': 'survey-management.detail.surveys',
        'translateString': 'global.tooltips.toolbarHeader.survey',
        'translateStrings': 'global.tooltips.toolbarHeader.surveys',
        'iconType': 'svg',
        'icon': 'assets/images/icons/survey.svg'
      },
      'login': {
        'type': 'global.toolbar.buttons.login'
      },
      'metrics': {
        'type': 'global.menu.admin.metrics'
      },
      'health': {
        'type': 'global.menu.admin.health'
      },
      'configuration': {
        'type': 'global.menu.admin.configuration'
      },
      'logs': {
        'type': 'global.menu.admin.logs'
      },
      'settings': {
        'type': 'global.menu.account.settings'
      },
      'password': {
        'type': 'global.menu.account.password'
      },
      'register': {
        'type': 'global.toolbar.buttons.register'
      },
      'activate': {
        'type': 'user-management.activate.title'
      },
      'requestReset': {
        'type': 'global.menu.account.password'
      },
      'finishReset': {
        'type': 'global.menu.account.password'
      },
      'user-management': {
        'type': 'global.menu.admin.user-management'
      },
      'user-management-detail': {
        'type': 'user-management.home.title'
      },
      'error': {
        'type': 'global.error.title'
      }
    };
    var createRelatedSurveyItem = function(surveys, itemTyp, itemId) {
      var surveyItem = {
        'iconType': translationStringsMap.instrumentDetail.iconType,
        'icon': translationStringsMap.surveyDetail.icon
      };
      if (surveys.length === 1) {
        surveyItem.state = 'surveyDetail({"id":"' + surveys[0].id +
        '"})';
        surveyItem.type = translationStringsMap.surveyDetail.type;
        surveyItem.tooltip = translationStringsMap.surveyDetail.
        translateString;
        surveyItem.number = surveys[0].number;
      }
      if (surveys.length > 1) {
        var stateParams = {'type': 'surveys', 'page': 1};
        stateParams[itemTyp] = itemId;
        surveyItem.state = 'search(' + JSON.stringify(stateParams) + ')';
        surveyItem.type = translationStringsMap.surveyDetail.types;
        surveyItem.tooltip = translationStringsMap.surveyDetail.
        translateStrings;
        surveyItem.numbers = surveys.length > 2 ? surveys[0].number +
        ', ..., ' + _.last(surveys) : surveys[0].number + ', ' +
        surveys[1].number;
      }
      if (surveys.length === 0) {
        surveyItem.disabled = true;
        surveyItem.type = translationStringsMap.surveyDetail.type;
        surveyItem.notFound = '?'; // survey undefined...
      }
      return surveyItem;
    };
    var updateToolbarHeader = function(item) {
      $rootScope.toolbarHeaderItems = [];
      var instrumentItem = {};
      var questionItem = {};
      var dataSetItem = {};
      var variableItem = {};
      var publicationItem = {};
      var surveyItem = {};
      if (!searchItem) {
        searchItem = {};
        searchItem.type = translationStringsMap.search.type;
        searchItem.tooltip = translationStringsMap.search.
        translateString;
        searchItem.state = 'search({"page": 1})';
        searchItem.tabName = 'search-management.tabs.all';
        searchItem.iconType = translationStringsMap.search.iconType;
        searchItem.icon = translationStringsMap.search.icon;
      }
      var studyItem = {};
      if (item.projectId) {
        studyItem = {
          'state': 'studyDetail({"id":"' + item.studyId + '"})',
          'type': translationStringsMap.studyDetail.type,
          'tooltip': translationStringsMap.studyDetail.translateString,
          'iconType': translationStringsMap.instrumentDetail.iconType,
          'icon': translationStringsMap.studyDetail.icon,
          'projectId': item.projectId
        };
      }
      switch (item.stateName) {
        case 'studyDetail':
          $rootScope.toolbarHeaderItems.push(searchItem, studyItem);
        break;
        case 'questionDetail':
          instrumentItem = {
            'state': 'instrumentDetail({"id":"' + item.instrumentId + '"})',
            'type': translationStringsMap.instrumentDetail.type,
            'tooltip': translationStringsMap.instrumentDetail.translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.instrumentDetail.icon,
            'number': item.instrumentNumber
          };
          questionItem = {
            'state': 'questionDetail',
            'type': translationStringsMap.questionDetail.type,
            'tooltip': translationStringsMap.questionDetail.translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.questionDetail.icon,
            'number': item.questionNumber
          };
          surveyItem = createRelatedSurveyItem(item.surveys, 'question',
          item.id);
          $rootScope.toolbarHeaderItems.push(searchItem, studyItem, surveyItem,
            instrumentItem, questionItem);
        break;
        case 'variableDetail':
          dataSetItem = {
            'state': 'dataSetDetail({"id":"' + item.dataSetId + '"})',
            'type': translationStringsMap.dataSetDetail.type,
            'tooltip': translationStringsMap.dataSetDetail.translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.dataSetDetail.icon,
            'number': item.dataSetNumber
          };
          variableItem = {
            'state': 'variableDetail',
            'type': translationStringsMap.variableDetail.type,
            'tooltip': translationStringsMap.variableDetail.translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.variableDetail.icon,
            'name': item.name
          };
          surveyItem = createRelatedSurveyItem(item.surveys, 'variable',
          item.id);
          $rootScope.toolbarHeaderItems.push(searchItem, studyItem, surveyItem,
            dataSetItem, variableItem);
        break;
        case 'surveyDetail':
          surveyItem = {
            'state': 'surveyDetail',
            'type': translationStringsMap.surveyDetail.type,
            'tooltip': translationStringsMap.surveyDetail.translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.surveyDetail.icon,
            'number': item.number
          };
          $rootScope.toolbarHeaderItems.push(searchItem, studyItem, surveyItem);
        break;
        case 'dataSetDetail':
          dataSetItem = {
            'state': 'dataSetDetail',
            'type': translationStringsMap.dataSetDetail.type,
            'tooltip': translationStringsMap.dataSetDetail.translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.dataSetDetail.icon,
            'number': item.number
          };
          surveyItem = createRelatedSurveyItem(item.surveys, 'data-set',
          item.id);
          $rootScope.toolbarHeaderItems.push(searchItem, studyItem, surveyItem,
            dataSetItem);
        break;
        case 'instrumentDetail':
          instrumentItem = {
            'state': 'instrumentDetail',
            'type': translationStringsMap.instrumentDetail.type,
            'tooltip': translationStringsMap.instrumentDetail.translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.instrumentDetail.icon,
            'number': item.number
          };
          surveyItem = createRelatedSurveyItem(item.surveys, 'instrument',
          item.id);
          $rootScope.toolbarHeaderItems.push(searchItem, studyItem,
            surveyItem, instrumentItem);
        break;
        case 'relatedPublicationDetail':
          publicationItem = {
            'state': 'relatedPublicationDetail',
            'type': translationStringsMap.relatedPublicationDetail.type,
            'tooltip': translationStringsMap.relatedPublicationDetail.
            translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.relatedPublicationDetail.icon,
            'id': item.id
          };
          $rootScope.toolbarHeaderItems.push(searchItem, publicationItem);
        break;
        case 'search':
          searchItem = {};
          searchItem.type = translationStringsMap.search.type;
          searchItem.tooltip = translationStringsMap.search.
          translateString;
          searchItem.iconType = translationStringsMap.search.iconType;
          searchItem.icon = translationStringsMap.search.icon;
          searchItem.disabled = false;
          if (_.size(item.searchParams) > 1) {
            searchItem.tabName = item.tabName;
          } else {
            searchItem.tabName = 'search-management.tabs.all';
          }
          searchItem.state = 'search(' + JSON.stringify(item.searchParams) +
          ')';
          $rootScope.toolbarHeaderItems.push(searchItem);
        break;
        case 'disclosure':
          var disclosureItem = {
            'state': 'disclosure',
            'type': translationStringsMap.disclosure.type
          };
          $rootScope.toolbarHeaderItems.push(disclosureItem);
        break;
        case 'user-management':
          var managementItem = {
            'state': 'user-management',
            'type': translationStringsMap['user-management'].type
          };
          $rootScope.toolbarHeaderItems.push(managementItem);
        break;
        case 'user-management-detail':
          var userDetailItem = {
            'state': 'user-management-detail',
            'type': translationStringsMap['user-management-detail'].type
          };
          $rootScope.toolbarHeaderItems.push(userDetailItem);
        break;
        case 'login':
          var loginItem = {
            'state': 'login',
            'type': translationStringsMap.login.type
          };
          $rootScope.toolbarHeaderItems.push(loginItem);
        break;
        case 'metrics':
          var metricsItem = {
            'state': 'metrics',
            'type': translationStringsMap.metrics.type
          };
          $rootScope.toolbarHeaderItems.push(metricsItem);
        break;
        case 'health':
          var healthItem = {
            'state': 'health',
            'type': translationStringsMap.health.type
          };
          $rootScope.toolbarHeaderItems.push(healthItem);
        break;
        case 'configuration':
          var configItem = {
            'state': 'configuration',
            'type': translationStringsMap.configuration.
            type
          };
          $rootScope.toolbarHeaderItems.push(configItem);
        break;
        case 'logs':
          var logsItem = {
            'state': 'logs',
            'type': translationStringsMap.logs.type
          };
          $rootScope.toolbarHeaderItems.push(logsItem);
        break;
        case 'settings':
          var settingsItem = {
            'state': 'settings',
            'type': translationStringsMap.settings.type
          };
          $rootScope.toolbarHeaderItems.push(settingsItem);
        break;
        case 'password':
          var passwordItem = {
            'state': 'password',
            'type': translationStringsMap.password.type
          };
          $rootScope.toolbarHeaderItems.push(passwordItem);
        break;
        case 'register':
          var registerItem = {
            'state': 'register',
            'type': translationStringsMap.register.type
          };
          $rootScope.toolbarHeaderItems.push(registerItem);
        break;
        case 'activate':
          var activateItem = {
            'state': 'activate',
            'type': translationStringsMap.activate.type
          };
          $rootScope.toolbarHeaderItems.push(activateItem);
        break;
        case 'finishReset':
          var finishResetItem = {
            'state': 'finishReset',
            'type': translationStringsMap.finishReset
            .type
          };
          $rootScope.toolbarHeaderItems.push(finishResetItem);
        break;
        case 'requestReset':
          var requestResetItem = {
            'state': 'requestReset',
            'type': translationStringsMap.requestReset.
            type
          };
          $rootScope.toolbarHeaderItems.push(requestResetItem);
        break;
        case 'error':
          var errorItem = {
            'state': 'error',
            'type': translationStringsMap.error.type
          };
          $rootScope.toolbarHeaderItems.push(errorItem);
        break;
        default:
          if (item.stateName) {
            console.log(item.stateName + ': coming...');
          }
        break;
      }
    };
    return {
      updateToolbarHeader: updateToolbarHeader
    };
  });
