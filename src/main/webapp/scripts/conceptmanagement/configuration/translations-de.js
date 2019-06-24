'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'concept-management': {
        'detail': {
          'label': {
            'concept': 'Konzept',
            'concepts': 'Konzepte',
            'authors': 'Autor(innen)',
            'doi': 'DOI',
            'title': 'Titel',
            'citation-hint': 'Zitationshinweis',
            'license': 'Lizenz des Konzepts',
            'original-languages': 'Ursprüngliche Sprachen des Konzepts',
            'attachments': {
              'type': 'Typ',
              'title': 'Titel',
              'description': 'Beschreibung',
              'language': 'Dokumentensprache',
              'file': 'Datei'
            }
          },
          'attachments': {
            'table-title': 'Materialien zum Konzept',
            'attachment-deleted-toast': 'Datei "{{ filename }}" wurde gelöscht!',
            'delete-attachment-tooltip': 'Klicken, um die Datei "{{ filename }}" zu löschen!',
            'edit-attachment-tooltip': 'Klicken, um die Datei "{{ filename }}" zu bearbeiten.',
            'select-attachment-tooltip': 'Klicken, um Datei "{{ filename }}" zum Verschieben auszuwählen.',
            'move-attachment-up-tooltip': 'Klicken, um die ausgewählte Datei nach oben zu verschieben.',
            'move-attachment-down-tooltip': 'Klicken, um die ausgewählte Datei nach unten zu verschieben.',
            'save-attachment-order-tooltip': 'Klicken, um die geänderte Reihenfolge der Dateien zu speichern.',
            'attachment-order-saved-toast': 'Die geänderte Reihenfolge der Dateien wurde gespeichert.',
            'add-attachment-tooltip': 'Klicken, um einen neue Datei zu diesem Konzept hinzuzufügen.',
            'edit-title': 'Datei "{{ filename }}" von Konzept "{{ conceptId }}" bearbeiten',
            'create-title': 'Neue Datei zu Konzept "{{ conceptId }}" hinzufügen',
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
            'save-concept-before-adding-attachment': 'Das Konzept muss erst gespeichert werden, bevor Materialien hinzugefügt werden können.',
            'hints': {
              'filename': 'Wählen Sie eine Datei aus, die Sie dem Konzept hinzufügen wollen.',
              'type': 'Wählen Sie den Typ der Datei aus.',
              'language': 'Wählen Sie die Sprache, die in der Datei verwendet wurde, aus.',
              'title': 'Geben Sie den Titel der Datei in der Dokumentensprache ein.',
              'description': {
                'de': 'Geben Sie eine Beschreibung dieser Datei auf Deutsch ein.',
                'en': 'Geben Sie eine Beschreibung dieser Datei auf Englisch ein.'
              }
            }
          },
          'title': '{{ title }} ({{ conceptId }})',
          'description': 'Beschreibung',
          'not-found': 'Die id {{id}} referenziert auf ein unbekanntes Konzept.',
          'not-found-references': 'Die id {{id}} hat keine Referenzen auf Konzepte.',
          'tooltips': {
            'surveys': {
              'one': 'Klicken, um die Erhebung, in der dieses Konzept gemessen wurde, anzuzeigen',
              'many': 'Klicken, um alle Erhebungen, in denen dieses Konzept gemessen wurde, anzuzeigen'
            },
            'data-sets': {
              'one': 'Klicken, um den Datensatz, in dem dieses Konzept gemessen wurde, anzuzeigen',
              'many': 'Klicken, um alle Datensätze, in denen dieses Konzept gemessen wurde, anzuzeigen'
            },
            'variables': {
              'one': 'Klicken, um die Variable, mit der dieses Konzept gemessen wurde, anzuzeigen',
              'many': 'Klicken, um alle Variablen, mit denen dieses Konzept gemessen wurde, anzuzeigen'
            },
            'questions': {
              'one': 'Klicken, um die Frage, mit der dieses Konzept gemessen wurde, anzuzeigen.',
              'many': 'Klicken, um die Fragen, mit denen dieses Konzept gemessen wurde, anzuzeigen.'
            },
            'instruments': {
              'one': 'Klicken, um das Instrument, mit dem dieses Konzept gemessen wurde, anzuzeigen.',
              'many': 'Klicken, um die Instrumente, mit denen dieses Konzept gemessen wurde, anzuzeigen.'
            },
            'studies': {
              'one': 'Klicken, um die Studie, in der dieses Konzept gemessen wurde, anzuzeigen',
              'many': 'Klicken, um alle Studien, in denen dieses Konzept gemessen wurde, anzuzeigen'
            }
          },
          'doi-tooltip': 'Klicken, um die DOI in einem neuen Tab zu öffnen',
          'tag-tooltip': 'Klicken, um Konzepte mit diesem Tag zu suchen',
          'tags': 'Tags',
        },
        'log-messages': {
          'concept': {
            'saved': 'Konzept mit FDZ-ID {{ id }} erfolgreich gespeichert!',
            'not-saved': 'Konzept mit FDZ-ID {{ id }} wurde nicht gespeichert:',
            'unable-to-delete': 'Das Konzept konnte nicht gelöscht werden!'
          },
          'concept-attachment': {
            'not-saved': 'Attachment "{{ id }}" wurde nicht gespeichert:',
            'file-not-found': 'Die Datei {{ filename }} wurde nicht gefunden und wurde daher nicht gespeichert!'
          }
        },
        'error': {
          'concept': {
            'id': {
              'not-empty': 'Die FDZ-ID eines Konzepts darf nicht leer sein!',
              'size': 'Die Maximallänge der FDZ-ID ist 512 Zeichen.',
              'pattern': 'Die FDZ-ID darf keine Leerzeichen enthalten.',
              'not-valid-id': 'Die FDZ-ID des Konzepts muss der Form "con-" + {text} + "$" entsprechen, wobei {text} keine Leerzeichen enthalten darf.',
              'not-unique': 'Diese FDZ-ID existiert bereits.'
            },
            'title': {
              'not-null': 'Der Titel eines Konzepts nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge des Titels eines Konzepts ist 512 Zeichen.',
              'i18n-string-entire-not-empty': 'Der Titel eines Konzepts muss in allen Sprachen vorhanden sein.'
            },
            'description': {
              'not-null': 'Die Beschreibung eines Konzepts darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge der Beschreibung eines Konzepts ist 2048 Zeichen.',
              'i18n-string-not-empty': 'Die Beschreibung eines Konzepts muss in beiden Sprachen vorhanden sein.'
            },
            'authors': {
              'not-empty': 'Es muss mindestens ein Autor des Konzepts angegeben werden!'
            },
            'doi': {
              'size': 'Die Maximallänge der DOI des Konzepts ist 512 Zeichen.'
            },
            'license': {
              'size': 'Die Maximallänge der Lizenz des Konzepts ist 1 MB Zeichen.'
            },
            'citation-hint': {
              'not-empty': 'Es muss eine Zitationsangabe für das Konzept gemacht werden!',
              'size': 'Die Maximallänge der Zitationsangabe ist 2048 Zeichen.'
            },
            'tags': {
              'not-empty': 'In beiden Sprachen muss mindestens ein Tag angegeben werden.'
            },
            'in-use': {
              'instruments': 'Das Konzept kann nicht gelöscht werden, weil es durch die folgenden Instrumente referenziert wird: {{ids}}.',
              'questions': 'Das Konzept kann nicht gelöscht werden, weil es durch die folgenden Fragen referenziert wird: {{ids}}.'
            }
          },
          'concept-attachment-metadata': {
            'concept-id': {
              'not-empty': 'Die ID des zugehörigen Konzepts darf nicht leer sein.'
            },
            'type': {
              'not-null': 'Der Typ des Attachments darf nicht leer sein.',
              'i18n-string-size': 'Der Typ muss in beiden Sprachen angegeben werden und darf nicht länger als 32 Zeichen sein.',
              'valid-type': 'Der Typ muss einer der folgenden Werte sein: Dokumentation, Instrument, Sonstiges.'
            },
            'description': {
              'not-null': 'Die Beschreibung des Attachments darf nicht leer sein.',
              'i18n-string-size': 'Die Beschreibung muss in mindestens einer Sprache angegeben werden und darf nicht länger als 512 Zeichen sein.',
              'i18n-string-not-empty': 'Die Beschreibung muss in mindestens einer Sprache vorhanden sein.'
            },
            'title': {
              'string-size': 'Der Title des Attachments darf nicht länger als 2048 Zeichen sein.'
            },
            'language': {
              'not-null': 'Die Sprache des Attachments darf nicht leer sein.',
              'not-supported': 'Die Sprache muss eine gültige zweibuchstabige Abkürzung gemäß ISO 639-1 sein.',
              'not-valid': 'Bitte wählen Sie eine vorgeschlagene Sprache aus.'
            },
            'filename': {
              'not-empty': 'Der Dateiname des Attachments darf nicht leer sein.',
              'not-unique': 'Es gibt bereits ein Attachment mit diesem Dateinamen.',
              'not-valid': 'Der Dateiname ist ungültig.'
            }
          }
        },
        'edit': {
          'edit-page-title': 'Konzept {{conceptId}} bearbeiten',
          'create-page-title': 'Konzept {{conceptId}} anlegen',
          'success-on-save-toast': 'Konzept {{conceptId}} wurde erfolgreich gespeichert',
          'error-on-save-toast': 'Ein Fehler trat beim Speichern von Konzept {{conceptId}} auf!',
          'concept-has-validation-errors-toast': 'Das Konzept wurde nicht gespeichert, weil es noch ungültige Felder gibt!',
          'previous-version-restored-toast': 'Die ältere Version von Konzept {{ conceptId }} kann jetzt gespeichert werden.',
          'current-version-restored-toast': 'Die aktuelle Version von Konzept {{ conceptyId }} wurde wiederhergestellt.',
          'not-authorized-toast': 'Sie sind nicht berechtigt Konzepte zu bearbeiten oder anzulegen!',
          'concept-deleted-toast': 'Das Konzept {{ id }} wurde gelöscht.',
          'label': {
            'id': 'FDZ-ID',
            'edit-concept': 'Konzept bearbeiten:',
            'create-concept': 'Konzept anlegen:',
            'first-name': 'Vorname',
            'middle-name': 'Zweiter Vorname',
            'last-name': 'Nachname',
            'tags': 'Tags (Schlüsselwörter) zum Konzept'
          },
          'open-choose-previous-version-tooltip': 'Klicken, um eine ältere Version dieses Konzepts wieder herzustellen.',
          'save-tooltip': 'Klicken, um das Konzept zu speichern.',
          'move-author-up-tooltip': 'Klicken, um den ausgewählten Autor nach oben zu verschieben.',
          'move-author-down-tooltip': 'Klicken, um den ausgewählten Autor nach unten zu verschieben.',
          'add-author-tooltip': 'Klicken, um einen neuen Autor diesem Konzept hinzuzufügen.',
          'delete-author-tooltip': 'Klicken, um den ausgewählten Autor dieses Konzept zu löschen.',
          'choose-previous-version': {
            'next-page-tooltip': 'Klicken, um ältere Versionen anzuzeigen.',
            'previous-page-tooltip': 'Klicken, um aktuellere Versionen anzuzeigen.',
            'title': 'Ältere Version des Konzepts {{ conceptId }} wiederherstellen',
            'text': 'Wählen Sie eine ältere Konzeptversion aus, die wiederhergestellt werden soll:',
            'cancel-tooltip': 'Klicken, um, ohne eine ältere Konzeptversion auszuwählen, zurückzukehren.',
            'no-versions-found': 'Es wurden keine älteren Versionen von Konzept {{ conceptId }} gefunden.',
            'concept-title': 'Titel',
            'lastModified': 'Geändert',
            'lastModifiedBy': 'von',
            'current-version-tooltip': 'Dies ist die aktuelle Version!'
          },
          'hints': {
            'id': 'Geben Sie die ID dieses Konzeptes in unserem FDZ an.',
            'doi': 'Bitte geben Sie die DOI für dieses Konzept an (falls verfügbar).',
            'license': 'Bitte geben die Lizenz an, unter der dieses Konzept veröffentlicht wurde.',
            'title': {
              'de': 'Geben Sie den Titel des Konzepts auf Deutsch ein.',
              'en': 'Geben Sie den Titel des Konzepts auf Englisch ein.'
            },
            'citation-hint': 'Geben Sie an, wie dieses Konzept zitiert werden soll.',
            'description': {
              'de': 'Geben Sie eine Beschreibung des Konzepts auf Deutsch ein.',
              'en': 'Geben Sie eine Beschreibung des Konzepts auf Englisch ein.'
            },
            'authors': {
              'first-name': 'Geben Sie den Vornamen des Autors ein.',
              'middle-name': 'Geben Sie, falls vorhanden, den zweiten Vornamen des Autors ein.',
              'last-name': 'Geben Sie den Nachnamen des Autors ein.'
            }
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
