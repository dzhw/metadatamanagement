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
          },
          'attachments': {
            'create-title': 'Neue Datei zu Analysepaket "{{ analysisPackageId }}" hinzufügen',
            'edit-title': 'Datei "{{ filename }}" von Analysepaket "{{ analysisPackageId }}" bearbeiten',
            'hints': {
              'filename': 'Wählen Sie eine Datei aus, die Sie des Analysepakets hinzufügen wollen.'
            }
          },
          'not-found': 'Die id {{id}} referenziert auf eine unbekanntes Analysepaket.',
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
