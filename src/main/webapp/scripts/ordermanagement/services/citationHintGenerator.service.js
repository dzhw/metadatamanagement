'use strict';

angular.module('metadatamanagementApp').service('CitationHintGeneratorService',
function($interpolate) {
  var generateCitationHint = function(accessWay, study) {
    var de = '{{study.authors | displayPersons}} ' +
      '({{study.release.firstDate | date:"yyyy"}}). ' +
      '{{study.title.de}}. ' +
      'Datenerhebung: {{study.surveyPeriod | displayPeriod}}. ' +
      'Version: {{study.release.version}}. ' +
      'Datenpaketzugangsweg: {{accessWay | displayAccessWay}}. ' +
      'Hannover: FDZ-DZHW. ' +
      'Datenkuratierung: {{study.authors | displayPersons}}. ' +
      'doi: {{study.doi}}';
    var en = '{{study.authors | displayPersons}} ' +
      '({{study.release.firstDate | date:"yyyy"}}). ' +
      '{{study.title.en}}. ' +
      'Data Collection: {{study.surveyPeriod | displayPeriod }}. ' +
      'Version: {{study.release.version}}. ' +
      'Data Package Access Way: {{accessWay | displayAccessWay}}. ' +
      'Hanover: FDZ-DZHW. ' +
      'Data Curation: {{study.authors | displayPersons}}. ' +
      'doi: {{study.doi}}';
    return {
      de: $interpolate(de)({accessWay: accessWay, study: study}),
      en: $interpolate(en)({accessWay: accessWay, study: study}),
    };
  };

  return {
    generateCitationHint: generateCitationHint
  };
});
