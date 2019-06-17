'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'instrument-management': {
        'log-messages': {
          'instrument': {
            'saved': 'Instrument mit FDZ-ID {{ id }} erfolgreich gespeichert!',
            'not-saved': 'Instrument mit FDZ-ID {{ id }} wurde nicht gespeichert:',
            'missing-number': 'Das Instrument im Excel Document aus dem Arbeitsblatt "instruments"  in der Zeile {{ index }} enthält keine Nummer und wurde nicht gespeichert!',
            'upload-terminated': 'Upload von {{ totalInstruments }} Instrumenten und {{ totalAttachments }} Attachments mit {{totalWarnings}} Warnungen und {{ totalErrors }} Fehlern beendet!',
            'unable-to-delete': 'Die Instrumente konnten nicht gelöscht werden!',
            'cancelled': 'Upload von Instrumenten Abgebrochen!',
            'duplicate-instrument-number': 'Die Nummer ({{ number }}) des Instrumentes aus der Exceldatei aus dem Arbeitsblatt "instruments" in der Zeile {{ index }} wurde bereits verwendet.'
          },
          'instrument-attachment': {
            'not-saved': 'Attachment "{{ id }}" wurde nicht gespeichert:',
            'missing-instrument-number': 'Das Instrument Attachment aus dem Exceldocument aus dem Arbeitsblatt "attachments" in der Zeile {{ index }} hat keine Instrumentnummer und wurde daher nicht gespeichert.',
            'unknown-instrument-number': 'Die Nummer des Instrumentes des Attachments aus der Exceldatei in dem Arbeitsblatt "attachments" in der Zeile {{index}} gibt es nicht. Das Attachment wurde daher nicht gespeichert.',
            'missing-filename': 'Das Attachment eines Instrumentes aus der Exceldatei aus dem Arbeitsblatt "attachments" in der Zeile {{index}} hat keinen Dateinamen und wurde daher nicht gespeichert.',
            'file-not-found': 'Die Datei {{ filename }} wurde nicht gefunden und wurde daher nicht gespeichert!'
          }
        },
        'home': {
          'title': 'Instrumente'
        },
        'detail': {
          'label': {
            'instrument': 'Instrument',
            'instruments': 'Instrumente',
            'description': 'Beschreibung',
            'title': 'Titel',
            'subtitle': 'Untertitel',
            'type': 'Typ',
            'annotations': 'Anmerkungen',
            'attachments': {
              'type': 'Typ',
              'description': 'Beschreibung',
              'language': 'Dokumentensprache',
              'file': 'Datei'
            }
          },
          'attachments': {
            'table-title': 'Materialien zum Instrument',
            'attachment-deleted-toast': 'Datei "{{ filename }}" wurde gelöscht!',
            'delete-attachment-tooltip': 'Klicken, um die Datei "{{ filename }}" zu löschen!',
            'edit-attachment-tooltip': 'Klicken, um die Datei "{{ filename }}" zu bearbeiten.',
            'select-attachment-tooltip': 'Klicken, um Datei "{{ filename }}" zum Verschieben auszuwählen.',
            'move-attachment-up-tooltip': 'Klicken, um die ausgewählte Datei nach oben zu verschieben.',
            'move-attachment-down-tooltip': 'Klicken, um die ausgewählte Datei nach unten zu verschieben.',
            'save-attachment-order-tooltip': 'Klicken, um die geänderte Reihenfolge der Dateien zu speichern.',
            'attachment-order-saved-toast': 'Die geänderte Reihenfolge der Dateien wurde gespeichert.',
            'add-attachment-tooltip': 'Klicken, um einen neue Datei zu diesem Instrument hinzuzufügen.',
            'edit-title': 'Datei "{{ filename }}" von Instrument "{{ instrumentId }}" bearbeiten',
            'create-title': 'Neue Datei zu Instrument "{{ instrumentId }}" hinzufügen',
            'cancel-tooltip': 'Klicken, um den Dialog ohne zu speichern zu schließen.',
            'save-tooltip': 'Klicken, um die Datei zu speichern.',
            'attachment-saved-toast': 'Datei "{{ filename }}" wurde gespeichert.',
            'attachment-has-validation-errors-toast': 'Die Datei wurde nicht gespeichert, weil es noch ungültige Felder gibt.',
            'change-file-tooltip': 'Klicken, um eine Datei auszuwählen.',
            'open-choose-previous-version-tooltip': 'Klicken, um eine ältere Version der Metadaten wiederherzustellen.',
            'current-version-restored-toast': 'Die aktuelle Version der Metadaten von Datei "{{ filename }}" wurde wiederhergestellt.',
            'previous-version-restored-toast': 'Die ältere Version der Metadaten von Datei "{{ filename }}" kann jetzt gespeichert werden.',
            'choose-previous-version': {
              'title': 'Ältere Version der Metadaten zu Datei "{{ filename }}" wiederherstellen',
              'text': 'Wählen Sie eine ältere Version der Metadaten zu Datei "{{ filename }}" aus, die wiederhergestellt werden soll:',
              'attachment-description': 'Beschreibung (auf Deutsch)',
              'lastModified': 'Geändert',
              'lastModifiedBy': 'von',
              'cancel-tooltip': 'Klicken, um ohne eine ältere Version der Metadaten auszuwählen zurückzukehren.',
              'current-version-tooltip': 'Dies ist die aktuelle Version!',
              'next-page-tooltip': 'Klicken, um ältere Versionen anzuzeigen.',
              'previous-page-tooltip': 'Klicken, um aktuellere Versionen anzuzeigen.',
              'attachment-deleted': 'Metadaten wurden gelöscht!',
              'no-versions-found': 'Es wurden keine älteren Versionen der Metadaten gefunden.'
            },
            'language-not-found': 'Keine gültige Sprache gefunden!',
            'save-instrument-before-adding-attachment': 'Das Instrument muss erst gespeichert werden, bevor Materialien hinzugefügt werden können.',
            'hints': {
              'filename': 'Wählen Sie eine Datei aus, die Sie dem Instrument hinzufügen wollen.',
              'language': 'Wählen Sie die Sprache, die in der Datei verwendet wurde, aus.',
              'type': 'Wählen Sie den Typ der Datei aus.',
              'description': {
                'de': 'Geben Sie eine Beschreibung dieser Datei auf Deutsch ein.',
                'en': 'Geben Sie eine Beschreibung dieser Datei auf Englisch ein.'
              }
            }
          },
          'page-title': '{{ description }} ({{ instrumentId }})',
          'not-released-toast': 'Das Instrument "{{ id }}" wurde noch nicht für alle Benutzer freigegeben!',
          'tooltips': {
            'surveys': {
              'one': 'Klicken, um die Erhebung anzuzeigen, bei der dieses Instrument verwendet wurde',
              'many': 'Klicken, um alle Erhebungen anzuzeigen, bei denen dieses Instrument verwendet wurde'
            },
            'publications': {
              'one': 'Klicken, um die Publikation zu diesem Instrument anzuzeigen',
              'many': 'Klicken, um alle Publikationen zu diesem Instrument anzuzeigen'
            },
            'questions': {
              'one': 'Klicken, um die Frage dieses Instrumentes anzuzeigen',
              'many': 'Klicken, um alle Fragen dieses Instrumentes anzuzeigen'
            },
            'studies': {
              'one': 'Klicken, um die Studie anzuzeigen, in der dieses Instrument verwendet wurde'
            },
            'concepts': {
              'one': 'Klicken, um das Konzept, welches mit diesem Instrument gemessen wurde, anzuzeigen',
              'many': 'Klicken, um alle Konzepte, die mit diesem Instrument gemessen wurden, anzuzeigen'
            },
          }
        },
        'error': {
          'instrument': {
            'id': {
              'not-empty': 'Die FDZ-ID des Instruments darf nicht leer sein!',
              'size': 'Die Maximallänge der FDZ-ID ist 512 Zeichen.',
              'pattern': 'Es dürfen nur alphanumerische Zeichen, deutsche Umlaute, ß, Leerzeichen, Ausrufezeichen und Minus für die FDZ-ID verwendet werden.'
            },
            'title': {
              'not-null': 'Der Titel des Instruments darf nicht leer sein!',
              'i18n-string-size': 'Der Titel darf nicht länger als 2048 Zeichen sein.',
              'i18n-string-not-empty': 'Der Titel muss in mind. einer Sprache angegeben werden.'
            },
            'subtitle': {
              'i18n-string-size': 'Der Untertitel darf nicht länger als 2048 Zeichen sein.',
            },
            'description': {
              'not-null': 'Die Beschreibung des Instruments darf nicht leer sein!',
              'i18n-string-size': 'Die Beschreibung muss in beiden Sprachen angegeben werden und darf nicht länger als 512 Zeichen sein.',
              'i18n-string-not-empty': 'Die Beschreibung darf nicht leer sein.'
            },
            'annotations': {
              'i18n-string-size': 'Die Maximallänge der Anmerkungen ist 2048 Zeichen.'
            },
            'type': {
              'not-empty': 'Der Typ des Instruments darf nicht leer sein!',
              'valid': 'Der Typ des Instruments muss einer der folgenden sein: PAPI, CAPI, CATI, CAWI'
            },
            'data-acquisition-project-id': {
              'not-empty': 'Die ID des Datenaufbereitungsprojektes darf nicht leer sein!'
            },
            'instrument.unique-instrument-number': 'Die Nummer eines Erhebungsinstruments muss eindeutig innerhalb eines Datenaufbereitungsprojektes sein!',
            'survey-numbers.not-empty': 'Die Liste der Erhebungen darf nicht leer sein!',
            'number': {
              'not-null': 'Die Nummer des Erhebungsinstruments darf nicht leer sein!'
            },
            'survey-id': {
              'not-empty': 'Die ID der zugehörigen Erhebung darf nicht leer sein!'
            },
            'valid-instrument-id-pattern': 'Die FDZ-ID des Instruments hat nicht die folgende Form: "ins-" + {ProjektId} + "-" + "ins" + {Nummer} + "$" .'
          },
          'instrument-attachment-metadata': {
            'instrument-id': {
              'not-empty': 'Die ID des zugehörigen Instrumentes darf nicht leer sein.'
            },
            'instrument-number': {
              'not-null': 'Die Nummer des zugehörigen Instrumentes darf nicht leer sein.'
            },
            'project-id': {
              'not-empty': 'Die ID des zugehörigen Datenaufbereitungsprojektes darf nicht leer sein.'
            },
            'type': {
              'not-null': 'Der Typ des Attachments darf nicht leer sein.',
              'i18n-string-size': 'Der Typ muss in beiden Sprachen angegeben werden und darf nicht länger als 32 Zeichen sein.',
              'valid-type': 'Der Typ muss einer der folgenden Werte sein: Fragebogen, Filterführungsdiagramm, Variablenfragebogen, Sonstige.'
            },
            'description': {
              'not-null': 'Die Beschreibung des Attachments darf nicht leer sein.',
              'i18n-string-size': 'Die Beschreibung muss in mindestens einer Sprache angegeben werden und darf nicht länger als 512 Zeichen sein.',
              'i18n-string-not-empty': 'Die Beschreibung darf nicht leer sein.'
            },
            'language': {
              'not-null': 'Die Sprache des Attachments darf nicht leer sein.',
              'not-supported': 'Die Sprache muss eine gültige zweibuchstabige Abkürzung gemäß ISO 639-1 sein.'
            },
            'filename': {
              'not-empty': 'Der Dateiname des Attachments darf nicht leer sein.',
              'not-unique': 'Es gibt bereits ein Attachment mit diesem Dateinamen.'
            }
          },
          'post-validation': {
            'instrument-has-invalid-survey-id': 'Das Instrument {{id}} referenziert eine unbekannte Erhebung ({{toBereferenzedId}}).',
            'no-instruments': 'Es sind keine Instrumente vorhanden.'
          }
        },
        'edit': {
          'edit-page-title': 'Instrument {{instrumentId}} bearbeiten',
          'create-page-title': 'Instrument {{instrumentId}} anlegen',
          'success-on-save-toast': 'Instrument {{instrumentId}} wurde erfolgreich gespeichert.',
          'error-on-save-toast': 'Ein Fehler trat beim Speichern von Instrument {{instrumentId}} auf!',
          'instrument-has-validation-errors-toast': 'Das Instrument wurde nicht gespeichert, weil es noch ungültige Felder gibt!',
          'previous-version-restored-toast': 'Die ältere Version von Instrument {{ instrumentId }} kann jetzt gespeichert werden.',
          'current-version-restored-toast': 'Die aktuelle Version von Instrument {{ instrumentId }} wurde wiederhergestellt.',
          'not-authorized-toast': 'Sie sind nicht berechtigt Instrumente zu bearbeiten oder anzulegen!',
          'choose-unreleased-project-toast': 'Instrumente dürfen nur bearbeitet werden, wenn das Projekt aktuell nicht freigegeben ist!',
          'instrument-deleted-toast': 'Das Instrument {{ id }} wurde gelöscht.',
          'label': {
            'edit-instrument': 'Instrument bearbeiten:',
            'create-instrument': 'Instrument anlegen:',
            'surveys': 'Erhebungen *',
            'concepts': 'Konzepte'
          },
          'open-choose-previous-version-tooltip': 'Klicken, um eine ältere Version dieses Instruments wieder herzustellen.',
          'save-tooltip': 'Klicken, um das Instrument zu speichern.',
          'choose-previous-version': {
            'next-page-tooltip': 'Klicken, um ältere Versionen anzuzeigen.',
            'previous-page-tooltip': 'Klicken, um aktuellere Versionen anzuzeigen.',
            'title': 'Ältere Version des Instruments {{ instrumentId }} wiederherstellen',
            'text': 'Wählen Sie eine ältere Version des Instruments aus, das wiederhergestellt werden soll:',
            'cancel-tooltip': 'Klicken, um ohne eine ältere Version des Instruments auszuwählen zurückzukehren.',
            'no-versions-found': 'Es wurden keine älteren Versionen des Instruments {{ instrumentId }} gefunden.',
            'instrument-description': 'Beschreibung',
            'lastModified': 'Geändert',
            'lastModifiedBy': 'von',
            'current-version-tooltip': 'Dies ist die aktuelle Version!',
            'instrument-deleted': 'Das Instrument wurde gelöscht!'
          },
          'choose-instrument-number': {
            'title': 'Auswahl einer freien Instrumentnummer',
            'label': 'Freie Instrumentnummern',
            'ok-tooltip': 'Klicken, um die Auswahl der Instrumentnummer zu bestätigen.'
          },
          'hints': {
            'description': {
              'de': 'Geben Sie eine kurze Beschreibung für das Instrument auf Deutsch ein.',
              'en': 'Geben Sie eine kurze Beschreibung für das Instrument auf Englisch ein.'
            },
            'title': {
              'de': 'Geben Sie den Titel des Instruments auf Deutsch ein.',
              'en': 'Geben Sie den Titel des Instruments auf Englisch ein.'
            },
            'subtitle': {
              'de': 'Geben Sie den Untertitel des Instruments auf Deutsch ein.',
              'en': 'Geben Sie den Untertitel des Instruments auf Englisch ein.'
            },
            'type': 'Wählen Sie den Typ des Instruments aus.',
            'surveys': 'Wählen Sie die Erhebungen aus, in denen dieses Instrument verwendet wurde.',
            'concepts': 'Wählen Sie die Konzepte aus, die mit diesem Instrument gemessen wurden.',
            'search-surveys': 'Erhebungen suchen...',
            'search-concepts': 'Konzepte suchen...',
            'no-surveys-found': 'Keine (weiteren) Erhebungen gefunden.',
            'no-concepts-found': 'Keine (weiteren) Konzepte gefunden.',
            'annotations': {
              'de': 'Geben Sie zusätzliche Anmerkungen zu dem Instrument hier auf Deutsch an.',
              'en': 'Geben Sie zusätzliche Anmerkungen zu dem Instrument hier auf Englisch an.',
            },
            'instrument-number': 'Wählen Sie eine freie Nummer für das neue Instrument aus.'
          },
          'all-instruments-deleted-toast': 'Alle Instrumente des Datenaufbereitungsprojekt "{{id}}" wurden gelöscht.'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
