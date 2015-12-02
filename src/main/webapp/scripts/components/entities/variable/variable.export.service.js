/* global JSZipUtils */
/* global JSZip */
/* global saveAs */
/* global Handlebars */
'use strict';

angular.module('metadatamanagementApp').factory(
    'VariableExportService',
    function($translate) {
      // the complete odt
      var zip;
      // the compiled handlebars template
      var template;

      var getTranslatedScaleLevel = function(scaleLevel) {
        return $translate.instant(
          'metadatamanagementApp.ScaleLevel.' + scaleLevel);
      };

      var getTranslatedDataType = function(dataType) {
        return $translate.instant(
          'metadatamanagementApp.DataType.' + dataType);
      };

      var getTranslatedIsMissing = function(isMissing) {
        if (isMissing) {
          return 'M';
        }
        return '';
      };

      var writeODT = function(variable) {
        // enhance variable with i18n functions
        variable.translatedScaleLevel = function() {
          return getTranslatedScaleLevel(variable.scaleLevel);
        };

        variable.translatedDataType = function() {
          return getTranslatedDataType(variable.dataType);
        };

        // TODO remove test anwerOptions
        variable.answerOptions = [
          {code: 0, label: 'testlabel', isMissing: false},
          {code: 1, label: 'testlabel', isMissing: true}
        ];
        variable.answerOptions.forEach(function(answerOption) {
          answerOption.translatedIsMissing = function() {
            return getTranslatedIsMissing(answerOption.isMissing);
          };
        });

        // put all required json objects in the context
        var context = {variable: variable};

        // fill the template with the context variables
        var content = template(context);
        zip.file('content.xml', content);
        // save the zip file (odt)
        var blob = zip.generate({type: 'blob'});
        saveAs(blob, 'variable_' + variable.name + '.odt');
      };

      return {
        exportToODT: function(variable) {
          // download the template if not already downloaded
          if (!zip) {
            JSZipUtils.getBinaryContent('/officetemplates/variable.odt',
            function(err, data) {
              if (err) {
                throw err; // or handle err
              }
              // unzip the template
              zip = new JSZip(data);
              var content = zip.file('content.xml').asText();
              // compile the handlebar template
              template = Handlebars.compile(content);

              writeODT(variable);
            });
          } else {
            writeODT(variable);
          }
        }
      };
    }
  );
