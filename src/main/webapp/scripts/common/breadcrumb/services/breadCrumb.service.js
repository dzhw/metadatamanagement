/*global _*/
'use strict';

angular.module('metadatamanagementApp').factory('BreadCrumbService',
  function($translate, $rootScope) {
    $rootScope.breadCrumbItems = [];
    var items = [];
    var createNewBreadCrumbItem = function(url, params, pathTemplate) {
      var breadCrumbItem = '';
      switch (pathTemplate) {
        case '/studies/{projectId}/surveys/{surveyNumber}':
          breadCrumbItem = {
            'url': url,
            'pageType': $translate.instant('survey-management.detail.survey'),
            'id': params.projectId + '-sy' + params.surveyNumber
          };
        break;
        case '/studies/{projectId}/data-sets/' +
        '{dataSetNumber}/variables/{variableName}':
          breadCrumbItem = {
            'url': url,
            'pageType': $translate
            .instant('variable-management.detail.variable'),
            'id': params.projectId + '-ds' +
             params.dataSetNumber + '-' + params.variableName
          };
        break;
        case '/studies/{projectId}/instruments/' +
        '{instrumentNumber}/questions/{questionNumber}':
          breadCrumbItem = {
            'url': url,
            'pageType': $translate
            .instant('question-management.detail.question'),
            'id': params.projectId + '-ins' + params.instrumentNumber + '-' +
            params.questionNumber
          };
        break;
        case '/studies/{projectId}/instruments/{instrumentNumber}':
          breadCrumbItem = {
            'url': url,
            'pageType': $translate
            .instant('instrument-management.detail.instrument'),
            'id': params.projectId + '-ins' + params.instrumentNumber
          };
        break;
        case '/studies/{projectId}/data-sets/{dataSetNumber}':
          breadCrumbItem = {
            'url': url,
            'pageType': $translate
            .instant('data-set-management.detail.data-set'),
            'id': params.projectId + '-ds' + params.dataSetNumber
          };
        break;
        case '/publications/{id}':
          breadCrumbItem = {
            'url': url,
            'pageType': $translate
            .instant('related-publication-management.detail.publication'),
            'id': params.id
          };
        break;
        case '/studies/{id}':
          breadCrumbItem = {
            'url': url,
            'pageType': $translate.instant('study-management.detail.study'),
            'id': params.id
          };
        break;
        case '/disclosure':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': $translate.instant('disclosure.title')
          };
        break;

        case '/user-management':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': $translate.instant('global.menu.admin.user-management')
          };
        break;
        case '/metrics':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': $translate.instant('global.menu.admin.metrics')
          };
        break;
        case '/health':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': $translate.instant('global.menu.admin.health')
          };
        break;
        case '/configuration':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': $translate.instant('global.menu.admin.configuration')
          };
        break;
        case '/logs':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': $translate.instant('global.menu.admin.logs')
          };
        break;
        case '/settings':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': $translate.instant('global.menu.account.settings')
          };
        break;
        case '/password':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': $translate.instant('global.menu.account.password')
          };
        break;
        case '/login':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': $translate.instant('global.toolbar.buttons.login')
          };
        break;
        case '/register':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': $translate.instant('global.toolbar.buttons.register')
          };
        break;
        default:
          items = [];
          if (params.type) {
            breadCrumbItem = {
              'url': url,
              'pageType': $translate.instant('search-management.detail.search'),
              'id': $translate.instant('search-management.tabs.' + params.type)
            };
          } else {
            breadCrumbItem = {
              'url': url,
              'pageType': $translate.instant('search-management.detail.search'),
              'id': $translate.instant('search-management.tabs.all')
            };
          }
        break;
      }
      return breadCrumbItem;
    };
    var addToBreadCrumb = function(url, params, pathTemplate) {
      var index = _.findIndex(items, function(item) {
        return item.url === url;
      });
      if (index >= 0) {
        items = items.slice(0, index);
      }
      if (items.length >= 20) {
        items = items.slice(0, items.length - 1);
      }
      var item = createNewBreadCrumbItem(url, params, pathTemplate);
      items.push(item);
      if (items.length >= 3) {
        $rootScope.breadCrumbItems = items
        .slice(items.length - 3, items.length);
      } else {
        $rootScope.breadCrumbItems = items;
      }
    };
    return {
      addToBreadCrumb: addToBreadCrumb
    };
  });
