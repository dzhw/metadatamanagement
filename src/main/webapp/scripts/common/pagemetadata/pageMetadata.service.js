'use strict';

// service for updating the page title (used in toolbar and window.title)
angular.module('metadatamanagementApp').factory('PageMetadataService',
  function($rootScope, $transitions, $location, $analytics, $timeout, $window,
    $sce, LanguageService, $filter) {
    $transitions.onExit({}, function() {
      setPageTitle();
      setPageDescription();
      setNextLink();
      setPreviousLink();
      setDublinCoreMetadata();
      setSchemaOrgMetadata();
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
          identifier: dataPackage.doi ?
            'https://doi.org/' + dataPackage.doi : undefined,
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
            de: 'Beantragung notwendig unter ' + $rootScope.baseUrl +
              '/de/data-packages/' +
              dataPackage.masterId + '?version=' + dataPackage.release.version,
            en: 'Application necessary under ' + $rootScope.baseUrl +
              '/en/data-packages/' +
              dataPackage.masterId + '?version=' + dataPackage.release.version
          }
        };
      } else {
        $rootScope.dublinCoreMetadata = null;
      }
    };

    var getI18nStringInOtherLanguage = function(i18nString, currentLanguage) {
      if (currentLanguage === 'en') {
        return i18nString.de;
      } else {
        return i18nString.en;
      }
    };

    var mapPersonToSchemaOrg = function(person) {
      return {
        '@type': 'Person',
        'sameAs': person.orcid,
        'givenName': person.firstName,
        'familyName': person.lastName,
        'name': person.firstName + ' ' +
          (person.middleName ? person.middleName + ' ' : '') +
          person.lastName
      };
    };

    var mapOrganizationToSchemaOrg = function(organization, language) {
      var organizationName;
      if (organization[language]) {
        organizationName = organization[language];
      } else {
        organizationName = getI18nStringInOtherLanguage(organization);
      }
      return {
        name: organizationName,
        '@type': 'Organization'
      };
    };

    var formatDate = function(date) {
      return $filter('date')(date, 'yyyy-MM-dd');
    };

    var setSchemaOrgMetadata = function(dataPackage) {
      if (dataPackage && dataPackage.release) {
        var language = LanguageService.getCurrentInstantly();
        var schemaOrgMetadata = {
          '@context': 'https://schema.org/',
          '@type': 'Dataset',
          '@language': language,
          'name': dataPackage.title[language] ?
            dataPackage.title[language] :
            getI18nStringInOtherLanguage(dataPackage.title),
          'description': dataPackage.description[language] ?
            dataPackage.description[language] :
            getI18nStringInOtherLanguage(dataPackage.description),
          'url': $rootScope.baseUrl + '/' + language + '/data-packages/' +
            dataPackage.masterId + '?version=' + dataPackage.release.version,
          'sameAs': $rootScope.baseUrl + '/en/data-packages/' +
            dataPackage.masterId,
          'version': dataPackage.release.version,
          'identifier': dataPackage.doi ?
            'https://doi.org/' + dataPackage.doi : undefined,
          'isAccessibleForFree': true,
          'datePublished': formatDate(dataPackage.release.firstDate),
          'dateModified': formatDate(dataPackage.release.lastDate)
        };
        if (dataPackage.tags) {
          schemaOrgMetadata.keywords = dataPackage.tags[language];
        }
        if (dataPackage.surveyCountries) {
          schemaOrgMetadata.spatialCoverage =
            dataPackage.surveyCountries.map(function(country) {
              return country[language];
            });
        }
        if (dataPackage.surveyPeriod) {
          schemaOrgMetadata.temporalCoverage =
            formatDate(dataPackage.surveyPeriod.start) +
            '/' +
            formatDate(dataPackage.surveyPeriod.end);
        }
        if (dataPackage.sponsors) {
          schemaOrgMetadata.funder = dataPackage.sponsors.map(
            function(sponsor) {
              return mapOrganizationToSchemaOrg(sponsor, language);
            });
          schemaOrgMetadata.creator = dataPackage.projectContributors.map(
            mapPersonToSchemaOrg);
          schemaOrgMetadata.creator = schemaOrgMetadata.creator.concat(
            dataPackage.institutions.map(function(institution) {
              return mapOrganizationToSchemaOrg(institution, language);
            }));
          schemaOrgMetadata.contributor = dataPackage.dataCurators.map(
            mapPersonToSchemaOrg);
          schemaOrgMetadata.publisher = {
            name: 'FDZ-DZHW',
            '@type': 'Organization'
          };
        }
        $rootScope.schemaOrgMetadata = $sce.trustAsHtml(angular.toJson(
          schemaOrgMetadata));
      } else {
        $rootScope.schemaOrgMetadata = null;
      }
    };

    var exports = {
      setPreviousLink: setPreviousLink,
      setNextLink: setNextLink,
      setPageTitle: setPageTitle,
      setPageDescription: setPageDescription,
      setDublinCoreMetadata: setDublinCoreMetadata,
      setSchemaOrgMetadata: setSchemaOrgMetadata
    };

    return exports;
  });
