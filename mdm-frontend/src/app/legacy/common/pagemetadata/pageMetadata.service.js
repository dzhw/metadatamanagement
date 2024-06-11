'use strict';

// service for updating the page title (used in toolbar and window.title)
angular.module('metadatamanagementApp').factory('PageMetadataService', [
  '$rootScope', '$transitions', '$location', '$analytics', '$timeout', '$window', '$sce', 'LanguageService', '$filter', 
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
        $rootScope.previousUiSref = '({type: "' + type + '", page: 1})';
      } else {
        $rootScope.previousUiSref = null;
      }
    };

    var setNextLink = function(type) {
      if (type) {
        $rootScope.nextUiSref = '({type: "' + type + '", page: 1})';
      } else {
        $rootScope.nextUiSref = null;
      }
    };
    var setDublinCoreMetadata = function(dataPackage) {
      var language = LanguageService.getCurrentInstantly();
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
          rights: getLicenseDescription(dataPackage)
        };
        $rootScope.dublinCoreMetadata.institutions =
          dataPackage.institutions.map(function(institution) {
            if (institution[language]) {
              return institution[language];
            } else {
              return getI18nStringInOtherLanguage(institution, language);
            }
          });
      } else {
        $rootScope.dublinCoreMetadata = null;
      }
    };

    var setDublinCoreMetadataForAP = function(analysisPackage) {
      var language = LanguageService.getCurrentInstantly();
      if (analysisPackage && analysisPackage.release) {
        $rootScope.dublinCoreMetadata = {
          type: 'Software',
          title: analysisPackage.title,
          identifier: analysisPackage.doi ?
            'https://doi.org/' + analysisPackage.doi : undefined,
          description: analysisPackage.description,
          creators: analysisPackage.authors,
          contributors: analysisPackage.dataCurators,
          date: analysisPackage.release.firstDate,
          subjects: analysisPackage.tags,
          publisher: 'FDZ-DZHW',
          rights: $rootScope.baseUrl + '/' + language +
            '/analysis-packages/' +
            analysisPackage.masterId + '?version=' +
            analysisPackage.release.version
        };
        if (analysisPackage.institutions) {
          $rootScope.dublinCoreMetadata.institutions =
            analysisPackage.institutions.map(function(institution) {
              if (institution[language]) {
                return institution[language];
              } else {
                return getI18nStringInOtherLanguage(institution, language);
              }
            });
        }

      } else {
        $rootScope.dublinCoreMetadata = null;
      }
    };

    var getLicenseDescription = function(dataPackage) {
      return {
        de: 'Beantragung notwendig unter ' + $rootScope.baseUrl +
          '/de/data-packages/' +
          dataPackage.masterId + '?version=' + dataPackage.release.version,
        en: 'Application necessary under ' + $rootScope.baseUrl +
          '/en/data-packages/' +
          dataPackage.masterId + '?version=' + dataPackage.release.version
      };
    };

    var getLicenseName = function() {
      return {
        de: 'Nutzung nach Beantragung',
        en: 'Usage after application'
      };
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
        'sameAs':
          person.orcid ? 'https://orcid.org/' + person.orcid : undefined,
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
    var mapSoftwarePackagesToSchemaOrg = function(scripts) {
      var result = scripts.map(function(item) {
        return item.softwarePackage;
      });
      return result.toString();
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
          'dateModified': formatDate(dataPackage.release.lastDate),
          'license': {
            '@type': 'CreativeWork',
            'name': getLicenseName()[language],
            'description': getLicenseDescription(dataPackage)[language]
          }
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
        if (dataPackage.surveyDataTypes) {
          schemaOrgMetadata.additionalType = dataPackage.surveyDataTypes.map(
            function(dataType) {
              return dataType[language];
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
        }
        if (dataPackage.projectContributors) {
          schemaOrgMetadata.creator = dataPackage.projectContributors.map(
            mapPersonToSchemaOrg);
        }
        if (dataPackage.institutions) {
          schemaOrgMetadata.creator = schemaOrgMetadata.creator || [];
          schemaOrgMetadata.creator = schemaOrgMetadata.creator.concat(
            dataPackage.institutions.map(function(institution) {
              return mapOrganizationToSchemaOrg(institution, language);
            }));
        }
        if (dataPackage.dataCurators) {
          schemaOrgMetadata.contributor = dataPackage.dataCurators.map(
            mapPersonToSchemaOrg);
        }
        schemaOrgMetadata.publisher = {
          name: 'FDZ-DZHW',
          '@type': 'Organization'
        };

        $rootScope.schemaOrgMetadata = $sce.trustAsHtml(angular.toJson(
          schemaOrgMetadata));
      } else {
        $rootScope.schemaOrgMetadata = null;
      }
    };

    var setSchemaOrgMetadataForAP = function(analysisPackage) {
      if (analysisPackage && analysisPackage.release) {
        var language = LanguageService.getCurrentInstantly();
        var schemaOrgMetadata = {
          '@context': 'https://schema.org/',
          '@type': 'SoftwareSourceCode',
          '@language': language,
          'name': analysisPackage.title[language] ?
            analysisPackage.title[language] :
            getI18nStringInOtherLanguage(analysisPackage.title),
          'description': analysisPackage.description[language] ?
            analysisPackage.description[language] :
            getI18nStringInOtherLanguage(analysisPackage.description),
          'url': $rootScope.baseUrl + '/' + language + '/analysis-packages/' +
            analysisPackage.masterId + '?version=' +
            analysisPackage.release.version,
          'sameAs': $rootScope.baseUrl + '/en' + '/analysis-packages/' +
            analysisPackage.masterId,
          'version': analysisPackage.release.version,
          'identifier': analysisPackage.doi ?
            'https://doi.org/' + analysisPackage.doi : undefined,
          'isAccessibleForFree': true,
          'datePublished': formatDate(analysisPackage.release.firstDate),
          'dateModified': formatDate(analysisPackage.release.lastDate),
          'license': {
            '@type': 'CreativeWork',
            'description': $rootScope.baseUrl + '/' + language +
              '/analysis-packages/' +
              analysisPackage.masterId + '?version=' +
              analysisPackage.release.version
          }
        };
        if (analysisPackage.authors) {
          schemaOrgMetadata.creator = analysisPackage.authors.map(
            mapPersonToSchemaOrg);
        }
        if (analysisPackage.institutions) {
          schemaOrgMetadata.creator = schemaOrgMetadata.creator || [];
          schemaOrgMetadata.creator = schemaOrgMetadata.creator.concat(
            analysisPackage.institutions.map(function(institution) {
              return mapOrganizationToSchemaOrg(institution, language);
            }));
        }
        schemaOrgMetadata.runtimePlatform =
          mapSoftwarePackagesToSchemaOrg(analysisPackage.scripts);
        if (analysisPackage.tags) {
          schemaOrgMetadata.keywords = analysisPackage.tags[language];
        }
        if (analysisPackage.sponsors) {
          schemaOrgMetadata.funder = analysisPackage.sponsors.map(
            function(sponsor) {
              return mapOrganizationToSchemaOrg(sponsor, language);
            });
        }
        if (analysisPackage.dataCurators) {
          schemaOrgMetadata.contributor = analysisPackage.dataCurators.map(
            mapPersonToSchemaOrg);
        }
        schemaOrgMetadata.publisher = {
          name: 'FDZ-DZHW',
          '@type': 'Organization'
        };
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
      setDublinCoreMetadataForAP: setDublinCoreMetadataForAP,
      setSchemaOrgMetadata: setSchemaOrgMetadata,
      setSchemaOrgMetadataForAP: setSchemaOrgMetadataForAP
    };

    return exports;
  }]);

