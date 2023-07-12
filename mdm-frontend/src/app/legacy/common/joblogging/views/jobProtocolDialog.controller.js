/* Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller(
  'JobProtocolDialogController', function(
    $scope, $mdDialog, $filter, JobLoggingService, $translate, FileSaver, Blob,
      EndOfLineService) {
    $scope.job = JobLoggingService.getCurrentJob();
    $scope.closeDialog = function() {
      $mdDialog.hide();
    };

    /* Save Protocol */
    $scope.saveProtocol = function() {
      var date = new Date();
      var dateForTextFile = $filter('date')(date, 'medium', 'local');
      var dateForFileName = $filter('date')(date, 'yyyy-MM-ddTHH-mm-ss',
        'local');
      var protocol = '';
      var endOfLine = EndOfLineService.getOsDependingEndOfLine();
      var spaces = '     ';
      //50 - signs per line. last line has actually 20 - (total: 120 - signs)
      var asciiLine = '--------------------------------------------------' +
      '--------------------------------------------------' +
      '--------------------';

      //Add Headline Summary
      protocol += asciiLine;
      protocol += endOfLine;
      protocol += $translate
        .instant('global.joblogging.protocol-dialog.success') + ': ' +
        $scope.job.successes + spaces;
      protocol += $translate
        .instant('global.joblogging.protocol-dialog.warning') + ': ' +
        $scope.job.warnings + spaces;
      protocol += $translate
        .instant('global.joblogging.protocol-dialog.error') + ': ' +
        $scope.job.errors + endOfLine;
      protocol += $translate
        .instant('global.joblogging.protocol.created-by') + ' ' +
        dateForTextFile + endOfLine;
      protocol += asciiLine;
      protocol += endOfLine;
      protocol += endOfLine;

      //Add all Messages
      $scope.job.logMessages.forEach(function(logMessage) {
          //Write Type of Message before the Message itself
          switch (logMessage.type) {
            case 'error': protocol += '(' + $translate
              .instant('global.joblogging.type.error') + ') ';
              break;
            case 'warning': protocol += '(' + $translate
              .instant('global.joblogging.type.warning') + ') ';
              break;
            case 'info': protocol += '(' + $translate
              .instant('global.joblogging.type.info') + ') ';
              break;
          }
          protocol += $translate
            .instant(logMessage.message, logMessage.translationParams) +
            endOfLine;
          protocol += endOfLine;

          //Add SubMessages, if existing.
          if (logMessage.subMessages) {
            logMessage.subMessages.forEach(function(subMessage) {
              protocol += '  * ' + $translate
                .instant(subMessage.message, subMessage.translationParams) +
                endOfLine;
            });
          }

        });

      //Save Log File
      var data = new Blob([protocol], {type: 'text/plain;charset=utf-8'});
      //FileName Example: Protocol-2017-05-31T15-23-45.txt
      var fileName = $translate
        .instant('global.joblogging.protocol-dialog.title') + '-' +
        dateForFileName + '.txt';
      FileSaver.saveAs(data, fileName);
    };
  });
