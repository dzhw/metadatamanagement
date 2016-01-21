/* global JSZipUtils */
/* global JSZip */
/* global saveAs */
'use strict';

angular.module('metadatamanagementApp').factory(
    'FdzProjectExportService',
    function($translate, HandlebarsService, VariableCollection) {
      // the complete odt
      var zip;
      // the compiled handlebars templates
      var templateContent;
      var templateStyles;
      // all variables of a fdzProject
      var variables;

      //Write odt with information of the fdz projection
      var writeODT = function(fdzProject) {

        //Query to RestAPI. Filter: fdzProject id
        VariableCollection.query({fdzProject: fdzProject.id},
          function(result) {
            variables = result._embedded.variables;

            // put all required json objects in the context
            var context = {variables: variables,
              fdzProject: fdzProject};

            // fill the templates with the context variables
            var content = templateContent(context);
            var styles = templateStyles(context);
            zip.file('content.xml', content);
            zip.file('styles.xml', styles);
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
              // unzip the templates
              zip = new JSZip(data);
              var content = zip.file('content.xml').asText();
              var styles = zip.file('styles.xml').asText();
              // compile the handlebar templates
              templateContent = HandlebarsService.compile(content);
              templateStyles = HandlebarsService.compile(styles);
              writeODT(fdzProject);
            });
          } else {
            writeODT(fdzProject);
          }
        }
      };
    }
  );
