'use strict';

angular.module('metadatamanagementApp')
  .service('AnalysisPackageCitationHintGenerator',
    function($interpolate, LanguageService, $rootScope) {
      var Cite;
      if (!$rootScope.bowser.msie) {
        Cite = require('citation-js');
      }

      var mapPeopleToCiteJson = function(people) {
        var destination = [];
        people.forEach(function(person) {
          var destinationPerson = {
            given: person.firstName +
              (person.middleName ? ' ' + person.middleName : ''),
            family: person.lastName
          };
          destination.push(destinationPerson);
        });
        return destination;
      };

      var generateTitle = function(analysisPackage, currentLanguage) {
        var constantLabels = {
          version: {
            de: 'Version: ',
            en: 'Version: '
          }
        };
        return analysisPackage.title[currentLanguage] + '. ' +
          constantLabels.version[currentLanguage] +
          analysisPackage.release.version;
      };

      var generateBibtex = function(analysisPackage) {
        if ($rootScope.bowser.msie) {
          throw 'citation.js is not compatible with IE11';
        }
        var currentLanguage = LanguageService.getCurrentInstantly();
        var citeJson = {
          title: generateTitle(analysisPackage, currentLanguage),
          type: 'dataset',
          DOI: analysisPackage.doi,
          publisher: 'FDZ-DZHW',
          'publisher-place': currentLanguage === 'de' ? 'Hannover' : 'Hanover',
          issued: [{
            'date-parts': [
              new Date(analysisPackage.release.firstDate).getFullYear()]
          }],
          author: mapPeopleToCiteJson(analysisPackage.authors),
          editor: mapPeopleToCiteJson(analysisPackage.dataCurators)
        };
        var cite = new Cite(citeJson);
        // use biblatex to include the doi and map fields back to bibtex names
        return cite.format('biblatex')
          .replace('@dataset', '@misc')
          .replace('date =', 'year =')
          .replace('location =', 'address =')
          .replace('publisher =', 'institution =')
          // remove spaces in latex code for umlauts
          .replace(/{\\.\s./g, function(match) {
            return match.replace(' ', '');
          });
      };

      var generateBibtexForAttachment = function(attachment) {
        if ($rootScope.bowser.msie) {
          throw 'citation.js is not compatible with IE11';
        }
        var citeJson = {
          title: attachment.title,
          type: 'report',
          publisher: attachment.citationDetails.institution,
          'publisher-place': attachment.citationDetails.location,
          issued: [{'date-parts': [attachment.citationDetails
              .publicationYear]}],
          author: mapPeopleToCiteJson(attachment.citationDetails.authors)
        };
        return new Cite(citeJson).format('bibtex')
          // remove spaces in latex code for umlauts
          .replace(/{\\.\s./g, function(match) {
            return match.replace(' ', '');
          });
      };

      var generateCitationHint = function(analysisPackage) {
        var de = '{{analysisPackage.authors | displayPersons}} ' +
          '({{analysisPackage.release.firstDate | date:"yyyy"}}). ' +
          '{{analysisPackage.title.de}}. ' +
          'Version: {{analysisPackage.release.version}}. ' +
          'Hannover: FDZ-DZHW. ' +
          'Datenkuratierung: ' +
          '{{analysisPackage.dataCurators | displayPersons}} ' +
          'https://doi.org/{{analysisPackage.doi}}';
        var en = '{{analysisPackage.authors | displayPersons}} ' +
          '({{analysisPackage.release.firstDate | date:"yyyy"}}). ' +
          '{{analysisPackage.title.en}}. ' +
          'Version: {{analysisPackage.release.version}}. ' +
          'Hanover: FDZ-DZHW. ' +
          'Data Curation: {{analysisPackage.dataCurators | displayPersons}} ' +
          'https://doi.org/{{analysisPackage.doi}}';
        return {
          de: $interpolate(de)({analysisPackage: analysisPackage}),
          en: $interpolate(en)({analysisPackage: analysisPackage}),
        };
      };

      var generateCitationHintForAttachment = function(attachment) {
        var citationHint =
          '{{attachment.citationDetails.authors | displayPersons}} ' +
          '({{attachment.citationDetails.publicationYear}}). ' +
          '{{attachment.title}}. ' +
          '{{attachment.citationDetails.location}}: ' +
          '{{attachment.citationDetails.institution}}.';
        return $interpolate(citationHint)({attachment: attachment});
      };

      return {
        generateBibtex: generateBibtex,
        generateBibtexForAttachment: generateBibtexForAttachment,
        generateCitationHint: generateCitationHint,
        generateCitationHintForAttachment: generateCitationHintForAttachment
      };
    });
