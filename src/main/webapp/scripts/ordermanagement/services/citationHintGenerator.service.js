'use strict';

angular.module('metadatamanagementApp').service('CitationHintGeneratorService',
function($interpolate) {
  var generateCitationHint = function(accessWay, dataPackage) {
    var de = '{{dataPackage.projectContributors | displayPersons}} ' +
      '({{dataPackage.release.firstDate | date:"yyyy"}}). ' +
      '{{dataPackage.title.de}}. ' +
      'Datenerhebung: {{dataPackage.surveyPeriod | displayPeriod}}. ' +
      'Version: {{dataPackage.release.version}}. ' +
      'Datenpaketzugangsweg: {{accessWay | displayAccessWay}}. ' +
      'Hannover: FDZ-DZHW. ' +
      'Datenkuratierung: {{dataPackage.dataCurators | displayPersons}} ' +
      'doi: {{dataPackage.doi}}';
    var en = '{{dataPackage.projectContributors | displayPersons}} ' +
      '({{dataPackage.release.firstDate | date:"yyyy"}}). ' +
      '{{dataPackage.title.en}}. ' +
      'Data Collection: {{dataPackage.surveyPeriod | displayPeriod }}. ' +
      'Version: {{dataPackage.release.version}}. ' +
      'Data Package Access Way: {{accessWay | displayAccessWay}}. ' +
      'Hanover: FDZ-DZHW. ' +
      'Data Curation: {{dataPackage.dataCurators | displayPersons}} ' +
      'doi: {{dataPackage.doi}}';
    return {
      de: $interpolate(de)({accessWay: accessWay, dataPackage: dataPackage}),
      en: $interpolate(en)({accessWay: accessWay, dataPackage: dataPackage}),
    };
  };

  return {
    generateCitationHint: generateCitationHint
  };
});
