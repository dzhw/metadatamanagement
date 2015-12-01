/* global JSZipUtils */
/* global JSZip */
/* global saveAs */
/* global Handlebars */
'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController',
    function($scope, $rootScope, $stateParams, entity, Variable) {
    $scope.variable = entity;
    $scope.load = function(id) {
      Variable.get({id: id}, function(result) {
        $scope.variable = result;
      });
    };
    $scope.exportToODT = function() {
      // download the template
      JSZipUtils.getBinaryContent('/officetemplates/variable.odt',
      function(err, data) {
        if (err) {
          throw err; // or handle err
        }
        // unzip the template
        var zip = new JSZip(data);
        var content = zip.file('content.xml').asText();
        // compile the handlebar template
        var template = Handlebars.compile(content);
        // put all required json objects in the context
        var context = {variable: $scope.variable};
        // fill the template with the context variables
        content = template(context);
        zip.file('content.xml', content);
        // save the zip file (odt)
        var blob = zip.generate({type: 'blob'});
        saveAs(blob, 'variable_' + $scope.variable.name + '.odt');
      });
    };
    var unsubscribe = $rootScope.$on('metadatamanagementApp:variableUpdate',
      function(event, result) {
      $scope.variable = result;
    });
    $scope.$on('$destroy', unsubscribe);

  });
