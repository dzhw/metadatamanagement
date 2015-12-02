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

      // TODO move registerHelper calls to global place
      Handlebars.registerHelper('translate', function(prefix, value) {
        return $translate.instant(prefix + '.' + value);
      });

      Handlebars.registerHelper('encodeMissing', function(isMissing) {
        if (isMissing) {
          return 'M';
        } else {
          return '';
        }
      });

      var writeODT = function(variable) {
        // TODO remove test anwerOptions
        variable.answerOptions = [
          {code: 0, label: 'testlabel', isMissing: false},
          {code: 1, label: 'testlabel', isMissing: true}
        ];

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
