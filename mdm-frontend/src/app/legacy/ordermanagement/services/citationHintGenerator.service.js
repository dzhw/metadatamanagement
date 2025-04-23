'use strict';

angular.module('metadatamanagementApp').service('CitationHintGeneratorService', [
  '$interpolate', 'LanguageService', '$filter', '$rootScope', 
function($interpolate, LanguageService, $filter, $rootScope) {
  var Cite;
  if (!$rootScope.bowser.msie) {
    Cite = require('citation-js');
  }

  var mapPeopleToCiteJson = function(people) {
    var destination = [];
    people.forEach(function(person) {
      var destinationPerson = {
        given: person.firstName + (person.middleName ? ' ' +
          person.middleName : ''),
        family: person.lastName
      };
      destination.push(destinationPerson);
    });
    return destination;
  };

  var generateTitle = function(accessWay, dataPackage, currentLanguage) {
    var constantLabels = {
      surveyPeriod: {
        de: 'Datenerhebung: ',
        en: 'Data Collection: '
      },
      version: {
        de: 'Version: ',
        en: 'Version: '
      },
      accessWay: {
        de: 'Datenpaketzugangsweg: ',
        en: 'Data Package Access Way: '
      }
    };
    return dataPackage.title[currentLanguage] + '. ' +
      constantLabels.surveyPeriod[currentLanguage] +
      $filter('displayPeriod')(dataPackage.surveyPeriod) + '. ' +
      constantLabels.version[currentLanguage] +
      dataPackage.release.version + '. ' +
      constantLabels.accessWay[currentLanguage] +
      $filter('displayAccessWay')(accessWay);
  };

  var generateBibtex = function(accessWay, dataPackage) {
    if ($rootScope.bowser.msie) {
      throw 'citation.js is not compatible with IE11';
    }
    var currentLanguage = LanguageService.getCurrentInstantly();
    var citeJson = {
      title: generateTitle(accessWay, dataPackage, currentLanguage),
      type: 'dataset',
      DOI: dataPackage.doi,
      publisher: 'FDZ-DZHW',
      'publisher-place': currentLanguage === 'de' ? 'Hannover' : 'Hanover',
      issued: [{'date-parts': [
        new Date(dataPackage.release.firstDate).getFullYear()]}],
      author: mapPeopleToCiteJson(dataPackage.projectContributors),
      editor: mapPeopleToCiteJson(dataPackage.dataCurators),
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
      DOI: attachment.doi,
      publisher: attachment.citationDetails.institution,
      'publisher-place': attachment.citationDetails.location,
      issued: [{'date-parts': [attachment.citationDetails.publicationYear]}],
      author: mapPeopleToCiteJson(attachment.citationDetails.authors)
    };
    return new Cite(citeJson).format('biblatex') // use biblatex to include the doi and map fields back to bibtex names
      .replace('@report', '@techreport')
      .replace('date =', 'year =')
      .replace('location =', 'address =')
      .replace('publisher =', 'institution =')
      // remove spaces in latex code for umlauts
      .replace(/{\\.\s./g, function(match) {
        return match.replace(' ', '');
      });
  };

  var generateBibtexForInstrumentAttachment = function(citationDetails) {
    if ($rootScope.bowser.msie) {
      throw 'citation.js is not compatible with IE11';
    }
    var citeJson = {
      type: 'instrument',
      publisher: citationDetails.institution,
      'publisher-place': citationDetails.location,
      issued: [{'date-parts': [citationDetails.publicationYear]}],
      author: mapPeopleToCiteJson(citationDetails.authors)
    };
    return new Cite(citeJson).format('bibtex')
      // remove spaces in latex code for umlauts
      .replace(/{\\.\s./g, function(match) {
        return match.replace(' ', '');
      });
  };

  var generateCitationHint = function(accessWay, dataPackage) {
    var de = '{{dataPackage.projectContributors | displayPersons}} ' +
      '({{dataPackage.release.firstDate | date:"yyyy"}}). ' +
      '{{dataPackage.title.de}}. ' +
      'Datenerhebung: {{dataPackage.surveyPeriod | displayPeriod}}. ' +
      'Version: {{dataPackage.release.version}}. ' +
      'Datenpaketzugangsweg: {{accessWay | displayAccessWay: true}}. ' +
      'Hannover: FDZ-DZHW. ' +
      'Datenkuratierung: {{dataPackage.dataCurators | displayPersons}} ' +
      'https://doi.org/{{dataPackage.doi}}';
    var en = '{{dataPackage.projectContributors | displayPersons}} ' +
      '({{dataPackage.release.firstDate | date:"yyyy"}}). ' +
      '{{dataPackage.title.en}}. ' +
      'Data Collection: {{dataPackage.surveyPeriod | displayPeriod }}. ' +
      'Version: {{dataPackage.release.version}}. ' +
      'Data Package Access Way: {{accessWay | displayAccessWay}}. ' +
      'Hanover: FDZ-DZHW. ' +
      'Data Curation: {{dataPackage.dataCurators | displayPersons}} ' +
      'https://doi.org/{{dataPackage.doi}}';
    return {
      de: $interpolate(de)({accessWay: accessWay, dataPackage: dataPackage}),
      en: $interpolate(en)({accessWay: accessWay, dataPackage: dataPackage}),
    };
  };

  var generateCitationHintForAttachment = function(attachment) {
    var citationHint =
      '{{attachment.citationDetails.authors | displayPersons}} ' +
      '({{attachment.citationDetails.publicationYear}}). ' +
      '{{attachment.title}}. ' +
      '{{attachment.citationDetails.location}}: ' +
      '{{attachment.citationDetails.institution}}.';
    if (attachment.doi) {
      citationHint += ' ' + attachment.doi;
    }
    return $interpolate(citationHint)({ attachment });
  };

  var generateCitationHintForInstrumentAttachment = function(attachment, description) {
      var citationHint =
        '{{authors | displayPersons}} ' +
        '({{publicationYear}}). ' +
        '{{description}}. ' +
        '{{location}}: ' +
        '{{institution}}.';
      return $interpolate(citationHint)({
        authors: attachment.authors,
        publicationYear: attachment.publicationYear,
        description: description,
        location: attachment.location,
        institution: attachment.institution
      });
    };
    

  return {
    generateBibtex: generateBibtex,
    generateBibtexForAttachment: generateBibtexForAttachment,
    generateBibtexForInstrumentAttachment: generateBibtexForInstrumentAttachment,
    generateCitationHint: generateCitationHint,
    generateCitationHintForAttachment: generateCitationHintForAttachment,
    generateCitationHintForInstrumentAttachment: generateCitationHintForInstrumentAttachment
  };
}]);

