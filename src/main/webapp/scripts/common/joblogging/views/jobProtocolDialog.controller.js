/* Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller(
  'JobProtocolDialogController', function(
    $scope, $mdDialog, JobLoggingService, $translate, FileSaver, Blob,
      EndOfLineService) {
    $scope.job = JobLoggingService.getCurrentJob();
    $scope.closeDialog = function() {
      $mdDialog.hide();
    };

    /* Save Protocol */
    $scope.saveProtocol = function() {
      var protocol = '';
      var endOfLine = EndOfLineService.getOsDependingEndOfLine();

      //Add Headline Summary
      protocol += $translate
        .instant('global.joblogging.protocol-dialog.total') + ': ' +
        $scope.job.total + endOfLine;
      protocol += $translate
        .instant('global.joblogging.protocol-dialog.success') + ': ' +
        $scope.job.successes + endOfLine;
      protocol += $translate
        .instant('global.joblogging.protocol-dialog.warning') + ': ' +
        $scope.job.warnings + endOfLine;
      protocol += $translate
        .instant('global.joblogging.protocol-dialog.error') + ': ' +
        $scope.job.errors + endOfLine;
      protocol += endOfLine;
      protocol += $translate
        .instant('global.joblogging.protocol-dialog.title') + ': ' + endOfLine;

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
      var date = new Date();

      //FileName Example: Protocol_2017_05_31_15_23_45.txt
      var fileName = $translate
        .instant('global.joblogging.protocol-dialog.title') + '_' +
        date.getFullYear() + '_' +
        ('0' + (date.getMonth() + 1)).slice(-2) + '_' +
        ('0' + date.getDate()).slice(-2) + '_' +
        ('0' + date.getHours()).slice(-2) + '_' +
        ('0' + date.getMinutes()).slice(-2) + '_' +
        ('0' + date.getSeconds()).slice(-2) + '.txt';
      FileSaver.saveAs(data, fileName);
    };
  });
