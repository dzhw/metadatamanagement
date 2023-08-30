'use strict';
/*
 * Score the project status. Readyness and required fields are evaluated to
 * calculate a score. The following scores are possible:
 *
 * 0: very dissatisfying
 * 1: satisfying
 * 2: very satisfying
 */
angular.module('metadatamanagementApp')
  .service('ProjectStatusScoringService', function() {
    var scoreProjectStatus = function(project, type) {
      var isRequired = project.configuration
        .requirements[type + 'Required'];

      var typeState = project.configuration[type + 'State'];

      if (isRequired && (typeState === undefined || typeState === null)) {
        return 0;
      }

      if (!isRequired && (typeState === undefined || typeState === null)) {
        return 2;
      }

      var isPublisherReady = typeState.publisherReady;
      var isDataProviderReady = typeState.dataProviderReady;

      if (isRequired && !isPublisherReady && !isDataProviderReady) {
        return 0;
      }

      if (!isRequired || isRequired && isPublisherReady) {
        return 2;
      }

      if (isRequired && !isPublisherReady && isDataProviderReady) {
        return 1;
      }
    };

    return {
      scoreProjectStatus: scoreProjectStatus
    };
  });
