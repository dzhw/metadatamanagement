'use strict';

// service for updating the page title (used in toolbar and window.title)
angular.module('metadatamanagementApp').factory('PageMetadataService',
  function($rootScope, $transitions, $location) {
    $transitions.onExit({}, function() {
      setPageTitle();
      setPageDescription();
      setNextLink();
      setPreviousLink();
    });

    $transitions.onSuccess({}, function() {
      // set canonical link
      $rootScope.canonicalHref = $location.absUrl().replace(
        /\?.*/, '');
    });

    // set the page title to a new string
    var setPageTitle = function(titleKey, titleParams) {
      if (titleKey) {
        $rootScope.pageTitleKey = titleKey;
        $rootScope.pageTitleParams = titleParams;
      } else {
        $rootScope.pageTitleKey = null;
        $rootScope.pageTitleParams = null;
      }
    };

    var setPageDescription = function(descriptionKey, descriptionParams) {
      if (descriptionKey) {
        $rootScope.pageDescriptionKey = descriptionKey;
        $rootScope.pageDescriptionParams = descriptionParams;
      } else {
        $rootScope.pageDescriptionKey = null;
        $rootScope.pageDescriptionParams = null;
      }
    };

    var setPreviousLink = function(type) {
      if (type) {
        $rootScope.previousUiSref = '({type: "' + type  + '", page: 1})';
      } else {
        $rootScope.previousUiSref = null;
      }
    };

    var setNextLink = function(type) {
      if (type) {
        $rootScope.nextUiSref = '({type: "' + type  + '", page: 1})';
      } else {
        $rootScope.nextUiSref = null;
      }
    };

    var exports = {
      setPreviousLink: setPreviousLink,
      setNextLink: setNextLink,
      setPageTitle: setPageTitle,
      setPageDescription: setPageDescription
    };

    return exports;
  });
