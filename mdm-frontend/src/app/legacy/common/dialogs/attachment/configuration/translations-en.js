'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'attachment': {
        'create-title': 'Add file',
        'edit-title': 'Edit file',
        'attachment-saved-toast': 'File "{{ filename }}" was saved.',
        'current-version-restored-toast': 'Current version of the metadata for document "{{ filename }}" has been restored.',
        'previous-version-restored-toast': 'Previous version of the metadata for document "{{ filename }}" can be saved now.',
        'error': {
          'attachment-has-validation-errors-toast': 'The file has not been saved because there are invalid fields!.',
          'description': {
            'i18n-string-not-empty': 'The description must not be empty at least for one language.',
            'i18n-string-size': 'The description is mandatory and must in at least one language and must not contain more than 512 characters.'
          },
          'filename': {
            'not-empty': 'A file must be selected.',
            'not-unique': 'A file with this name already exists.',
            'not-valid': 'This file name is invalid',
            'pattern': 'Use only alphanumeric signs, German umlauts, ß and space, underscore and minus for the RDC-ID.'
          },
          'file-not-found': 'The File {{ filename }} was not found and has not been saved.',
          'type': {
            'not-null': 'Attachment type must not be emtpy.'
          },
          'language': {
            'not-found': 'No valid language found.',
            'not-null': 'The attachment language must not be empty.',
            'not-valid': 'Please select one of the provided languages.'
          },
          'title': {
            'maxlength': 'The title of the attachment must not contain more than 2048 characters.',
            'not-empty': 'The titel of the attachment must not be empty.'
          },
          'doi': {
            'size': 'The DOI must be shorter then 512 characters.',
            'pattern': 'The DOI-Url should match the following pattern: https://doi.org/<id>'
          }
        },
        'hint': {
          'filename': 'Choose a file you want to add as an attachment',
          'language': 'Select the language which has been used in the file.',
          'type': 'Choose the type of the file.',
          'title': 'Enter the title of the file in the language of the file.',
          'description': {
            'de': 'Please enter a description for the file in German.',
            'en': 'Please enter a description for the file in English.'
          },
          'doi': 'Please enter the DOI as URL (Example: https://doi.org/10.21249/DZHW:gra2009:2.0.0).',
        },
        'label': {
          'description': 'Description',
          'file': 'File',
          'type': 'Type',
          'language': 'Document Language',
          'title': 'Title',
          'doi': 'DOI',
        },
        'tooltip': {
          'cancel': 'Click to close this dialog without saving.',
          'change-file': 'Click to choose a file.',
          'previous-version': 'Click to restore a previous version of the metadata.',
          'save': 'Click to save this document.'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
