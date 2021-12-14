(function() {
  'use strict';

  var Component = {
    controller: 'AnalysisPackageConfiguratorController',
    templateUrl:
      'scripts/ordermanagement/components/' +
      'analysis-package-configurator.html.tmpl'
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzAnalysisPackageConfigurator', Component);

})();
