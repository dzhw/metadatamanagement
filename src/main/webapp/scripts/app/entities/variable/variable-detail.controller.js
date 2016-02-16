/* global d3 */
/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', ['$scope', 'entity', 'Language',
    '$translate',

    function($scope, entity, Language, $translate) {
      $scope.variable = entity;

      //options for charts
      $scope.options = {
        chart: {
          //CSS information
          type: 'discreteBarChart',
          height: 350,
          showValues: true,
          color: ['#0069B4'],
          valueFormat: function(d) {
            //.2f means two numbers after a commata
            return d3.format(',.2f')(d);
          },

          transitionDuration: 500,

          //Point for data extraction to visualisation
          x: function(d) {

            //if en, return en lables
            if (Language.getCurrentInstantly() === 'en') {
              if (d.label.en) {
                return d.label.en;
                //return the code, if label is not set
              } else {
                return d.code;
              }
            }

            //de is default. return de labels.
            if (d.label.de) {
              return d.label.de;
              //return code, if label is not set
            } else {
              return d.code;
            }
          },

          y: function(d) {
            return d.absoluteFrequency;
          },
        },

        // title options
        title: {
          enable: true,
          text: '',
        },

        // subtitle options
        subtitle: {
          enable: true,
          text: '',
          css: {
            'text-align': 'center',
            margin: '10px 13px 0px 7px',
          },
        },

        // caption options
        caption: {
          enable: true,
          html: '',
          css: {
            'text-align': 'center',
            margin: '-30px 0px 0px 0px',
          },
        },
      };

      //Wait for promise object
      $scope.variable.$promise.then(function() {
        //The data object is for the display
        $scope.data = [{
          values: $scope.variable.values
        }];
        $scope.options.title.text = $scope.variable.name;

        $translate(
          'metadatamanagementApp.variable.chart.absoluteFrequency').then(
          function(translation) {
            $scope.options.subtitle.text = translation;
          });

        $translate('metadatamanagementApp.variable.chart.figure').then(
          function(translation) {
            $scope.options.caption.html = translation + ': ' + $scope.variable
              .name + ', ' + $scope.options.subtitle.text;
          });
      });
    }
  ]);
