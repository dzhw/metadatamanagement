'use strict';

// service for updating the page title (used in toolbar and window.title)
angular.module('metadatamanagementApp').factory('PageTitleService',
  function($rootScope) {
    // set the page title to a new string
    var setPageTitle = function(titleKey, titleParams) {
      $rootScope.pageTitleKey = titleKey;
      $rootScope.pageTitleParams = titleParams;
    };

    var exports = {
      setPageTitle: setPageTitle
    };

    return exports;
  });
