/* global JSZipUtils */
/* global JSZip */
/* global saveAs */
'use strict';

angular.module('metadatamanagementApp').factory(
    'VariableExportService',
    function($translate, HandlebarsService) {
      // the complete odt
      var zip;
      // the compiled handlebars template
      var template;

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
        var blob = zip.generate({type: 'blob',
          mimeType: 'application/vnd.oasis.opendocument.text'});
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
              template = HandlebarsService.compile(content);

              writeODT(variable);
            });
          } else {
            writeODT(variable);
          }
        }
      };
    }
  );
