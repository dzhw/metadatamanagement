'use strict';

// service for updating the page title (used in toolbar and window.title)
angular.module('metadatamanagementApp').factory('PageMetadataService',
  function($rootScope, $transitions, $location, $analytics, $timeout, $window) {
    $transitions.onExit({}, function() {
      setPageTitle();
      setPageDescription();
      setNextLink();
      setPreviousLink();
      setDublinCoreMetadata();
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
        // wait one digest cycle until page title is rendered
        $timeout(function() {
          $analytics.pageTrack($location.absUrl(), $location);
        });
        $timeout(function() {
          // let matomo/piwik scan for outgoing links
          if ($window._paq) {
            $window._paq.push(['enableLinkTracking']);
          }
        }, 500);
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

    var setDublinCoreMetadata = function(dataPackage) {
      if (dataPackage && dataPackage.release) {
        $rootScope.dublinCoreMetadata = {
          type: 'Dataset',
          title: dataPackage.title,
          identifier: 'https://doi.org/' + dataPackage.doi,
          description: dataPackage.description,
          creators: dataPackage.projectContributors,
          contributors: dataPackage.dataCurators,
          date: dataPackage.release.firstDate,
          subjects: dataPackage.tags,
          publisher: 'FDZ-DZHW',
          coverage: {
            period: dataPackage.surveyPeriod,
            countries: dataPackage.surveyCountries
          },
          languages: dataPackage.dataLanguages,
          rights: {
            de: 'Beantragung notwendig unter ' +
              'https://metadata.fdz.dzhw.eu/de/data-packages/' +
              dataPackage.masterId + '?version=' + dataPackage.release.version,
            en: 'Application necessary under ' +
              'https://metadata.fdz.dzhw.eu/en/data-packages/' +
              dataPackage.masterId + '?version=' + dataPackage.release.version
          }
        };
      } else {
        $rootScope.dublinCoreMetadata = null;
      }
    };

    var exports = {
      setPreviousLink: setPreviousLink,
      setNextLink: setNextLink,
      setPageTitle: setPageTitle,
      setPageDescription: setPageDescription,
      setDublinCoreMetadata: setDublinCoreMetadata
    };

    return exports;
  });
