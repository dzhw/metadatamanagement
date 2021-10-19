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
            'data-curators': 'Data Curation',
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
            'attachment-deleted-toast': 'Datei "{{ filename }}" wurde gelöscht!',
            'attachment-order-saved-toast': 'Die geänderte Reihenfolge der Dateien wurde gespeichert.',
            'hints': {
              'filename': 'Wählen Sie eine Datei aus, die Sie des Analysepakets hinzufügen wollen.'
            }
          },
          'edit': {
            'add-package-author-tooltip': 'Klicken, um eine neue Autor:in diesem Datenpaket hinzuzufügen.',
            'delete-package-author-tooltip': 'Klicken, um die Autor:in aus diesem Analysepaket zu löschen.',
            'move-package-author-up-tooltip': 'Klicken, um die ausgewählte Autor:in nach oben zu verschieben.',
            'move-package-author-down-tooltip': 'Klicken, um die ausgewählte Autor:in nach unten zu verschieben.',
            'add-curator-tooltip': 'Klicken, um eine neue Datenkurator:in diesem Analysepaket hinzuzufügen.',
            'delete-curator-tooltip': 'Klicken, um die ausgewählte Datenkurator:in aus diesem Analysepaket zu löschen.',
            'move-curator-up-tooltip': 'Klicken, um die ausgewählte Datenkurator:in nach oben zu verschieben.',
            'move-curator-down-tooltip': 'Klicken, um die ausgewählte Datenkurator:in nach unten zu verschieben.',
            'choose-unreleased-project-toast': 'Analysepakete dürfen nur bearbeitet werden, wenn das Projekt aktuell nicht freigegeben ist!',
            'not-authorized-toast': 'Sie sind nicht berechtigt Analysepakete zu bearbeiten oder anzulegen!',
            'error-on-save-toast': 'Ein Fehler trat beim Speichern von Analysepaket {{analysisPackageId}} auf!',
            'analysis-package-has-validation-errors-toast': 'Das Analysepaket wurde nicht gespeichert, weil es noch ungültige Felder gibt!',
            'success-on-save-toast': 'Analysepaket {{analysisPackageId}} wurde erfolgreich gespeichert',
            'previous-version-restored-toast': 'Die ältere Version von Analysepaket {{ analysisPackageId }} kann jetzt gespeichert werden.',
            'current-version-restored-toast': 'Die aktuelle Version von Analysepaket {{ analysisPackageId }} wurde wiederhergestellt.',
            'choose-previous-version': {
              'next-page-tooltip': 'Klicken, um ältere Versionen anzuzeigen.',
              'previous-page-tooltip': 'Klicken, um aktuellere Versionen anzuzeigen.',
              'title': 'Ältere Version des Analysepakets {{ analysisPackageId }} wiederherstellen',
              'text': 'Wählen Sie eine ältere Analysepaketversion aus, die wiederhergestellt werden soll:',
              'cancel-tooltip': 'Klicken, um ohne eine ältere Analysepaketversion auszuwählen zurückzukehren.',
              'no-versions-found': 'Es wurden keine älteren Versionen von Analysepaket {{ analysisPackageId }} gefunden.',
              'data-package-deleted': 'Das Analysepaket wurde gelöscht!'
            },
            'hints': {
              'authors': {
                'first-name': 'Geben Sie den Vornamen der Autor:in ein.',
                'middle-name': 'Geben Sie, falls vorhanden, den zweiten Vornamen der Autor:in ein.',
                'last-name': 'Geben Sie den Nachnamen der Autor:in ein.'
              },
              'curators': {
                'first-name': 'Geben Sie den Vornamen der Person ein, die an der Datenaufbereitung beteiligt ist.',
                'middle-name': 'Geben Sie, falls vorhanden, den zweiten Vornamen der Person ein.',
                'last-name': 'Geben Sie den Nachnamen der Person ein, die an der Datenaufbereitung beteiligt ist.'
              }
            }
          },
          'not-found': 'Die id {{id}} referenziert auf eine unbekanntes Analysepaket.',
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
