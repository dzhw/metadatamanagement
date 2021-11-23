(function() {
  'use strict';

  var Component = {
    controller: 'ReportPublicationsController',
    templateUrl: 'scripts/relatedpublicationmanagement/components/' +
      'report-publications.html.tmpl',
    bindings: {
      analysisPackage: '<'
    }
  };

  angular
    .module('metadatamanagementApp')
    .component('reportPublicationsComponent', Component);
})();
