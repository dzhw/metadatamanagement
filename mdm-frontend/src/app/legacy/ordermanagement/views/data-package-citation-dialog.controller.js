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
  function($mdDialog, SimpleMessageToastService, accessWay,
     dataPackage, $rootScope, $scope, DataPackageAttachmentResource,
     DataAcquisitionProjectAttachmentsResource,
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

      // cite list of instrument attachment of type questionnaire
      DataAcquisitionProjectAttachmentsResource.get({
        id: dataPackage.dataAcquisitionProjectId
      }).$promise.then(
        function(attachments) {
          const instrumentAttachments = attachments.instruments;
          if (instrumentAttachments && instrumentAttachments.length > 0) {
            ctrl.questionnaireCitationHint = {
              de: [],
              en: []
            }
            ctrl.questionnaire = {
              de: [],
              en: []
            };
              
            // include the original questionnaire
            for (const attachment of instrumentAttachments) {
              if (attachment.type.en === 'Questionnaire') {
                ctrl.createCitations(attachment);
              }
            }

            // only include the original questionnaire but in case a questionnaire is not available but a variable
            // questionnaire is there, the variable questionnaire should be used
            if (ctrl.questionnaireCitationHint.de.length === 0 && ctrl.questionnaireCitationHint.en.length === 0) {
              for (const attachment of instrumentAttachments) {
                if (attachment.type.en === 'Variable Questionnaire') {
                  ctrl.createCitations(attachment);
                }
              }
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

    ctrl.downloadQuestionnaireBibtex = function(citation) {
      var currentLanguage = LanguageService.getCurrentInstantly();
      var fallbackLanguage = currentLanguage === 'de' ? 'en' : 'de';
      var questionnaires = ctrl.questionnaire[currentLanguage];
      // find object in array of maps by its citation text as map key
      let matchingObject = questionnaires.find(obj => Object.keys(obj)[0] === citation);
      if (!questionnaires || !matchingObject || !matchingObject[citation]) {
        questionnaires = ctrl.questionnaire[fallbackLanguage];
        matchingObject = questionnaires.find(obj => Object.keys(obj)[0] === citation);
      }
      if (matchingObject[citation]) {
        ctrl.saveBibtex(CitationHintGeneratorService
          .generateBibtexForInstrumentAttachment(matchingObject[citation]));
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

    ctrl.createCitations = function(attachment) {
      // cite questionnaire or variable questionnaire if optional citation details (publication year, institution, location) and authors are given.
      if (attachment.citationDetails && attachment.citationDetails.publicationYear && attachment.citationDetails.location 
        && attachment.citationDetails.institution && attachment.citationDetails.authors.length > 0) {
        if (attachment.language === 'de' && !!attachment.description.de) {
          const citation = CitationHintGeneratorService.generateCitationHintForInstrumentAttachment(attachment.citationDetails, attachment.description.de);
          ctrl.questionnaire.de.push({
            [citation]: attachment.citationDetails
          });
          ctrl.questionnaireCitationHint.de.push(citation);
        } else if (!!attachment.description.en) {
          const citation = CitationHintGeneratorService.generateCitationHintForInstrumentAttachment(attachment.citationDetails, attachment.description.en);
          ctrl.questionnaire.en.push({
            [citation]: attachment.citationDetails
          });
          ctrl.questionnaireCitationHint.en.push(citation);
        }
      }
    }
  }]);

