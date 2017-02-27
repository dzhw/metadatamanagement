/*global _*/
'use strict';

angular.module('metadatamanagementApp').factory('ToolbarHeaderService',
  function($rootScope) {
    var searchItem;
    var translationStringsMap = {
      'questionDetail': {
        'translateString': 'question-management.detail.question'
      },
      'variableDetail': {
        'translateString': 'variable-management.detail.variable'
      },
      'instrumentDetail': {
        'translateString': 'instrument-management.detail.instrument'
      },
      'dataSetDetail': {
        'translateString': 'data-set-management.detail.data-set'
      },
      'disclosure': {
        'translateString': 'disclosure.title'
      },
      'relatedPublicationDetail': {
        'translateString': 'related-publication-management.detail.publication'
      },
      'search': {
        'translateString': 'search-management.detail.search',
        'tab': 'search-management.tabs.'
      },
      'studyDetail': {
        'translateString': 'study-management.detail.study'
      },
      'surveyDetail': {
        'translateString': 'survey-management.detail.survey'
      },
      'user-management': {
        'translateString': 'global.menu.admin.user-management'
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
      }
    };
    var updateToolbarHeader = function(item) {
      $rootScope.breadCrumbItems = [];
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
      if (item.studyId) {
        studyItem = {
          'state': 'studyDetail({"id":"' + item.studyId + '"})',
          'translateString': translationStringsMap.studyDetail.
          translateString,
          'id': item.studyId
        };
      }
      switch (item.stateName) {
        case 'questionDetail':
          $rootScope.breadCrumbItems.push(searchItem);
          instrumentItem = {
            'state': 'instrumentDetail({"id":"' + item.instrumentId + '"})',
            'translateString': translationStringsMap.instrumentDetail.
            translateString,
            'id': item.instrumentId
          };
          questionItem = {
            'state': 'questionDetail',
            'translateString': translationStringsMap.questionDetail.
            translateString,
            'id': item.id
          };
          $rootScope.breadCrumbItems.push(studyItem, instrumentItem,
            questionItem);
        break;
        case 'variableDetail':
          $rootScope.breadCrumbItems.push(searchItem);
          dataSetItem = {
            'state': 'dataSetDetail({"id":"' + item.dataSetId + '"})',
            'translateString': translationStringsMap.dataSetDetail.
             translateString,
            'id': item.dataSetId
          };
          variableItem = {
            'state': 'variableDetail',
            'translateString': translationStringsMap.variableDetail.
             translateString,
            'id': item.id
          };
          $rootScope.breadCrumbItems.push(studyItem, dataSetItem, variableItem);
        break;
        case 'instrumentDetail':
          $rootScope.breadCrumbItems.push(searchItem);
          instrumentItem = {
            'state': 'instrumentDetail',
            'translateString': translationStringsMap.instrumentDetail.
            translateString,
            'id': item.id
          };
          $rootScope.breadCrumbItems.push(studyItem, instrumentItem);
        break;
        case 'dataSetDetail':
          $rootScope.breadCrumbItems.push(searchItem);
          dataSetItem = {
            'state': 'dataSetDetail',
            'translateString': translationStringsMap.dataSetDetail.
            translateString,
            'id': item.id
          };
          $rootScope.breadCrumbItems.push(studyItem, dataSetItem);
        break;
        case 'relatedPublicationDetail':
          $rootScope.breadCrumbItems.push(searchItem);
          publicationItem = {
            'state': 'relatedPublicationDetail',
            'translateString': translationStringsMap.relatedPublicationDetail.
            translateString,
            'id': item.id
          };
          $rootScope.breadCrumbItems.push(publicationItem);
        break;
        case 'search':
          searchItem = {};
          searchItem.translateString = translationStringsMap.search
          .translateString;
          if (_.size(item.searchParams) > 1) {
            searchItem.tabName = item.tabName;
          } else {
            searchItem.tabName = 'search-management.tabs.all';
          }
          searchItem.state = 'search(' + JSON.stringify(item.searchParams) +
          ')';
          $rootScope.breadCrumbItems.push(searchItem);
        break;
        case 'studyDetail':
          $rootScope.breadCrumbItems.push(searchItem);
          studyItem = {
            'state': 'studyDetail',
            'translateString': translationStringsMap.studyDetail.
            translateString,
            'id': item.id
          };
          $rootScope.breadCrumbItems.push(studyItem);
        break;
        case 'surveyDetail':
          $rootScope.breadCrumbItems.push(searchItem);
          surveyItem = {
            'state': 'surveyDetail',
            'translateString': translationStringsMap.surveyDetail.
            translateString,
            'id': item.id
          };
          $rootScope.breadCrumbItems.push(surveyItem);
        break;
        default:
          console.log(item.stateName + ': coming...');
        break;
      }
    };
    return {
      updateToolbarHeader: updateToolbarHeader
    };
  });
