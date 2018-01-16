/* global document */
'use strict';

angular.module('metadatamanagementApp').factory('CommonDialogsService',
  function($mdDialog, $translate) {
    var translationPrefixDirty = 'global.common-dialogs.confirm-dirty.';
    var translationPrefixFileDelete =
      'global.common-dialogs.confirm-file-delete.';
    var translationPrefixFilenameChange =
      'global.common-dialogs.confirm-filename-change.';

    var focusCancelButton = function() {
      var $actionsSection = angular.element(
        document.querySelector('md-dialog-actions'));
      var $cancelButton = $actionsSection.children()[0];
      var $confirmButton = $actionsSection.children()[1];
      angular.element($confirmButton).removeClass('md-focused');
      angular.element($cancelButton).addClass('md-focused');
      $cancelButton.focus();
    };

    var showConfirmOnDirtyDialog = function() {
      var confirmOnDirtyDialog = $mdDialog.confirm({
        onComplete: focusCancelButton,
        multiple: true
      }).title($translate.instant(translationPrefixDirty + 'title'))
        .textContent($translate.instant(translationPrefixDirty + 'content'))
        .ok($translate.instant('global.common-dialogs.yes'))
        .cancel($translate.instant('global.common-dialogs.no'))
        .multiple(true);
      return $mdDialog.show(confirmOnDirtyDialog);
    };

    var showConfirmFileDeletionDialog = function(filename) {
      var confirmOnFileDeletionDialog = $mdDialog.confirm({
        onComplete: focusCancelButton
      }).title($translate.instant(translationPrefixFileDelete + 'title', {
        filename: filename
      })).textContent(
        $translate.instant(translationPrefixFileDelete + 'content', {
          filename: filename
        })).ok($translate.instant('global.common-dialogs.yes'))
        .cancel($translate.instant('global.common-dialogs.no'));
      return $mdDialog.show(confirmOnFileDeletionDialog);
    };

    var showConfirmFilenameChangedDialog = function(oldFilename, newFilename) {
      var confirmOnFilenameChangedDialog = $mdDialog.confirm({
        onComplete: focusCancelButton,
        multiple: true
      }).title($translate.instant(translationPrefixFilenameChange + 'title', {
        oldFilename: oldFilename,
        newFilename: newFilename
      })).textContent(
        $translate.instant(translationPrefixFilenameChange + 'content', {
          oldFilename: oldFilename,
          newFilename: newFilename
        })).ok($translate.instant('global.common-dialogs.yes'))
        .cancel($translate.instant('global.common-dialogs.no'))
        .multiple(true);
      return $mdDialog.show(confirmOnFilenameChangedDialog);
    };

    return {
      showConfirmOnDirtyDialog: showConfirmOnDirtyDialog,
      showConfirmFileDeletionDialog: showConfirmFileDeletionDialog,
      showConfirmFilenameChangedDialog: showConfirmFilenameChangedDialog
    };
  });
