/* global JSZipUtils */
/* global JSZip */
/* global saveAs */
'use strict';

angular.module('metadatamanagementApp').factory(
  'DataAcquisitionProjectExportService',
  function($translate, $translatePartialLoader,
    HandlebarsService, VariableCollection) {
    // the complete odt
    var zip;
    // the compiled handlebars templates
    var templateContent;
    var templateStyles;
    // all variables of a dataAcquisitionProject
    var variables;

    //Write odt with information of the rdc projection
    var writeODT = function(dataAcquisitionProject) {

      //add dataType.json and scaleLevel.json to the known translation parts
      $translatePartialLoader.addPart('dataType');
      $translatePartialLoader.addPart('scaleLevel');
      $translate.refresh();

      //Query to RestAPI. Filter: dataAcquisitionProject id
      VariableCollection.query({
          dataAcquisitionProject: dataAcquisitionProject.id
        },
        function(result) {
          variables = result._embedded.variables;
          // put all required json objects in the context
          var context = {
            variables: variables,
            dataAcquisitionProject: dataAcquisitionProject
          };

          // fill the templates with the context variables
          var content = templateContent(context);
          var styles = templateStyles(context);
          zip.file('content.xml', content);
          zip.file('styles.xml', styles);
          // save the zip file (odt)
          var blob = zip.generate({
            type: 'blob',
            mimeType: 'application/vnd.oasis.opendocument.text'
          });
          saveAs(blob, dataAcquisitionProject.id + '_Report.odt');
        });
    };

    return {
      exportToODT: function(dataAcquisitionProject) {
        // download the template if not already downloaded
        if (!zip) {
          JSZipUtils.getBinaryContent(
            '/officetemplates/dataAcquisitionProjectTemplate.odt',
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
              writeODT(dataAcquisitionProject);
            });
        } else {
          writeODT(dataAcquisitionProject);
        }
      }
    };
  }
);
