/*global _*/
'use strict';

angular.module('metadatamanagementApp').factory('HistoryService',
  function(localStorageService) {
    var breadcrumb = {
      items: []
    };
    var getLastItems = function() {
      return JSON.parse(localStorageService.get('breadcrumb')).items;
    };
    var addItem = function(url, pageType) {
      if (!localStorageService.get('breadcrumb')) {
        localStorageService
        .set('breadcrumb', JSON.stringify(breadcrumb));
      } else {
        breadcrumb = JSON.parse(localStorageService.get('breadcrumb'));
      }
      if (_.last(breadcrumb.items)
      .url !== url) {
        if (breadcrumb.items.length >= 4) {
          breadcrumb.items = breadcrumb.items
          .slice(breadcrumb.items.length - 2, breadcrumb.items.length);
        }
        breadcrumb.items.push({'url': url,'pageType': pageType});
      }
      localStorageService
      .set('breadcrumb', JSON.stringify(breadcrumb));
    };
    return {
      getLastItems: getLastItems,
      addItem: addItem
    };
  });
