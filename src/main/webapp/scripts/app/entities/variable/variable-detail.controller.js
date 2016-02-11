/* global d3 */
/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController',
    function($scope, entity) {
      $scope.variable = entity;

      //options for charts
      $scope.options = {
        chart: {

          //CSS information
          type: 'discreteBarChart',
          height: 350,
          showValues: true,
          valueFormat: function(d) {
            return d3.format(',.2f')(d); //.2f means two numbers after a commata
          },
          transitionDuration: 500,

          //Point for data extraction to visualisation
          x: function(d) {
            return d.label.en;
          },
          y: function(d) {
            return d.absoluteFrequency;
          }
        }
      };

      //The data object is for the display
      $scope.data = [{
        values:  $scope.variable.values
      }];
    });
