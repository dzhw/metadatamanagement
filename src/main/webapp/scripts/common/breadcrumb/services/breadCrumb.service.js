/*global _*/
'use strict';

angular.module('metadatamanagementApp').factory('HistoryService',
  function(localStorageService) {
    var breadCrumb = {
      items: []
    };
    var getLastItems = function() {
      return JSON.parse(localStorageService.get('breadCrumb')).items;
    };
    var addItem = function(url, pageType) {
      if (!localStorageService.get('breadCrumb')) {
        localStorageService
        .set('breadCrumb', JSON.stringify(breadCrumb));
      } else {
        breadCrumb = JSON.parse(localStorageService.get('breadCrumb'));
      }
      if (breadCrumb.items.length > 0) {
        if (_.last(breadCrumb.items)
        .url !== url) {
          if (breadCrumb.items.length >= 4) {
            breadCrumb.items = breadCrumb.items
            .slice(breadCrumb.items.length - 2, breadCrumb.items.length);
          }
          breadCrumb.items.push({'url': url,'pageType': pageType});
        }
      } else {
        breadCrumb.items.push({'url': url,'pageType': pageType});
      }
      localStorageService
      .set('breadCrumb', JSON.stringify(breadCrumb));
    };
    return {
      getLastItems: getLastItems,
      addItem: addItem
    };
  });
