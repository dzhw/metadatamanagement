/*global _*/
'use strict';

angular.module('metadatamanagementApp').factory('BreadCrumbService',
  function($translate, $rootScope) {
    $rootScope.breadCrumbItems = [];
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
          breadCrumbItem = {
            'url': url,
            'pageType': $translate
            .instant('disclosure.title')
          };
        break;
        default:
          if (params.type) {
            breadCrumbItem = {
              'url': url,
              'pageType': $translate.instant('search-management.detail.search'),
              'id': $translate.instant('search-management.tabs.' + params.type)
            };
          } else {
            $rootScope.breadCrumbItems = [];
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
      var items;
      if ($rootScope.breadCrumbItems.length > 0) {
        if (_.last($rootScope.breadCrumbItems)
        .url !== url) {
          /* should be refined */
          if ($rootScope.breadCrumbItems.length >= 3) {
            $rootScope.breadCrumbItems = $rootScope.breadCrumbItems
            .slice($rootScope.breadCrumbItems.length - 2,
              $rootScope.breadCrumbItems.length);
          }
          items = createNewBreadCrumbItem(url, params, pathTemplate);
          $rootScope.breadCrumbItems.push(items);
        }
      } else {
        items = createNewBreadCrumbItem(url, params, pathTemplate);
        $rootScope.breadCrumbItems.push(items);
      }
    };
    return {
      addToBreadCrumb: addToBreadCrumb
    };
  });
