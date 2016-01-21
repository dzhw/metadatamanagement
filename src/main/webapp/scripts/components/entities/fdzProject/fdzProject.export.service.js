/* global JSZipUtils */
/* global JSZip */
/* global saveAs */
'use strict';

angular.module('metadatamanagementApp').factory(
    'FdzProjectExportService',
    function($translate, HandlebarsService, VariableCollection) {
      // the complete odt
      var zip;
      // the compiled handlebars template
      var template;

      var variables;

      var writeODT = function(fdzProject) {

        VariableCollection.query({fdzProject: fdzProject.id},
          function(result) {
            variables = result._embedded.variables;

            // put all required json objects in the context
            var context = {variables: variables};

            // fill the template with the context variables
            var content = template(context);
            zip.file('content.xml', content);
            // save the zip file (odt)
            var blob = zip.generate({type: 'blob',
              mimeType: 'application/vnd.oasis.opendocument.text'});
            saveAs(blob, fdzProject.name + '_Report.odt');
          });
      };

      return {
        exportToODT: function(fdzProject) {
          // download the template if not already downloaded
          if (!zip) {
            JSZipUtils.getBinaryContent(
              '/officetemplates/fdzProjectTemplate.odt',
            function(err, data) {
              if (err) {
                throw err; // or handle err
              }
              // unzip the template
              zip = new JSZip(data);
              var content = zip.file('content.xml').asText();
              // compile the handlebar template
              template = HandlebarsService.compile(content);

              writeODT(fdzProject);
            });
          } else {
            writeODT(fdzProject);
          }
        }
      };
    }
  );
