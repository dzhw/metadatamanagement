/*global _*/
'use strict';

angular.module('metadatamanagementApp').factory('ToolbarHeaderService',
  function($rootScope) {
    var searchItem;
    var translationStringsMap = {
      'questionDetail': {
        'translateString': 'question-management.detail.question',
        'iconType': 'svg',
        'icon': 'assets/images/icons/question.svg'
      },
      'variableDetail': {
        'translateString': 'variable-management.detail.variable',
        'iconType': 'svg',
        'icon': 'assets/images/icons/variable.svg'
      },
      'instrumentDetail': {
        'translateString': 'instrument-management.detail.instrument',
        'iconType': 'svg',
        'icon': 'assets/images/icons/instrument.svg'
      },
      'dataSetDetail': {
        'translateString': 'data-set-management.detail.data-set',
        'iconType': 'svg',
        'icon': 'assets/images/icons/data-set.svg'
      },
      'disclosure': {
        'translateString': 'disclosure.title'
      },
      'relatedPublicationDetail': {
        'translateString': 'related-publication-management.detail.publication',
        'iconType': 'svg',
        'icon': 'assets/images/icons/related-publication.svg'
      },
      'search': {
        'translateString': 'search-management.detail.search',
        'tab': 'search-management.tabs.',
        'iconType': 'font',
        'icon': 'search'
      },
      'studyDetail': {
        'translateString': 'study-management.detail.study',
        'iconType': 'svg',
        'icon': 'assets/images/icons/study.svg'
      },
      'surveyDetail': {
        'translateString': 'survey-management.detail.survey',
        'iconType': 'svg',
        'icon': 'assets/images/icons/survey.svg'
      },
      'login': {
        'translateString': 'global.toolbar.buttons.login'
      },
      'metrics': {
        'translateString': 'global.menu.admin.metrics'
      },
      'health': {
        'translateString': 'global.menu.admin.health'
      },
      'configuration': {
        'translateString': 'global.menu.admin.configuration'
      },
      'logs': {
        'translateString': 'global.menu.admin.logs'
      },
      'settings': {
        'translateString': 'global.menu.account.settings'
      },
      'password': {
        'translateString': 'global.menu.account.password'
      },
      'register': {
        'translateString': 'global.toolbar.buttons.register'
      },
      'activate': {
        'translateString': 'user-management.activate.title'
      },
      'requestReset': {
        'translateString': 'global.menu.account.password'
      },
      'finishReset': {
        'translateString': 'global.menu.account.password'
      },
      'user-management': {
        'translateString': 'global.menu.admin.user-management'
      },
      'user-management-detail': {
        'translateString': 'user-management.home.title'
      },
      'error': {
        'translateString': 'global.error.title'
      }
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
        searchItem.translateString = translationStringsMap.search
        .translateString;
        searchItem.state = 'search({"page": 1})';
        searchItem.tabName = 'search-management.tabs.all';
      }
      var studyItem = {};
      if (item.projectId) {
        studyItem = {
          'state': 'studyDetail({"id":"' + item.studyId + '"})',
          'translateString': translationStringsMap.studyDetail.
          translateString,
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
            'translateString': translationStringsMap.instrumentDetail.
            translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.instrumentDetail.icon,
            'number': item.instrumentNumber
          };
          questionItem = {
            'state': 'questionDetail',
            'translateString': translationStringsMap.questionDetail.
            translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.questionDetail.icon,
            'number': item.questionNumber
          };
          $rootScope.toolbarHeaderItems.push(searchItem, studyItem,
            instrumentItem,
            questionItem);
        break;
        case 'variableDetail':
          dataSetItem = {
            'state': 'dataSetDetail({"id":"' + item.dataSetId + '"})',
            'translateString': translationStringsMap.dataSetDetail.
             translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.dataSetDetail.icon,
            'number': item.dataSetNumber
          };
          variableItem = {
            'state': 'variableDetail',
            'translateString': translationStringsMap.variableDetail.
             translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.variableDetail.icon,
            'name': item.name
          };
          $rootScope.toolbarHeaderItems.push(searchItem, studyItem, dataSetItem,
            variableItem);
        break;
        case 'surveyDetail':
          surveyItem = {
            'state': 'surveyDetail',
            'translateString': translationStringsMap.surveyDetail.
            translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.surveyDetail.icon,
            'number': item.number
          };
          $rootScope.toolbarHeaderItems.push(searchItem, studyItem, surveyItem);
        break;
        case 'dataSetDetail':
          dataSetItem = {
            'state': 'dataSetDetail',
            'translateString': translationStringsMap.dataSetDetail.
            translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.dataSetDetail.icon,
            'number': item.number
          };
          $rootScope.toolbarHeaderItems.push(searchItem, studyItem,
            dataSetItem);
        break;
        case 'instrumentDetail':
          instrumentItem = {
            'state': 'instrumentDetail',
            'translateString': translationStringsMap.instrumentDetail.
            translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.instrumentDetail.icon,
            'number': item.number
          };
          $rootScope.toolbarHeaderItems.push(searchItem, studyItem,
            instrumentItem);
        break;
        case 'relatedPublicationDetail':
          publicationItem = {
            'state': 'relatedPublicationDetail',
            'translateString': translationStringsMap.relatedPublicationDetail.
            translateString,
            'iconType': translationStringsMap.instrumentDetail.iconType,
            'icon': translationStringsMap.relatedPublicationDetail.icon,
            'id': item.id
          };
          $rootScope.toolbarHeaderItems.push(searchItem, publicationItem);
        break;
        case 'search':
          searchItem = {};
          searchItem.translateString = translationStringsMap.search
          .translateString;
          searchItem.iconType = translationStringsMap.search.iconType;
          searchItem.icon = translationStringsMap.search.icon;
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
            'translateString': translationStringsMap.disclosure.translateString
          };
          $rootScope.toolbarHeaderItems.push(disclosureItem);
        break;
        case 'user-management':
          var managementItem = {
            'state': 'user-management',
            'translateString': translationStringsMap['user-management'].
            translateString
          };
          $rootScope.toolbarHeaderItems.push(managementItem);
        break;
        case 'user-management-detail':
          var userDetailItem = {
            'state': 'user-management-detail',
            'translateString': translationStringsMap['user-management-detail'].
            translateString
          };
          $rootScope.toolbarHeaderItems.push(userDetailItem);
        break;
        case 'login':
          var loginItem = {
            'state': 'login',
            'translateString': translationStringsMap.login.translateString
          };
          $rootScope.toolbarHeaderItems.push(loginItem);
        break;
        case 'metrics':
          var metricsItem = {
            'state': 'metrics',
            'translateString': translationStringsMap.metrics.translateString
          };
          $rootScope.toolbarHeaderItems.push(metricsItem);
        break;
        case 'health':
          var healthItem = {
            'state': 'health',
            'translateString': translationStringsMap.health.translateString
          };
          $rootScope.toolbarHeaderItems.push(healthItem);
        break;
        case 'configuration':
          var configItem = {
            'state': 'configuration',
            'translateString': translationStringsMap.configuration.
            translateString
          };
          $rootScope.toolbarHeaderItems.push(configItem);
        break;
        case 'logs':
          var logsItem = {
            'state': 'logs',
            'translateString': translationStringsMap.logs.translateString
          };
          $rootScope.toolbarHeaderItems.push(logsItem);
        break;
        case 'settings':
          var settingsItem = {
            'state': 'settings',
            'translateString': translationStringsMap.settings.translateString
          };
          $rootScope.toolbarHeaderItems.push(settingsItem);
        break;
        case 'password':
          var passwordItem = {
            'state': 'password',
            'translateString': translationStringsMap.password.translateString
          };
          $rootScope.toolbarHeaderItems.push(passwordItem);
        break;
        case 'register':
          var registerItem = {
            'state': 'register',
            'translateString': translationStringsMap.register.translateString
          };
          $rootScope.toolbarHeaderItems.push(registerItem);
        break;
        case 'activate':
          var activateItem = {
            'state': 'activate',
            'translateString': translationStringsMap.activate.translateString
          };
          $rootScope.toolbarHeaderItems.push(activateItem);
        break;
        case 'finishReset':
          var finishResetItem = {
            'state': 'finishReset',
            'translateString': translationStringsMap.finishReset.translateString
          };
          $rootScope.toolbarHeaderItems.push(finishResetItem);
        break;
        case 'requestReset':
          var requestResetItem = {
            'state': 'requestReset',
            'translateString': translationStringsMap.requestReset.
            translateString
          };
          $rootScope.toolbarHeaderItems.push(requestResetItem);
        break;
        case 'error':
          var errorItem = {
            'state': 'error',
            'translateString': translationStringsMap.error.translateString
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
