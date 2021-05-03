'use strict';
angular.module('metadatamanagementApp')
  .controller('DataPackageCitationDialogController',
  function($mdDialog, SimpleMessageToastService, accessWay,
     dataPackage, $rootScope, $scope, DataPackageAttachmentResource,
     CitationHintGeneratorService, LanguageService, FileSaver, Blob,
     EndOfLineService, $filter) {
    var ctrl = this;
    $scope.bowser = $rootScope.bowser;

    ctrl.$onInit = function() {
      DataPackageAttachmentResource.findByDataPackageId({
        dataPackageId: dataPackage.id
      }).$promise.then(function(attachments) {
        var germanMethodReports = attachments.filter(function(attachment) {
          return attachment.type.en === 'Method Report' &&
          attachment.language === 'de' &&
          attachment.citationDetails;
        });
        var englishMethodReports = attachments.filter(function(attachment) {
          return attachment.type.en === 'Method Report' &&
          attachment.language === 'en' &&
          attachment.citationDetails;
        });
        ctrl.methodReportsCitationHint = {
          de: germanMethodReports.length > 0 ?
            CitationHintGeneratorService.generateCitationHintForAttachment(
              germanMethodReports[0]) : null,
          en: englishMethodReports.length > 0 ?
            CitationHintGeneratorService.generateCitationHintForAttachment(
              englishMethodReports[0]) : null,
        };
        ctrl.methodReports = {
          de: germanMethodReports.length > 0 ? germanMethodReports[0] : null,
          en: englishMethodReports.length > 0 ? englishMethodReports[0] : null
        };
        ctrl.dataPackageCitationHint = CitationHintGeneratorService
          .generateCitationHint(accessWay, dataPackage);
      });
    };

    ctrl.openSuccessCopyToClipboardToast = function(message) {
      SimpleMessageToastService.openSimpleMessageToast(message, []);
    };

    ctrl.closeDialog = function() {
      $mdDialog.hide();
    };

    ctrl.downloadDataPackageBibtex = function() {
      ctrl.saveBibtex(CitationHintGeneratorService
          .generateBibtex(accessWay, dataPackage));
    };

    ctrl.downloadMethodReportBibtex = function() {
      var currentLanguage = LanguageService.getCurrentInstantly();
      var fallbackLanguage = currentLanguage === 'de' ? 'en' : 'de';
      var methodReport = ctrl.methodReports[currentLanguage];
      if (!methodReport) {
        methodReport = ctrl.methodReports[fallbackLanguage];
      }
      if (methodReport) {
        ctrl.saveBibtex(CitationHintGeneratorService
          .generateBibtexForAttachment(methodReport));
      }
    };

    ctrl.saveBibtex = function(bibtex) {
      var bibtexKey = bibtex.match(/{(.*),/)[1];
      var date = new Date();
      var dateForTextFile = $filter('date')(date, 'MM/dd/yyyy \'at\' HH:mm');
      var endOfLine = EndOfLineService.getOsDependingEndOfLine();

      var fileContent = '% This file was created on ' +
        dateForTextFile;
      fileContent += endOfLine;
      fileContent += endOfLine;
      fileContent += bibtex;

      //Save file
      var data = new Blob([fileContent], {type: 'text/plain;charset=utf-8'});
      var fileName = bibtexKey + '.bib';
      FileSaver.saveAs(data, fileName);
    };
  });
