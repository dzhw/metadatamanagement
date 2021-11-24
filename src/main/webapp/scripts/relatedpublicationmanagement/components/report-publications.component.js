(function() {
  'use strict';

  var Component = {
    controller: 'ReportPublicationsController',
    // templateUrl: 'scripts/relatedpublicationmanagement/components/' +
    //   'report-publications.html.tmpl',
    bindings: {
      analysisPackage: '<'
    },
    templateUrl: ['$attrs', function($attrs) {
      var tmpl = {
        false: 'scripts/relatedpublicationmanagement/components/' +
          'report-publications.html.tmpl',
        true: 'scripts/relatedpublicationmanagement/components/' +
          'report-publications.analysis-package.html.tmpl'
      };
      console.log($attrs.analysisPackage);
      if ($attrs.analysisPackage) {
        console.log('false test');
        return tmpl.true;
      }
      return tmpl.false;
    }]
  };

  angular
    .module('metadatamanagementApp')
    .component('reportPublicationsComponent', Component);
})();
