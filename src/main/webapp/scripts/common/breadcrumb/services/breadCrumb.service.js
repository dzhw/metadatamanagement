/*global _*/
'use strict';

angular.module('metadatamanagementApp').factory('BreadCrumbService',
  function($rootScope) {
    $rootScope.breadCrumbItems = [];
    var items = [];
    var createNewBreadCrumbItem = function(url, params, pathTemplate) {
      var breadCrumbItem = '';
      switch (pathTemplate) {
        case '/surveys/{id}':
          breadCrumbItem = {
            'url': url,
            'pageType': 'survey-management.detail.survey',
            'id': params.id
          };
        break;
        case '/variables/{id}':
          breadCrumbItem = {
            'url': url,
            'pageType': 'variable-management.detail.variable',
            'id': params.id
          };
        break;
        case '/questions/{id}':
          breadCrumbItem = {
            'url': url,
            'pageType': 'question-management.detail.question',
            'id': params.id
          };
        break;
        case '/instruments/{id}':
          breadCrumbItem = {
            'url': url,
            'pageType': 'instrument-management.detail.instrument',
            'id': params.id
          };
        break;
        case '/data-sets/{id}':
          breadCrumbItem = {
            'url': url,
            'pageType': 'data-set-management.detail.data-set',
            'id': params.id
          };
        break;
        case '/publications/{id}':
          breadCrumbItem = {
            'url': url,
            'pageType': 'related-publication-management.detail.publication',
            'id': params.id
          };
        break;
        case '/studies/{id}':
          breadCrumbItem = {
            'url': url,
            'pageType': 'study-management.detail.study',
            'id': params.id
          };
        break;
        case '/disclosure':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': 'disclosure.title'
          };
        break;

        case '/user-management':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': 'global.menu.admin.user-management'
          };
        break;
        case '/metrics':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': 'global.menu.admin.metrics'
          };
        break;
        case '/health':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': 'global.menu.admin.health'
          };
        break;
        case '/configuration':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': 'global.menu.admin.configuration'
          };
        break;
        case '/logs':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': 'global.menu.admin.logs'
          };
        break;
        case '/settings':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': 'global.menu.account.settings'
          };
        break;
        case '/password':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': 'global.menu.account.password'
          };
        break;
        case '/login':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': 'global.toolbar.buttons.login'
          };
        break;
        case '/register':
          items = [];
          breadCrumbItem = {
            'url': url,
            'pageType': 'global.toolbar.buttons.register'
          };
        break;
        default:
          if (_.size(params) < 3) {
            items = [];
          }
          if (params.type) {
            breadCrumbItem = {
              'url': url,
              'pageType': 'search-management.detail.search',
              'tab': 'search-management.tabs.' + params.type
            };
          } else {
            items = [];
            breadCrumbItem = {
              'url': url,
              'pageType': 'search-management.detail.search',
              'tab': 'search-management.tabs.all'
            };
          }
        break;
      }
      return breadCrumbItem;
    };
    var generateUrlObject = function(url) {
      var urlObject = {};
      if ($rootScope.currentLanguage === 'de') {
        urlObject.de = url;
        urlObject.en = _.replace(url, '/#!/de/' , '/#!/en/');
      }else {
        urlObject.en = url;
        urlObject.de = _.replace(url, '/#!/en/' , '/#!/de/');
      }
      return urlObject;
    };
    var addToBreadCrumb = function(url, params, pathTemplate) {
        var index = _.findIndex(items, function(item) {
          return item.url.de === url || item.url.en === url;
        });
        if (index >= 0) {
          items = items.slice(0, index);
        }
        if (items.length >= 20) {
          items = items.slice(0, items.length - 1);
        }
        var item = createNewBreadCrumbItem(generateUrlObject(url),
        params, pathTemplate);
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
