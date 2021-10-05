'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'analysis-package-management': {
        'detail': {
          'label': {
            'analysisPackage': 'Analysepaket',
            'analysisPackages': 'Analysepakete',
            'authors': 'Autor:innen',
            'doi': 'DOI',
            'title': 'Titel',
            'attachments': {
              'title': 'Titel',
              'description': 'Beschreibung',
              'language': 'Dokumentensprache',
              'file': 'Datei'
            }
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
