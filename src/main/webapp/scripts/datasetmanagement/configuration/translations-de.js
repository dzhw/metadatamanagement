'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'data-set-management': {
        'log-messages': {
          'data-set': {
            'saved': 'Datensatz mit FDZ-ID {{ id }} erfolgreich gespeichert!',
            'not-saved': 'Datensatz mit FDZ-ID {{ id }} wurde nicht gespeichert!',
            'missing-id': 'Der Datensatz im Exceldokument im Arbeitsblatt "dataSets" in Zeile {{ index }} enthält keine FDZ-ID und wurde nicht gespeichert!',
            'upload-terminated': 'Upload von {{ total }} Datensätzen und {{ attachments }} Attachments mit {{warnings}} Warnungen und {{ errors }} Fehlern beendet!',
            'cancelled': 'Upload von Datensätzen Abgebrochen',
            'unable-to-delete': 'Die Datensätze konnten nicht gelöscht werden!',
            'duplicate-data-set-number': 'Die Nummer ({{ number }}) des Datensatzes im Excel Dokument im Arbeitsblatt "dataSets" in Zeile {{ index }} wurde bereits verwendet.',
            'sub-data-set': {
              'number-of-observations-parse-error': 'Anzahl der Beobachtungen des Sub-Datensatzes {{name}} ist keine Zahl!'
            }
          },
          'data-set-attachment': {
            'unknown-data-set-number': 'Ein Attachment der Datensätze aus der Zeile {{index}} des Arbeitsblattes "attachments" im Excel Dokument verweist auf eine unbekannte Datensatznummer: {{dataSetNumber}}.'
          },
          'sub-data-set': {
            'unknown-data-set-number': 'Der Subdatensatz aus der Zeile {{index}} in dem Arbeitsblatt "subDataSets" des Excel Dokumentes verweist auf eine unbekannte Datensatznummer: {{dataSetNumber}}.'
          }
        },
        'home': {
          'title': 'Datensätze'
        },
        'detail': {
          'label': {
            'data-set': 'Datensatz',
            'data-sets': 'Datensätze',
            'description': 'Beschreibung',
            'type': 'Typ',
            'format': 'Format',
            'annotations': 'Anmerkungen',
            'data-set-same-data-package': 'Datensätze des gleichen Datenpakets',
            'languages': 'Daten verfügbar auf',
            'sub-data-sets': {
              'name': 'Name',
              'data-formats': 'Datenformate',
              'accessWay': 'Zugangsweg',
              'description': 'Beschreibung',
              'numberOfAnalyzedVariables': 'Analysierbare Variablen',
              'unknownNumberOfAnalyzedVariables': 'Nicht bekannt',
              'numberOfAnalyzedVariables-tooltip': 'Klicken, um alle analysierbaren Variablen dieses Subdatensatzes anzuzeigen',
              'numberOfObservations': 'Fälle',
              'numberOfEpisodes': 'Episoden'
            },
            'attachments': {
              'title': 'Titel',
              'description': 'Beschreibung',
              'language': 'Dokumentensprache',
              'file': 'Datei'
            }
          },
          'sub-data-sets': {
            'title': 'Verfügbare Subdatensätze',
            'data-formats': {
              'Stata': 'Stata',
              'R': 'R',
              'SPSS': 'SPSS',
              'Word': 'Word',
              'CSV': 'CSV'
            }
          },
          'attachments': {
            'table-title': 'Materialien zu dem Datensatz',
            'attachment-deleted-toast': 'Datei "{{ filename }}" wurde gelöscht!',
            'delete-attachment-tooltip': 'Klicken, um die Datei "{{ filename }}" zu löschen!',
            'edit-attachment-tooltip': 'Klicken, um die Datei "{{ filename }}" zu bearbeiten.',
            'select-attachment-tooltip': 'Klicken, um Datei "{{ filename }}" zum Verschieben auszuwählen.',
            'move-attachment-up-tooltip': 'Klicken, um die ausgewählte Datei nach oben zu verschieben.',
            'move-attachment-down-tooltip': 'Klicken, um die ausgewählte Datei nach unten zu verschieben.',
            'save-attachment-order-tooltip': 'Klicken, um die geänderte Reihenfolge der Dateien zu speichern.',
            'attachment-order-saved-toast': 'Die geänderte Reihenfolge der Dateien wurde gespeichert.',
            'add-attachment-tooltip': 'Klicken, um einen neue Datei zu diesem Datensatz hinzuzufügen.',
            'edit-title': 'Datei "{{ filename }}" von Datensatz "{{ dataSetId }}" bearbeiten',
            'create-title': 'Neue Datei zu Datensatz "{{ dataSetId }}" hinzufügen',
            'change-file-tooltip': 'Klicken, um eine Datei auszuwählen.',
            'open-choose-previous-version-tooltip': 'Klicken, um eine ältere Version der Metadaten wiederherzustellen.',
            'current-version-restored-toast': 'Die aktuelle Version der Metadaten von Datei "{{ filename }}" wurde wiederhergestellt.',
            'previous-version-restored-toast': 'Die ältere Version der Metadaten von Datei "{{ filename }}" kann jetzt gespeichert werden.',
            'language-not-found': 'Keine gültige Sprache gefunden!',
            'save-data-set-before-adding-attachment': 'Der Datensatz muss erst gespeichert werden, bevor Materialien hinzugefügt werden können.',
            'hints': {
              'filename': 'Wählen Sie eine Datei aus, die Sie dem Datensatz hinzufügen wollen.'
            }
          },
          'title': '{{ description }}',
          'description': 'Ein Datensatz kann Daten aus mehreren Erhebungen enthalten.',
          'not-found': 'Die id {{id}} referenziert auf einen unbekannten Datensatz.',
          'not-found-references': 'Die id {{id}} hat keine Referenzen auf Datensätze.',
          'content': {
            'true': 'Klicken zum maximieren',
            'false': 'Klicken zum minimieren'
          },
          'generate-dataset-report-tooltip': 'Klicken, um den Datensatzreport für diesen Datensatz zu erzeugen',
          'not-released-toast': 'Der Datensatz "{{ id }}" wurde noch nicht für alle Benutzer:innen freigegeben!',
          'tooltips': {
            'data-sets': {
              'same-data-sets': 'Klicken, um alle Datensätze dieses Datenpakets anzuzeigen'
            },
            'surveys': {
              'one': 'Klicken, um die Erhebung anzuzeigen, aus der dieser Datensatz resultierte',
              'many': 'Klicken, um alle Erhebungen anzuzeigen, aus denen dieser Datensatz resultierte'
            },
            'publications': {
              'one': 'Klicken, um die Publikation zu diesem Datensatz anzuzeigen',
              'many': 'Klicken, um alle Publikationen zu diesem Datensatz anzuzeigen'
            },
            'variables': {
              'one': 'Klicken, um die Variable dieses Datensatzes anzuzeigen',
              'many': 'Klicken, um alle Variablen dieses Datensatzes anzuzeigen'
            },
            'concepts': {
              'one': 'Klicken, um das Konzept, welches in diesem Datensatzes gemessen wurde, anzuzeigen',
              'many': 'Klicken, um alle Konzepte, die in diesem Datensatzes gemessen wurden, anzuzeigen'
            },
            'data-packages': {
              'one': 'Klicken, um das Datenpaket dieses Datensatzes anzuzeigen'
            },
            'get-data-set-tooltip': 'Klicken, um Informationen zum Datenzugang zu erhalten'
          },
          'report-generation-started-toast': 'Der Datensatzreport wird jetzt erzeugt. Sie werden per E-Mail benachrichtigt, sobald der Vorgang abgeschlossen ist.'
        },
        'error': {
          'data-set': {
            'unique-data-set-number-in-project': 'Die Nummer des Datensatzes ist innerhalb des Datenpakets nicht eindeutig.',
            'id': {
              'valid-data-set-id-name': 'Die FDZ-ID des Datensatzes entspricht nicht dem Muster: "dat-" + {FDZID} + "-ds" + {Number} + "$".',
              'not-empty': 'Die FDZ-ID des Datensatzes darf nicht leer sein!',
              'size': 'Die Maximallänge der FDZ-ID ist 512 Zeichen.',
              'pattern': 'Die FDZ-ID darf nur alphanumerische Zeichen, deutsche Umlaute, Ausrufezeichen und ß beinhalten.'
            },
            'description': {
              'i18n-string-size': 'Die Maximallänge der Datensatzbeschreibung ist 2048 Zeichen.',
              'i18n-string-not-empty': 'Die Datensatzbeschreibung muss in mindestens einer Sprache angegeben werden.'
            },
            'annotations': {
              'i18n-string-size': 'Die Maximallänge der Anmerkungen ist 2048 Zeichen.'
            },
            'data-acquisition-project': {
              'id': {
                'not-empty': 'Die FDZ - ID des Projektes darf bei dem Datensatz nicht leer sein!'
              }
            },
            'data-package': {
              'id': {
                'not-empty': 'Die Datenpaket-ID darf bei dem Datensatz nicht leer sein!'
              }
            },
            'survey-numbers': {
              'not-empty': 'Der Datensatz muss mindestens einer Erhebung zugeordnet sein!'
            },
            'number': {
              'not-null': 'Die Nummer des Datensatzes darf nicht leer sein!'
            },
            'format': {
              'valid-format': 'Das Format für einen Datensatz darf nur die Werte: breit und lang annehmen.'
            },
            'survey': {
              'ids': {
                'not-empty': 'Der Datensatz muss zu mindestens einer Erhebung gehören!'
              }
            },
            'sub-data-sets': {
              'not-empty': 'Der Datensatz muss mindestens einen Sub-Datensatz haben!',
              'access-way-unique-within-data-set': 'Der Zugangsweg des Subdatensatzes muss eindeutig innerhalb eines Datensatzes sein.'
            },
            'type': {
              'valid-type': 'Der Typ des Datensatzes ist ungültig. Erlaubt im deutschen sind: Personendatensatz oder Episodendatensatz. Der Typ kann auch in anderen Sprachen fehlerhaft sein.',
              'not-null': 'Der Typ des Datensatzes darf nicht leer sein!'
            }
          },
          'sub-data-set': {
            'name': {
              'not-empty': 'Der Name eines Subdatensatz darf nicht leer sein!',
              'size': 'Die Maximallänge des Namens eines Subdatensatz ist 32 Zeichen.'
            },
            'description': {
              'i18n-string-not-empty': 'Die Beschreibung eines Subdatensatz darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge der Beschreibung eines Subdatensatz ist 512 Zeichen.'
            },
            'access-way': {
              'not-null': 'Der Zugangsweg eines Subdatensatz darf nicht leer sein!',
              'valid-access-way': 'Der Zugangsweg eines Subdatensatz ist ungültig. Erlaubt sind nur: download-cuf, download-suf, remote-desktop-suf oder onsite-suf.'
            },
            'number-of-observations': {
              'not-null': 'Die Anzahl von Fälle/Episoden darf bei einem Subdatensatz nicht leer sein!',
              'invalid-number': 'Geben Sie eine positive ganze Zahl (kleiner als 2.147.483.648) an!'
            },
            'data-formats': {
              'required': 'Es muss mindestens ein Datenformat angegeben werden.'
            }
          },
          'post-validation': {
            'data-set-has-invalid-survey-id': 'Der Datensatz {{id}} referenziert eine unbekannte Erhebung({{toBereferenzedId}}).'
          }
        },
        'edit': {
          'edit-page-title': 'Datensatz {{dataSetId}} bearbeiten',
          'create-page-title': 'Datensatz {{dataSetId}} anlegen',
          'success-on-save-toast': 'Datensatz {{dataSetId}} wurde erfolgreich gespeichert.',
          'error-on-save-toast': 'Ein Fehler trat beim Speichern von Datensatz {{dataSetId}} auf!',
          'data-set-has-validation-errors-toast': 'Der Datensatz wurde nicht gespeichert, weil es noch ungültige Felder gibt!',
          'previous-version-restored-toast': 'Die ältere Version von Datensatz {{ dataSetId }} kann jetzt gespeichert werden.',
          'current-version-restored-toast': 'Die aktuelle Version von Datensatz {{ dataSetId }} wurde wiederhergestellt.',
          'not-authorized-toast': 'Sie sind nicht berechtigt Datensätze zu bearbeiten oder anzulegen!',
          'choose-unreleased-project-toast': 'Datensätze dürfen nur bearbeitet werden, wenn das Projekt aktuell nicht freigegeben ist!',
          'data-set-deleted-toast': 'Der Datensatz {{ id }} wurde gelöscht.',
          'delete-sub-data-set-tooltip': 'Klicken, um den Subdatensatz zu löschen',
          'add-sub-data-set-tooltip': 'Klicken, um einen neuen Subdatensatz hinzuzufügen',
          'move-sub-data-set-up-tooltip': 'Klicken, um den ausgewählten Subdatensatz nach oben zu verschieben',
          'move-sub-data-set-down-tooltip': 'Klicken, um den ausgewählten Subdatensatz nach oben zu verschieben',
          'label': {
            'edit-data-set': 'Datensatz bearbeiten:',
            'create-data-set': 'Datensatz anlegen:',
            'surveys': 'Erhebungen *',
            'sub-data-set': {
              'name': 'Name',
              'access-way': 'Zugangsweg',
              'number-of-observations': 'Anzahl Fälle/Episoden',
              'description': 'Beschreibung',
              'data-formats': 'Verfügbare Datenformate'
            }
          },
          'open-choose-previous-version-tooltip': 'Klicken, um eine ältere Version dieses Datensatzes wieder herzustellen.',
          'save-tooltip': 'Klicken, um den Datensatz zu speichern.',
          'choose-previous-version': {
            'title': 'Ältere Version des Datensatzes {{ dataSetId }} wiederherstellen',
            'text': 'Wählen Sie eine ältere Version des Datensatzes aus, die wiederhergestellt werden soll:',
            'cancel-tooltip': 'Klicken, um, ohne eine ältere Version des Datensatzes auszuwählen, zurückzukehren.',
            'no-versions-found': 'Es wurden keine älteren Versionen des Datensatzes {{ dataSetId }} gefunden.',
            'data-set-deleted': 'Der Datensatz wurde gelöscht!'
          },
          'choose-data-set-number': {
            'title': 'Auswahl einer freien Datensatznummer',
            'label': 'Freie Datensatznummern',
            'ok-tooltip': 'Klicken, um die Auswahl der Datensatznummer zu bestätigen.'
          },
          'hints': {
            'description': {
              'de': 'Geben Sie eine kurze Beschreibung für den Datensatz auf Deutsch ein.',
              'en': 'Geben Sie eine kurze Beschreibung für den Datensatz auf Englisch ein.'
            },
            'format': 'Wählen Sie das Datensatzformat aus.',
            'type': 'Geben Sie an ob es sich um einen Personen- oder Episodendatensatz handelt.',
            'surveys': 'Wählen Sie die Erhebungen aus, aus denen dieser Datensatz resultiert.',
            'search-surveys': 'Erhebungen suchen...',
            'no-surveys-found': 'Keine (weiteren) Erhebungen gefunden.',
            'annotations': {
              'de': 'Geben Sie zusätzliche Anmerkungen zu dem Datensatz hier auf Deutsch an.',
              'en': 'Geben Sie zusätzliche Anmerkungen zu dem Datensatz hier auf Englisch an.',
            },
            'data-set-number': 'Wählen Sie eine freie Nummer für den neuen Datensatz aus.',
            'sub-data-set': {
                'name': 'Geben Sie den (Datei-)namen des Subdatensatzes an.',
                'access-way': 'Auf welchem Weg steht der Subdatensatz zur Verfügung?',
                'number-of-observations': 'Wieviele Fälle bzw. Epsioden enthält der Subdatensatz?',
                'description': {
                  'de': 'Geben Sie eine kurze Beschreibung für den Subdatensatz auf Deutsch ein.',
                  'en': 'Geben Sie eine kurze Beschreibung für den Subdatensatz auf Englisch ein.'
                },
                'data-formats': 'Geben Sie die Datenformate an, in denen der Subdatensatz verfügbar ist.'
              }
            },
            'all-data-sets-deleted-toast': 'Alle Datensätze des Datenaufnahmeprojekts "{{id}}" wurden gelöscht.'
          },
          'create-report': {
            'title': 'Datensatzreport erzeugen',
            'version': 'Version des Datensatzreports',
            'languages': {
              'in-german': 'Deutsch',
              'in-english': 'Englisch'
            },
            'error': {
              'version': {
                'not-empty': 'Die Version darf nicht leer sein.',
                'pattern': 'Die Version muss von der Form "major.minor.patch" (z.B. "1.0.0") sein.',
                'size': 'Die Version darf nicht länger als 32 Zeichen sein.'
              },
              'languages': {
                'not-empty': 'Sie müssen mindestens eine Sprache auswählen!',
              }
            },
            'hints': {
              'version': 'Geben Sie die Versionsnummer an, die auf der Titelseite des Datensatzreports angezeigt werden soll.',
              'languages': 'Wählen Sie mindestens eine Sprache aus in der der Report erzeugt werden soll.'
            },
            'tooltip': {
              'cancel': 'Klicken, um das Erzeugen des Datensatzreports abzubrechen.',
              'ok': 'Klicken, um das Erzeugen des Datensatzreports zu starten.'
            }
          }
        }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
