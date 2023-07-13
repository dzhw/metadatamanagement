/* globals _ */
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
      controller: ['ProjectStatusScoringService', function(ProjectStatusScoringService) {
        this.$onInit = function() {
          var requirements = _.get(this.project, 'configuration.requirements');
          this.displayStatus =  requirements &&
            requirements[this.type + 'Required'];

          if (this.displayStatus) {
            this.score = ProjectStatusScoringService
              .scoreProjectStatus(this.project, this.type);
          }
        };
      }]
    };
  });
