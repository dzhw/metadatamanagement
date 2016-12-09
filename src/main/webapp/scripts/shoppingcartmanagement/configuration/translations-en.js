'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'notepad-management': {
        'home': {
          'title': 'Notepad',
          'empty': 'Empty',
          'send': 'Send',
          'user-name': 'Name',
          'user-name-required': 'Your name is required.',
          'user-name-too-short': 'Your name is too short.',
          'user-name-too-long': 'Your name is too long.',
          'user-email-required': 'This field is required.',
          'user-email-invalid': 'This needs to be a valid email.'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
