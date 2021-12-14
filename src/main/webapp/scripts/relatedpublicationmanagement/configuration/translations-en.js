'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'related-publication-management': {
        'home': {
          'title': 'Publication'
        },
        'log-messages': {
          'related-publication': {
            'saved': 'Publication with RDC-ID {{ id }} was saved successfully!',
            'not-saved': 'Publication with RDC-ID {{ id }} has not been saved!',
            'missing-id': 'Publication {{ index }} does not contain a RDC-ID and has not been saved!',
            'duplicate-id': 'The RDC-ID ({{ id }}) of Publication {{ index }} has already been used.',
            'upload-terminated': 'Finished upload of {{ total }} Publications with {{warnings}} warnings and {{ errors }} errors.',
            'unable-to-delete': 'Publications could not be deleted!',
            'cancelled': 'Publications upload cancelled!'
          }
        },
        'detail': {
          'label': {
            'publication': 'Publication',
            'publications': 'Publications',
            'doi': 'DOI',
            'sourceReference': 'Source Reference',
            'sourceLink': 'URL',
            'authors': 'Authors',
            'year': 'Year of publication',
            'source-reference': 'Reference',
            'abstract-source': 'Source',
            'studySerieses': 'Study Serieses',
            'annotations': 'Annotations'
          },
          'abstract': 'Abstract',
          'title': '{{ title }}',
          'description': '{{ sourceReference }}',
          'doi-tooltip': 'Click to open the DOI in a new Tab',
          'sourceLink-tooltip': 'Click to open the source of this publication in a new Tab',
          'tooltips': {
            'surveys': {
              'one': 'Click to show the survey for which this publication has been written',
              'many': 'Click to show all surveys for which this publication has been written'
            },
            'data-sets': {
              'one': 'Click to show the data set for which this publication has been written',
              'many': 'Click to show all data sets for which this publication has been written'
            },
            'questions': {
              'one': 'Click to show the question for which this publication has been written',
              'many': 'Click to show all questions for which this publication has been written'
            },
            'instruments': {
              'one': 'Click to show the instrument for which this publication has been written',
              'many': 'Click to show all instruments for which this publication has been written'
            },
            'variables': {
              'one': 'Click to show the variable for which this publication has been written',
              'many': 'Click to show all variables for which this publication has been written'
            },
            'data-packages':{
              'one': 'Click to show the data package for which this publication has been written',
              'many': 'Click to show all data packages for which this publication has been written'
            },
            'studies-series' : 'Click to show all data packages for this study series'
          }
        },
        'error': {
          'related-publication': {
            'valid-related-publication-id': 'The Id of Publication have to be build up after the pattern: "pub-" + {IdFromCitavi} + "$".',
            'dataPackage-exists': 'There is no Data Package with RDC-ID "{{invalidValue}}"!',
            'analysisPackage-exists': 'There is no Analysis Package with RDC-ID "{{invalidValue}}"!',
            'at-least-one-referenced-id': 'The publication must have at least one connection to a data package or to an analysis package.',
            'id': {
              'not-empty': 'The RDC-ID of the Publication must not be empty!',
              'size': 'The max length of the RDC-ID is 512 signs.',
              'pattern': 'The RDC-ID must not contain any whitespaces.'
            },
            'source-reference': {
              'not-empty': 'The source reference of the Publication must not be empty!',
              'size': 'The max length of the source reference of the Publication is 2048 signs.'
            },
            'publication-abstract': {
              'size': 'The max length of the publication abstract of the Publication is 1048576 signs.'
            },
            'doi': {
              'size': 'The max length of the DOI of the Publication is 512 signs.'
            },
            'source-link': {
              'pattern': 'The source link of the Publication is not a valid URL.'
            },
            'title': {
              'not-empty': 'The title of the Publication must not be empty!',
              'size': 'The max length of the title of the Publication is 2048 signs.'
            },
            'authors': {
              'size': 'The max length of the authors of the Publication is 2048 signs.',
              'not-empty': 'The authors of the Publication must not be empty!'
            },
            'year': {
              'not-null': 'The Publication Year must not be empty!',
              'valid': 'The Publication Year must be after 1960.'
            },
            'abstract-source': {
              'size': 'The max length of Source of the Publication is 2048 signs.'
            },
            'language': {
              'not-null': 'The language of the Publication must not be empty!',
              'not-supported': 'The language of the Publication must be a two-letter abbreviation according to ISO 639-1!'
            },
            'annotations': {
              'size': 'The max length of the annotations is 2048 signs.'
            }
          }
        },
        'report-publications': {
          'speech-bubble': {
            'close': 'Close',
            'text': 'Do you know of any publications based on our data packages? Then please share them with us...',
            'analyse-package': {
              'text': 'Do you know of any publications based on our analysis packages? Then please share them with us...'
            }
          },
          'button': {
            'text': 'Report publications',
            'tooltip': 'Click to report publications on our data packages',
            'link': 'mailto:userservice@dzhw.eu?subject=Reporting%20of%20publications%20on%20data%20packages%20from%20FDZ-DZHW&body=Dear%20FDZ-DZHW%2C%0D%0A%0D%0AI%20would%20like%20to%20report%20the%20following%20publication(s)%2C%20which%20belong%20to%20data%20package%20{{ dataPackageId ? "%22" + dataPackageId + "%22" : "X"}}%3A%0D%0A',
            'analysis-package': {
              'tooltip': 'Click to report publications on our analysis packages',
              'link': 'mailto:userservice@dzhw.eu?subject=Reporting%20of%20publications%20on%20analysis%20packages%20from%20FDZ-DZHW&body=Dear%20FDZ-DZHW%2C%0D%0A%0D%0AI%20would%20like%20to%20report%20the%20following%20publication(s)%2C%20which%20belong%20to%20analysis%20package%20{{ analysisPackageId ? "%22" + analysisPackageId + "%22" : "X"}}%3A%0D%0A'
            }
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
