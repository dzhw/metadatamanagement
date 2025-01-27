'use strict';
angular.module('metadatamanagementApp')
  .controller('DataPackageCitationDialogController', [
  '$mdDialog',
  'SimpleMessageToastService',
  'accessWay',
  'dataPackage',
  '$rootScope',
  '$scope',
  'DataPackageAttachmentResource',
  'DataAcquisitionProjectAttachmentsResource',
  'CitationHintGeneratorService',
  'LanguageService',
  'FileSaver',
  'Blob',
  'EndOfLineService',
  '$filter',
  'InstrumentAttachmentTypesEn',
  function($mdDialog, SimpleMessageToastService, accessWay,
     dataPackage, $rootScope, $scope, DataPackageAttachmentResource,
     DataAcquisitionProjectAttachmentsResource,
     CitationHintGeneratorService, LanguageService, FileSaver, Blob,
     EndOfLineService, $filter, InstrumentAttachmentTypesEn) {
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

      // cite list of instrument attachment of type questionnaire
      DataAcquisitionProjectAttachmentsResource.get({
        id: dataPackage.dataAcquisitionProjectId
      }).$promise.then(function(attachments) {

        // group questionnaire attachments by instrument
        const instrumentAttachments = attachments.instruments
          .map(a => a.instrumentNumber)
          // deduplicate instrument number
          .filter((number, index, numbers) => {
            return !numbers.slice(0, index).includes(number);
          })
          // reduce instrument numbers array to an object that maps questionnaire
          // attachments to their corresponding instrument number
          .reduce((atts, number) => {
            atts[number] = attachments.instruments.filter(a => a.instrumentNumber === number && (
              a.type.en === InstrumentAttachmentTypesEn.Questionnaire ||
              a.type.en === InstrumentAttachmentTypesEn.VariableQuestionnaire
            ));
            return atts;
          }, {});

        const currentLanguage = LanguageService.getCurrentInstantly();
        ctrl.instrumentAttachmentCitations = [];
        for (const entries of Object.values(instrumentAttachments)) {
          console.log(entries);
          // use questionnaire for the current instrument and only use variable questionnaire as a backup
          var attachment = entries.find(a => a.type.en === InstrumentAttachmentTypesEn.Questionnaire) ||
            entries.find(a => a.type.en === InstrumentAttachmentTypesEn.VariableQuestionnaire);
          if (!attachment) continue;
          const citationHint = createInstrumentAttachmentCitation(attachment, currentLanguage);
          if (citationHint) {
            ctrl.instrumentAttachmentCitations.push({
              hint: citationHint,
              details: attachment.citationDetails
            });
          }
        }
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

    /**
     * Generates BibTex document from the provided instrument attachment
     * citation details and triggers its download for the user.
     * @param {*} details the instrument attachment's citation details
     */
    ctrl.downloadInstrumentAttachmentBibtex = function(details) {
      const bibtex = CitationHintGeneratorService.generateBibtexForInstrumentAttachment(details);
      ctrl.saveBibtex(bibtex);
    };

    /**
     * Creates a citation hint for the attachment's citation details.
     * @param {InstrumentAttachmentMetadata} attachment the attachment the citation hint is created for
     * @param {string} currentLanguage the language the user is currently using
     * @returns the citation hint
     */
    const createInstrumentAttachmentCitation = function(attachment, currentLanguage) {

      if (!attachment.citationDetails) return;
      if (!attachment.citationDetails.publicationYear) return;
      if (!attachment.citationDetails.location) return;
      if (!attachment.citationDetails.institution) return;
      if (!attachment.citationDetails.authors) return;
      if (attachment.citationDetails.authors.length === 0) return;

      const fallbackLanguage = currentLanguage === 'de' ? 'en' : 'de';
      var description = "";
      if (attachment.language === currentLanguage) {
        description = attachment.description[currentLanguage];
      } else if (!!attachment.description[fallbackLanguage]) {
        description = attachment.description[fallbackLanguage];
      } else {
        return;
      }

      return CitationHintGeneratorService.generateCitationHintForInstrumentAttachment(
        attachment.citationDetails, description);
    }
  }]);

