'use strict';

// service for updating the page title (used in toolbar and window.title)
angular.module('metadatamanagementApp').factory('PageTitleService',
  function($rootScope, $window) {
    // init default page title
    var pageTitle = 'Metadatamanagement';

    // set the page title to a new string
    var setPageTitle = function(newPageTitle) {
      pageTitle = newPageTitle;
      $window.document.title = pageTitle;
      $rootScope.$broadcast('page-title-changed', pageTitle);
    };

    var getPageTitle = function() {
      return pageTitle;
    };

    var exports = {
      setPageTitle: setPageTitle,
      getPageTitle: getPageTitle
    };

    return exports;
  });
