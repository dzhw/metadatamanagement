'use strict';
angular.module('metadatamanagementApp')
  .controller('AnalysisPackageCitationDialogController', [
  '$mdDialog',
  'SimpleMessageToastService',
  'analysisPackage',
  '$rootScope',
  '$scope',
  'AnalysisPackageCitationHintGenerator',
  'FileSaver',
  'Blob',
  'EndOfLineService',
  '$filter',
  function($mdDialog, SimpleMessageToastService, analysisPackage,
           $rootScope, $scope, AnalysisPackageCitationHintGenerator, FileSaver,
           Blob, EndOfLineService, $filter) {
    var ctrl = this;
    $scope.bowser = $rootScope.bowser;

    ctrl.$onInit = function() {

      ctrl.analysisPackageCitationHint = AnalysisPackageCitationHintGenerator
          .generateCitationHint(analysisPackage);
    };

    ctrl.openSuccessCopyToClipboardToast = function(message) {
      SimpleMessageToastService.openSimpleMessageToast(message, []);
    };

    ctrl.closeDialog = function() {
      $mdDialog.hide();
    };

    ctrl.downloadAnalysisPackageBibtex = function() {
      ctrl.saveBibtex(AnalysisPackageCitationHintGenerator
          .generateBibtex(analysisPackage));
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
  }]);

