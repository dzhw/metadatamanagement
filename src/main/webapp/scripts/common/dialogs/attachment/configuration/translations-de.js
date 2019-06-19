'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'attachment': {
        'create-title': 'Datei anlegen',
        'edit-title': 'Datei bearbeiten',
        'attachment-saved-toast': 'Datei "{{ filename }}" wurde gespeichert.',
        'error': {
          'attachment-has-validation-errors-toast': 'Die Datei wurde nicht gespeichert, weil es noch ungültige Felder gibt.',
          'description': {
            'i18n-string-not-empty': 'Die Beschreibung muss in mindestens einer Sprache vorhanden sein.',
            'i18n-string-size': 'Die Beschreibung muss in mindestens einer Sprache angegeben werden und darf nicht länger als 512 Zeichen sein.'
          },
          'filename': {
            'not-empty': 'Es muss eine Datei ausgewählt sein',
            'not-unique': 'Eine Datei mit diesem Namen existiert bereits',
            'not-valid': 'Dieser Dateiname ist ungültig',
            'pattern': 'Es dürfen für die FDZ-ID nur alphanumerische Zeichen, deutsche Umlaute, ß, Minus und der Unterstrich verwendet werden.'
          },
          'file-not-found': 'Die Datei {{ filename }} wurde nicht gefunden und wurde daher nicht gespeichert!',
          'type': {
            'not-null': 'Der Typ des Attachments darf nicht leer sein.'
          },
          'language': {
            'not-found': 'Keine gültige Sprache gefunden!',
            'not-null': 'Die Sprache des Attachments darf nicht leer sein.',
            'not-valid': 'Bitte wählen Sie eine vorgeschlagene Sprache aus.'
          },
          'title': {
            'maxlength': 'Der Titel des Attachments darf nicht länger als 2048 Zeichen sein.'
          }
        },
        'hint': {
          'filename': 'Wählen Sie eine Datei aus, die sie hinzufügen wollen',
          'language': 'Wählen Sie die Sprache, die in der Datei verwendet wurde, aus.',
          'type': 'Wählen Sie den Typ der Datei aus.',
          'title': 'Geben Sie den Titel der Datei in der Dokumentensprache ein.',
          'description': {
            'de': 'Geben Sie eine Beschreibung dieser Datei auf Deutsch ein.',
            'en': 'Geben Sie eine Beschreibung dieser Datei auf Englisch ein.'
          }
        },
        'label': {
          'description': 'Beschreibung',
          'file': 'Datei',
          'type': 'Typ',
          'language': 'Dokumentensprache',
          'title': 'Titel'
        },
        'tooltip': {
          'cancel': 'Klicken, um den Dialog ohne zu speichern zu schließen.',
          'change-file': 'Klicken, um eine Datei auszuwählen.',
          'save': 'Klicken, um die Datei zu speichern.'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
