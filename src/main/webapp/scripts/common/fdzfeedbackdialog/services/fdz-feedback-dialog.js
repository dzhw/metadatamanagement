'use strict';

angular.module('metadatamanagementApp').run(function(FdzFeedbackDialogService,
                                                     $timeout) {
    $timeout(function() {
      FdzFeedbackDialogService.deleteOption();
    });
  });
