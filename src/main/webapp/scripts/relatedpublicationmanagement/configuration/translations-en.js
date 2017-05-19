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
          'title': '{{ title }} ({{publicationId}})',
          'publication': 'Publication',
          'publications': 'Publications',
          'related-information': 'Related Information',
          'studies-title': 'Studies',
          'questions-title': 'Questions',
          'variables-title': 'Variables',
          'citation-text': 'Citation Text',
          'abstract': 'Abstract',
          'doi': 'DOI',
          'doi-tooltip': 'Click to open the DOI in a new Tab',
          'sourceReference': 'Source Reference',
          'sourceLink': 'URL',
          'sourceLink-tooltip': 'Click to open the source of this publication in a new Tab',
          'no-related-publications': 'No Publications.',
          'related-publications': 'Publications',
          'authors': 'Authors',
          'year': 'Year of publication',
          'source-reference': 'Citation',
          'abstract-source': 'Source',
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
            'studies':{
              'one': 'Click to show the study for which this publication has been written',
              'many': 'Click to show all studies for which this publication has been written'
            }
          }
        },
        'error': {
          'related-publication': {
            'one-foreign-key-is-used': 'The Publication has no connection to any other object.',
            'one-study-is-used': 'The publication has no connection to any study.',
            'valid-related-publication-id': 'The Id of Publication have to be build up after the pattern: "pub-" + {IdFromCitavi} + "$".',
            'id': {
              'not-empty': 'The RDC-ID of the Publication must not be empty!',
              'size': 'The max length of the RDC-ID is 128 signs.',
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
              'size': 'The max length of the DOI of the Publication is 128 signs.'
            },
            'source-link': {
              'pattern': 'The source link of the Publication is not a valid URL.'
            },
            'title': {
              'not-empty': 'The title of the Publication must not be empty!',
              'size': 'The max length of the title of the Publication is 128 signs.'
            },
            'authors': {
              'size': 'The max length of the authors of the Publication is 2048 signs.',
              'not-empty': 'The authors of the Publication must not be empty!'
            },
            'year': {
              'not-null': 'The Publication Year must not be empty!',
              'valid': 'The Publication Year must be between 1960 and {{currentDate | date :"yyyy"}}.'
            },
            'abstract-source': {
              'i18n-string-size': 'The max length of Source of the Publication is 2048 signs.'
            },
            'language': {
              'not-null': 'The language of the Publication must not be empty!',
              'not-supported': 'The language of the Publication must be a two-letter abbreviation according to ISO 639-1!'
            }
          },
          'post-validation': {
            'study-unknown': 'The Study {{id}} could not be found. It is referenced by the publication ({{toBereferenzedId}}).',
            'variable-unknown': 'The Variable {{id}} could not be found. It is referenced by the publication ({{toBereferenzedId}}).',
            'variable-has-not-a-referenced-study': 'The Variable {{id}} has a reference to the Study ({{additionalId}}). This Study is not refereced by the publication ({{toBereferenzedId}}).',
            'survey-unknown': 'The Survey {{id}} could not be found. It is referenced by the publication ({{toBereferenzedId}}).',
            'survey-has-not-a-referenced-study': 'The Survey {{id}} has a reference to the Study ({{additionalId}}). This Study is not refereced by the publication ({{toBereferenzedId}}).',
            'data-set-unknown': 'The Data Set {{id}} could not be found. It is referenced by the publication ({{toBereferenzedId}}).',
            'data-set-has-not-a-referenced-study': 'The Data Set {{id}} has a reference to the Study ({{additionalId}}). This Study is not refereced by the publication ({{toBereferenzedId}}).',
            'instrument-unknown': 'The instrument {{id}} could not be found. It is referenced by the publication ({{toBereferenzedId}}).',
            'instrument-has-not-a-referenced-study': 'The instrument {{id}} has a reference to the Study ({{additionalId}}). This Study is not refereced by the publication ({{toBereferenzedId}}).',
            'question-unknown': 'The Question {{id}} could not be found. It is referenced by the publication ({{toBereferenzedId}}).',
            'question-has-not-a-referenced-study': 'The Question {{id}} has a reference to the Study ({{additionalId}}). This Study is not refereced by the publication ({{toBereferenzedId}}).'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
