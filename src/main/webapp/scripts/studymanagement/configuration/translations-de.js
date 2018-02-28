'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'study-management': {
        'detail': {
          'label': {
            'study': 'Studie',
            'studies': 'Studien',
            'studySeries': 'Studienreihe',
            'institution': 'Erhebende Institution',
            'authors': 'Projektmitarbeiter(innen)',
            'sponsors': 'Gefördert von',
            'version': 'Version der Datensätze',
            'surveyDesign': 'Erhebungsdesign',
            'annotations': 'Anmerkungen',
            'wave': 'Verfügbare Wellen',
            'survey-data-type': 'Erhebungsdatentyp',
            'title': 'Titel',
            'dataAvailability': 'Datenverfügbarkeit',
            'attachments': {
              'type': 'Typ',
              'title': 'Titel',
              'description': 'Beschreibung',
              'language': 'Dokumentensprache',
              'file': 'Datei'
            },
            'data-set': {
              'accessWays': 'Zugangswege',
              'description': 'Beschreibung',
              'description-tooltip': 'Klicken, um den Datensatz "{{id}}" anzuzeigen',
              'maxNumberOfObservations': 'Fälle',
              'maxNumberOfEpisodes': 'Episoden',
              'surveyed-in': 'Enthält Daten aus diesen Erhebungen'
            },
            'doi': 'DOI'
          },
          'attachments': {
            'table-title': 'Materialien zu der Studie',
            'attachment-deleted-toast': 'Datei "{{ filename }}" wurde gelöscht!',
            'delete-attachment-tooltip': 'Klicken, um die Datei "{{ filename }}" zu löschen!',
            'edit-attachment-tooltip': 'Klicken, um die Datei "{{ filename }}" zu bearbeiten.',
            'select-attachment-tooltip': 'Klicken, um Datei "{{ filename }}" zum Verschieben auszuwählen.',
            'move-attachment-up-tooltip': 'Klicken, um die ausgewählte Datei nach oben zu verschieben.',
            'move-attachment-down-tooltip': 'Klicken, um die ausgewählte Datei nach unten zu verschieben.',
            'save-attachment-order-tooltip': 'Klicken, um die geänderte Reihenfolge der Dateien zu speichern.',
            'attachment-order-saved-toast': 'Die geänderte Reihenfolge der Dateien wurde gespeichert.',
            'add-attachment-tooltip': 'Klicken, um einen neue Datei zu dieser Studie hinzuzufügen.',
            'edit-title': 'Datei "{{ filename }}" von Studie "{{ studyId }}" bearbeiten',
            'create-title': 'Neue Datei zu Studie "{{ studyId }}" hinzufügen',
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
            'save-study-before-adding-attachment': 'Die Studie muss erst gespeichert werden, bevor Materialien hinzugefügt werden können.',
            'hints': {
              'filename': 'Wählen Sie eine Datei aus, die Sie der Studie hinzufügen wollen.',
              'type': 'Wählen Sie den Typ der Datei aus.',
              'language': 'Wählen Sie die Sprache, die in der Datei verwendet wurde, aus.',
              'title': 'Geben Sie den Titel der Datei in der Dokumentensprache ein.',
              'description': {
                'de': 'Geben Sie eine Beschreibung dieser Datei auf Deutsch ein.',
                'en': 'Geben Sie eine Beschreibung dieser Datei auf Englisch ein.'
              }
            }
          },
          'data-set': {
            'card-title': 'Verfügbare Datensätze'
          },
          'title': '{{ title }} ({{ studyId }})',
          'description': 'Studienbeschreibung',
          'basic-data-of-surveys': 'Eckdaten der Erhebungen',
          'not-found': 'Die id {{id}} referenziert auf eine unbekannte Studie.',
          'not-found-references': 'Die id {{id}} hat keine Referenzen auf Studien.',
          'not-yet-released': 'Noch nicht freigegeben',
          'not-released-toast': 'Die Studie "{{ id }}" wurde noch nicht für alle Benutzer freigegeben!',
          'old-version': 'Der verwendete Link verweist auf eine ältere Version ({{versionFromUrl}}). Hier wird die aktuelle Version ({{actualVersion}}) der Studie {{title}} dargestellt.',
          'beta-release-no-doi': 'Dies ist ein vorläufiger BETA Release und enthält noch keine DOI.',
          'tooltips': {
            'surveys': {
              'one': 'Klicken, um die Erhebung dieser Studie anzuzeigen',
              'many': 'Klicken, um alle Erhebungen dieser Studie anzuzeigen'
            },
            'data-sets': {
              'one': 'Klicken, um den Datensatz dieser Studie anzuzeigen',
              'many': 'Klicken, um alle Datensätze dieser Studie anzuzeigen',
            },
            'publications': {
              'one': 'Klicken, um die Publikation zu dieser Studie anzuzeigen',
              'many': 'Klicken, um alle Publikationen zu dieser Studie anzuzeigen'
            },
            'variables': {
              'one': 'Klicken, um die Variable dieser Studie anzuzeigen',
              'many': 'Klicken, um alle Variablen dieser Studie anzuzeigen'
            },
            'questions': {
              'one': 'Klicken, um die Frage dieser Studie anzuzeigen.',
              'many': 'Klicken, um die Fragen dieser Studie anzuzeigen.'
            },
            'instruments': {
              'one': 'Klicken, um die Instrumente dieser Studie anzuzeigen.',
              'many': 'Klicken, um die Instrumente dieser Studie anzuzeigen.'
            },
            'studies': {
              'study-series': 'Klicken, um alle Studien aus der Studienreihe anzuzeigen.'
            }
          },
          'doi-tooltip': 'Klicken, um die DOI in einem neuen Tab zu öffnen'
        },
        'log-messages': {
          'study': {
            'saved': 'Studie mit FDZ-ID {{ id }} erfolgreich gespeichert!',
            'not-saved': 'Studie mit FDZ-ID {{ id }} wurde nicht gespeichert:',
            'study-file-not-found': 'In dem ausgewählten Verzeichnis fehlt die folgende Datei: study.xlsx!',
            'releases-file-not-found': 'In dem ausgewählten Verzeichnis fehlt die folgende Datei: releases.xlsx!',
            'unable-to-delete': 'Die Studie konnte nicht gelöscht werden!',
            'upload-terminated': 'Upload von {{ total }} Studie  und {{ attachments }} Attachments mit {{warnings}} Warnungen und {{ errors }} Fehlern beendet!',
            'cancelled': 'Upload der Studie Abgebrochen!'
          },
          'study-attachment': {
            'not-saved': 'Attachment "{{ id }}" wurde nicht gespeichert:',
            'file-not-found': 'Die Datei {{ filename }} wurde nicht gefunden und wurde daher nicht gespeichert!'
          }
        },
        'error': {
          'study': {
            'id': {
              'not-empty': 'Die FDZ-ID der Studie darf nicht leer sein!',
              'size': 'Die Maximallänge der FDZ-ID ist 128 Zeichen.',
              'pattern': 'Es dürfen für die FDZ-ID nur alphanumerische Zeichen, deutsche Umlaute, ß, Minus, Ausrufezeichen und der Unterstrich verwendet werden.',
              'not-valid-id': 'Die FDZ-ID der Studie muss der Form "stu-" + {ProjektID} + "$" entsprechen.'
            },
            'title': {
              'not-null': 'Der Titel einer Studie darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge des Titels einer Studie ist 2048 Zeichen.',
              'i18n-string-entire-not-empty': 'Der Titel einer Studie muss in allen Sprachen vorhanden sein.'
            },
            'description': {
              'not-null': 'Die Beschreibung einer Studie darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge der Beschreibung einer Studie ist 2048 Zeichen.',
              'i18n-string-not-empty': 'Die Beschreibung einer Studie muss in mindestens einer Sprache vorhanden sein.'
            },
            'institution': {
              'not-null': 'Die Institution einer Studie darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge der Institution einer Studie ist 128 Zeichen.',
              'i18n-string-not-empty': 'Die Institution einer Studie muss in mindestens einer Sprache vorhanden sein.'
            },
            'sponsor': {
              'not-null': 'Der Sponsor einer Studie darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge des Sponsors einer Studie ist 128 Zeichen.',
              'i18n-string-entire-not-empty': 'Der Sponsor einer Studie muss in beiden Sprachen vorhanden sein.'
            },
            'study-series': {
              'i18n-string-size': 'Die Maximallänge der Studienreihe ist 128 Zeichen.',
              'i18n-string-entire-not-empty-optional': 'Wenn die Studienreihe in einer Sprache vorliegt, muss sie in allen Sprachen vorliegen.'
            },
            'data-availability': {
              'not-null': 'Die Datenerreichbarkeit einer Studie darf nicht leer sein!',
              'valid-data-availability': 'Die erlaubten Werte für Datenverfügbarkeit der Studie sind: Verfügbar, In Aufbereitung, Nicht verfügbar.'
            },
            'survey-design': {
              'not-null': 'Das Erhebungsdesign einer Studie darf nicht leer sein!',
              'valid-survey-design': 'Die erlaubten Werte für das Erhebungsdesign der Studie sind: Querschnitt, Panel.'
            },
            'authors': {
              'not-empty': 'Die Liste der Autoren einer Studie benötigt mindestens ein Element und darf nicht leer sein!',
            },
            'annotations': {
              'i18n-string-size': 'Die Maximallänge der Anmerkungen ist 2048 Zeichen.'
            },
            'data-acquisition-project': {
              'id': {
                'not-empty': 'Die FDZ - ID des Projektes darf bei der Studie nicht leer sein!'
              }
            }
          },
          'study-attachment-metadata': {
            'study-id': {
              'not-empty': 'Die ID der zugehörigen Studie darf nicht leer sein.'
            },
            'project-id': {
              'not-empty': 'Die ID des zugehörigen Datenaufbereitungsprojektes darf nicht leer sein.'
            },
            'type': {
              'not-null': 'Der Typ des Attachments darf nicht leer sein.',
              'i18n-string-size': 'Der Typ muss in beiden Sprachen angegeben werden und darf nicht länger als 32 Zeichen sein.',
              'valid-type': 'Der Typ muss einer der folgenden Werte sein: Daten- und Methodenbericht, Sonstiges.'
            },
            'description': {
              'not-null': 'Die Beschreibung des Attachments darf nicht leer sein.',
              'i18n-string-size': 'Die Beschreibung muss in mindestens einer Sprache angegeben werden und darf nicht länger als 128 Zeichen sein.',
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
          'edit-page-title': 'Studie {{studyId}} bearbeiten',
          'create-page-title': 'Studie {{studyId}} anlegen',
          'success-on-save-toast': 'Studie {{studyId}} wurde erfolgreich gespeichert',
          'error-on-save-toast': 'Ein Fehler trat beim Speichern von Studie {{studyId}} auf!',
          'study-has-validation-errors-toast': 'Die Studie wurde nicht gespeichert, weil es noch ungültige Felder gibt!',
          'previous-version-restored-toast': 'Die ältere Version von Studie {{ studyId }} kann jetzt gespeichert werden.',
          'current-version-restored-toast': 'Die aktuelle Version von Studie {{ studyId }} wurde wiederhergestellt.',
          'not-authorized-toast': 'Sie sind nicht berechtigt Studien zu bearbeiten oder anzulegen!',
          'choose-unreleased-project-toast': 'Bitte wählen Sie ein Projekt aus, welches aktuell nicht freigegeben ist!',
          'label': {
            'edit-study': 'Studie bearbeiten:',
            'create-study': 'Studie anlegen:',
            'first-name': 'Vorname',
            'middle-name': 'Zweiter Vorname',
            'last-name': 'Nachname'
          },
          'open-choose-previous-version-tooltip': 'Klicken, um eine ältere Version dieser Studie wieder herzustellen.',
          'save-tooltip': 'Klicken, um die Studie zu speichern.',
          'move-author-up-tooltip': 'Klicken, um den ausgewählten Mitarbeiter nach oben zu verschieben.',
          'move-author-down-tooltip': 'Klicken, um den ausgewählten Mitarbeiter nach unten zu verschieben.',
          'add-author-tooltip': 'Klicken, um einen neuen Mitarbeiter dieser Studie hinzuzufügen.',
          'delete-author-tooltip': 'Klicken, um den Projektmitarbeiter aus dieser Studie zu löschen.',
          'choose-previous-version': {
            'next-page-tooltip': 'Klicken, um ältere Versionen anzuzeigen.',
            'previous-page-tooltip': 'Klicken, um aktuellere Versionen anzuzeigen.',
            'title': 'Ältere Version der Studie {{ studyId }} wiederherstellen',
            'text': 'Wählen Sie eine ältere Studienversion aus, die wiederhergestellt werden soll:',
            'cancel-tooltip': 'Klicken, um ohne eine ältere Studienversion auszuwählen zurückzukehren.',
            'no-versions-found': 'Es wurden keine älteren Versionen von Studie {{ studyId }} gefunden.',
            'study-title': 'Titel',
            'lastModified': 'Geändert',
            'lastModifiedBy': 'von',
            'current-version-tooltip': 'Dies ist die aktuelle Version!'
          },
          'hints': {
            'title': {
              'de': 'Geben Sie den Titel der Studie auf Deutsch ein.',
              'en': 'Geben Sie den Titel der Studie auf Englisch ein.'
            },
            'study-series': {
              'de': 'Geben Sie, falls vorhanden, den Namen der Studienreihe auf Deutsch ein.',
              'en': 'Geben Sie, falls vorhanden, den Namen der Studienreihe auf Englisch ein.'
            },
            'institution': {
              'de': 'Geben Sie den deutschen Namen der Institution ein, die die Erhebungen durchgeführt hat.',
              'en': 'Geben Sie den englischen Namen der Institution ein, die die Erhebungen durchgeführt hat.'
            },
            'sponsor': {
              'de': 'Geben Sie den deutschen Namen des Geldgebers für diese Studie ein.',
              'en': 'Geben Sie den englischen Namen des Geldgebers für diese Studie ein.'
            },
            'survey-design': 'Wählen Sie das Erhebungsdesign dieser Studie aus.',
            'annotations': {
              'de': 'Geben Sie zusätzliche Anmerkungen zur Studie auf Deutsch ein.',
              'en': 'Geben Sie zusätzliche Anmerkungen zur Studie auf Englisch ein.'
            },
            'data-availability': 'Wählen Sie den Status aus, der die aktuelle Verfügbarkeit der Daten am Besten beschreibt.',
            'description': {
              'de': 'Geben Sie eine Beschreibung der Studie auf Deutsch ein.',
              'en': 'Geben Sie eine Beschreibung der Studie auf Englisch ein.'
            },
            'authors': {
              'first-name': 'Geben Sie den Vornamen des Projektmitarbeiters ein.',
              'middle-name': 'Geben Sie, falls vorhanden, den zweiten Vornamen des Projektmitarbeiters ein.',
              'last-name': 'Geben Sie den Nachnamen des Projektmitarbeiters ein.'
            }
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
