'use strict';

angular.module('metadatamanagementApp').directive('metadataStatus',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/dataacquisitionprojectmanagement/directives/' +
        'metadata-status.html.tmpl',
      scope: {
        project: '=',
        type: '='
      },
      controllerAs: 'ctrl',
      bindToController: true,
      controller: function(ProjectStatusScoringService) {
        this.$onInit = function() {
          this.score = ProjectStatusScoringService
            .scoreProjectStatus(this.project, this.type);
        };
      }
    };
  });
