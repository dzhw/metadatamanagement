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
            'upload-terminated': 'Finished upload of {{ total }} Publications with {{ errors }} errors.',
            'unable-to-delete': 'Publications could not be deleted!',
            'cancelled': 'Publications upload cancelled!'
          }
        },
        'detail': {
          'title': '{{ title }}',
          'publication': 'Publication',
          'publications': 'Publications',
          'related-information': 'Related Information',
          'studies-title': 'Studies',
          'questions-title': 'Questions',
          'variables-title': 'Variables',
          'citation-text': 'Citation Text',
          'abstract': 'Abstract',
          'doi': 'DOI',
          'sourceReference': 'Source Reference',
          'sourceLink': 'URL',
          'no-related-publications': 'No Publications.',
          'related-publications': 'Publications'
        },
        'error': {
          'related-publication': {
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
            }
          },
          'post-validation': {
            'study-unknown': 'The Study {{id}} could not be found. It is referenced to the publication ({{toBereferenzedId}}).',
            'variable-unknown': 'The Variable {{id}} could not be found. It is referenced to the publication ({{toBereferenzedId}}).',
            'variable-has-not-a-referenced-study': 'The Variable {{id}} has a reference to the Study ({{additionalId}}). This Study is not refereced by the publication ({{toBereferenzedId}}).',
            'survey-unknown': 'The Survey {{id}} could not be found. It is referenced to the publication ({{toBereferenzedId}}).',
            'survey-has-not-a-referenced-study': 'The Survey {{id}} has a reference to the Study ({{additionalId}}). This Study is not refereced by the publication ({{toBereferenzedId}}).',
            'data-set-unknown': 'The Data Set {{id}} could not be found. It is referenced to the publication ({{toBereferenzedId}}).',
            'data-set-has-not-a-referenced-study': 'The Data Set {{id}} has a reference to the Study ({{additionalId}}). This Study is not refereced by the publication ({{toBereferenzedId}}).',
            'instrument-unknown': 'The instrument {{id}} could not be found. It is referenced to the publication ({{toBereferenzedId}}).',
            'instrument-has-not-a-referenced-study': 'The instrument {{id}} has a reference to the Study ({{additionalId}}). This Study is not refereced by the publication ({{toBereferenzedId}}).',
            'question-unknown': 'The Question {{id}} could not be found. It is referenced to the publication ({{toBereferenzedId}}).',
            'question-has-not-a-referenced-study': 'The Question {{id}} has a reference to the Study ({{additionalId}}). This Study is not refereced by the publication ({{toBereferenzedId}}).'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
