'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'data-package-management': {
        'detail': {
          'label': {
            'studySeries': 'Studienreihe',
            'dataPackage': 'Datenpaket',
            'dataPackages': 'Datenpakete',
            'institution': 'Erhebende Institution',
            'institutions': 'Erhebende Institution(en)',
            'projectContributors': 'Projektmitarbeiter:innen',
            'data-curators': 'Datenkuratierung',
            'sponsors': 'Gefördert von',
            'version': 'Version',
            'surveyDesign': 'Erhebungsdesign',
            'annotations': 'Anmerkungen',
            'wave': 'Wellen',
            'survey-data-type': 'Erhebungsdatentyp',
            'survey-period': 'Erhebungszeitraum',
            'title': 'Titel',
            'dataLanguages': 'Daten verfügbar auf',
            'tags': 'Schlagwörter',
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
            'doi': 'DOI',
            'published-at': 'veröffentlicht am',
            'published': 'Veröffentlicht am'
          },
          'attachments': {
            'table-title': 'Materialien zu diesem Datenpaket',
            'attachment-deleted-toast': 'Datei "{{ filename }}" wurde gelöscht!',
            'delete-attachment-tooltip': 'Klicken, um die Datei "{{ filename }}" zu löschen!',
            'edit-attachment-tooltip': 'Klicken, um die Datei "{{ filename }}" zu bearbeiten.',
            'select-attachment-tooltip': 'Klicken, um Datei "{{ filename }}" zum Verschieben auszuwählen.',
            'move-attachment-up-tooltip': 'Klicken, um die ausgewählte Datei nach oben zu verschieben.',
            'move-attachment-down-tooltip': 'Klicken, um die ausgewählte Datei nach unten zu verschieben.',
            'save-attachment-order-tooltip': 'Klicken, um die geänderte Reihenfolge der Dateien zu speichern.',
            'attachment-order-saved-toast': 'Die geänderte Reihenfolge der Dateien wurde gespeichert.',
            'add-attachment-tooltip': 'Klicken, um einen neue Datei zu diesem Datenpaket hinzuzufügen.',
            'edit-title': 'Datei "{{ filename }}" von Datenpaket "{{ dataPackageId }}" bearbeiten',
            'create-title': 'Neue Datei zu Datenpaket "{{ dataPackageId }}" hinzufügen',
            'change-file-tooltip': 'Klicken, um eine Datei auszuwählen.',
            'open-choose-previous-version-tooltip': 'Klicken, um eine ältere Version der Metadaten wiederherzustellen.',
            'current-version-restored-toast': 'Die aktuelle Version der Metadaten von Datei "{{ filename }}" wurde wiederhergestellt.',
            'previous-version-restored-toast': 'Die ältere Version der Metadaten von Datei "{{ filename }}" kann jetzt gespeichert werden.',
            'language-not-found': 'Keine gültige Sprache gefunden!',
            'save-data-package-before-adding-attachment': 'Das Datenpaket muss erst gespeichert werden, bevor Materialien hinzugefügt werden können.',
            'hints': {
              'filename': 'Wählen Sie eine Datei aus, die Sie des Datenpakets hinzufügen wollen.'
            }
          },
          'data-set': {
            'card-title': 'Verfügbare Datensätze'
          },
          'title': '{{ title }} ({{ dataPackageId }})',
          'description': 'Datenpaketbeschreibung',
          'basic-data-of-surveys': 'Eckdaten der Erhebungen',
          'not-found': 'Die id {{id}} referenziert auf eine unbekanntes Datenpaket.',
          'not-found-references': 'Die id {{id}} hat keine Referenzen auf Datenpakete.',
          'not-yet-released': 'Aktuell nicht freigegeben',
          'not-released-toast': 'Das Datenpaket "{{ id }}" wird aktuell bearbeitet und ist daher nicht für alle Benutzer:innen freigegeben!',
          'beta-release-no-doi': 'Dieses Datenpaket hat noch keine DOI.',
          'publications-for-series': 'Publikationen zur Studienreihe "{{studySeries}}"',
          'publications-for-data-package': 'Publikationen zu diesem Datenpaket',
          'tooltips': {
            'surveys': {
              'one': 'Klicken, um die Erhebung dieses Datenpakets anzuzeigen',
              'many': 'Klicken, um alle Erhebungen dieses Datenpakets anzuzeigen'
            },
            'data-sets': {
              'one': 'Klicken, um den Datensatz dieses Datenpakets anzuzeigen',
              'many': 'Klicken, um alle Datensätze dieses Datenpakets anzuzeigen'
            },
            'series-publications': 'Klicken, um alle Publikationen zu dieser Studienreihe anzuzeigen',
            'publications': {
              'one': 'Klicken, um die Publikation zu diesem Datenpaket anzuzeigen',
              'many': 'Klicken, um alle Publikationen zu diesem Datenpaket anzuzeigen'
            },
            'variables': {
              'one': 'Klicken, um die Variable dieses Datenpakets anzuzeigen',
              'many': 'Klicken, um alle Variablen dieses Datenpakets anzuzeigen'
            },
            'questions': {
              'one': 'Klicken, um die Frage dieses Datenpakets anzuzeigen.',
              'many': 'Klicken, um die Fragen dieses Datenpakets anzuzeigen.'
            },
            'instruments': {
              'one': 'Klicken, um das Instrument dieses Datenpakets anzuzeigen.',
              'many': 'Klicken, um die Instrumente dieses Datenpakets anzuzeigen.'
            },
            'data-packages': {
              'study-series': 'Klicken, um alle Datenpakete aus der Studienreihe anzuzeigen.'
            },
            'concepts': {
              'one': 'Klicken, um das Konzept, welches in diesem Datenpaket gemessen wurde, anzuzeigen',
              'many': 'Klicken, um alle Konzepte, die in diesem Datenpaket gemessen wurden, anzuzeigen'
            }
          },
          'doi-tooltip': 'Klicken, um die DOI in einem neuen Tab zu öffnen',
          'tag-tooltip': 'Klicken, um Datenpakete mit diesem Tag zu suchen'
        },
        'log-messages': {
          'data-package': {
            'saved': 'Datenpaket mit FDZ-ID {{ id }} erfolgreich gespeichert!',
            'not-saved': 'Datenpaket mit FDZ-ID {{ id }} wurde nicht gespeichert:',
            'data-package-file-not-found': 'In dem ausgewählten Verzeichnis fehlt die folgende Datei: dataPackage.xlsx!',
            'releases-file-not-found': 'In dem ausgewählten Verzeichnis fehlt die folgende Datei: releases.xlsx!',
            'unable-to-delete': 'Das Datenpaket konnte nicht gelöscht werden!',
            'upload-terminated': 'Upload von {{ total }} Datenpaket  und {{ attachments }} Attachments mit {{warnings}} Warnungen und {{ errors }} Fehlern beendet!',
            'cancelled': 'Upload des Datenpakets Abgebrochen!'
          }
        },
        'error': {
          'data-package': {
            'id': {
              'not-empty': 'Die FDZ-ID des Datenpakets darf nicht leer sein!',
              'size': 'Die Maximallänge der FDZ-ID ist 512 Zeichen.',
              'pattern': 'Es dürfen für die FDZ-ID nur alphanumerische Zeichen, deutsche Umlaute, ß, Minus und der Unterstrich verwendet werden.',
              'not-valid-id': 'Die FDZ-ID des Datenpakets muss der Form "stu-" + {ProjektID} + "$" entsprechen.'
            },
            'title': {
              'not-null': 'Der Titel eines Datenpakets darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge des Titels eines Datenpakets ist 2048 Zeichen.',
              'i18n-string-entire-not-empty': 'Der Titel eines Datenpakets muss in allen Sprachen vorhanden sein.'
            },
            'description': {
              'not-null': 'Die Beschreibung eines Datenpakets darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge der Beschreibung eines Datenpakets ist 2048 Zeichen.',
              'i18n-string-not-empty': 'Die Beschreibung eines Datenpakets muss in beiden Sprachen vorhanden sein.'
            },
            'institution': {
              'not-null': 'Die Institution eines Datenpakets darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge der Institution eines Datenpakets ist 512 Zeichen.',
              'i18n-string-entire-not-empty': 'Die Institution eines Datenpakets muss in beiden Sprachen vorhanden sein.'
            },
            'sponsor': {
              'not-null': 'Der Sponsor eines Datenpakets darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge des Sponsors eines Datenpakets ist 512 Zeichen.',
              'i18n-string-entire-not-empty': 'Der Sponsor eines Datenpakets muss in beiden Sprachen vorhanden sein.'
            },
            'study-series': {
              'i18n-string-size': 'Die Maximallänge der Studienreihe ist 512 Zeichen.',
              'i18n-string-entire-not-empty-optional': 'Wenn die Studienreihe in einer Sprache vorliegt, muss sie in allen Sprachen vorliegen.',
              'i18n-string-must-not-contain-comma': 'Die Studienreihe darf keine Kommata enthalten.'
            },
            'survey-design': {
              'not-null': 'Das Erhebungsdesign eines Datenpakets darf nicht leer sein!',
              'valid-survey-design': 'Die erlaubten Werte für das Erhebungsdesign des Datenpakets sind: Querschnitt, Panel.'
            },
            'project-contributors': {
              'not-empty': 'Die Liste der Projektmitarbeiter:innen eines Datenpakets benötigt mindestens ein Element und darf nicht leer sein!'
            },
            'annotations': {
              'i18n-string-size': 'Die Maximallänge der Anmerkungen ist 2048 Zeichen.'
            },
            'data-acquisition-project': {
              'id': {
                'not-empty': 'Die FDZ - ID des Projektes des Datenpakets darf nicht leer sein!'
              }
            }
          }
        },
        'edit': {
          'edit-page-title': 'Datenpaket {{dataPackageId}} bearbeiten',
          'create-page-title': 'Datenpaket {{dataPackageId}} anlegen',
          'success-on-save-toast': 'Datenpaket {{dataPackageId}} wurde erfolgreich gespeichert',
          'error-on-save-toast': 'Ein Fehler trat beim Speichern von Datenpaket {{dataPackageId}} auf!',
          'data-package-has-validation-errors-toast': 'Das Datenpaket wurde nicht gespeichert, weil es noch ungültige Felder gibt!',
          'previous-version-restored-toast': 'Die ältere Version von Datenpaket {{ dataPackageId }} kann jetzt gespeichert werden.',
          'current-version-restored-toast': 'Die aktuelle Version von Datenpaket {{ dataPackageId }} wurde wiederhergestellt.',
          'not-authorized-toast': 'Sie sind nicht berechtigt Datenpakete zu bearbeiten oder anzulegen!',
          'choose-unreleased-project-toast': 'Datenpakete dürfen nur bearbeitet werden, wenn das Projekt aktuell nicht freigegeben ist!',
          'label': {
            'edit-data-package': 'Datenpaket bearbeiten:',
            'create-data-package': 'Datenpaket anlegen:',
            'first-name': 'Vorname',
            'middle-name': 'Zweiter Vorname',
            'last-name': 'Nachname',
            'tags': 'Tags (Schlagwörter) zum Datenpaket'
          },
          'open-choose-previous-version-tooltip': 'Klicken, um eine ältere Version dieses Datenpakets wieder herzustellen.',
          'save-tooltip': 'Klicken, um das Datenpaket zu speichern.',
          'move-contributor-up-tooltip': 'Klicken, um die ausgewählte Mitarbeiter:in nach oben zu verschieben.',
          'move-contributor-down-tooltip': 'Klicken, um die ausgewählte Mitarbeiter:in nach unten zu verschieben.',
          'add-contributor-tooltip': 'Klicken, um eine neue Mitarbeiter:in diesem Datenpaket hinzuzufügen.',
          'delete-contributor-tooltip': 'Klicken, um die Projektmitarbeiter:in aus diesem Datenpaket zu löschen.',
          'move-curator-up-tooltip': 'Klicken, um die ausgewählte Datenkurator:in nach oben zu verschieben.',
          'move-curator-down-tooltip': 'Klicken, um die ausgewählte Datenkurator:in nach unten zu verschieben.',
          'add-curator-tooltip': 'Klicken, um eine neue Datenkurator:in diesem Datenpaket hinzuzufügen.',
          'delete-curator-tooltip': 'Klicken, um die ausgewählte Datenkurator:in aus diesem Datenpaket zu löschen.',
          'move-institution-up-tooltip': 'Klicken, um die ausgewählte Institution nach oben zu verschieben.',
          'move-institution-down-tooltip': 'Klicken, um die ausgewählte Institution nach unten zu verschieben.',
          'add-institution-tooltip': 'Klicken, um eine weitere Institution diesem Datenpaket hinzuzufügen.',
          'delete-institution-tooltip': 'Klicken, um die Institution aus diesem Datenpaket zu entfernen.',
          'choose-previous-version': {
            'next-page-tooltip': 'Klicken, um ältere Versionen anzuzeigen.',
            'previous-page-tooltip': 'Klicken, um aktuellere Versionen anzuzeigen.',
            'title': 'Ältere Version des Datenpakets {{ dataPackageId }} wiederherstellen',
            'text': 'Wählen Sie eine ältere Datenpaketversion aus, die wiederhergestellt werden soll:',
            'cancel-tooltip': 'Klicken, um ohne eine ältere Datenpaketversion auszuwählen zurückzukehren.',
            'no-versions-found': 'Es wurden keine älteren Versionen von Datenpaket {{ dataPackageId }} gefunden.',
            'data-package-deleted': 'Das Datenpaket wurde gelöscht!'
          },
          'hints': {
            'title': {
              'de': 'Geben Sie den Titel des Datenpakets auf Deutsch ein.',
              'en': 'Geben Sie den Titel des Datenpakets auf Englisch ein.'
            },
            'study-series': {
              'de': 'Geben Sie, falls vorhanden, den Namen der Studienreihe auf Deutsch ein.',
              'en': 'Geben Sie, falls vorhanden, den Namen des Studienreihe auf Englisch ein.'
            },
            'institution': {
              'de': 'Geben Sie den deutschen Namen der Institution ein, die die Erhebungen durchgeführt hat.',
              'en': 'Geben Sie den englischen Namen der Institution ein, die die Erhebungen durchgeführt hat.'
            },
            'sponsor': {
              'de': 'Geben Sie den deutschen Namen des Geldgebers für dieses Datenpaket ein.',
              'en': 'Geben Sie den englischen Namen des Geldgebers für dieses Datenpaket ein.'
            },
            'survey-design': 'Wählen Sie das Erhebungsdesign dieses Datenpakets aus.',
            'annotations': {
              'de': 'Geben Sie zusätzliche Anmerkungen zum Datenpaket auf Deutsch ein.',
              'en': 'Geben Sie zusätzliche Anmerkungen zum Datenpaket auf Englisch ein.'
            },
            'description': {
              'de': 'Geben Sie eine Beschreibung des Datenpakets auf Deutsch ein.',
              'en': 'Geben Sie eine Beschreibung des Datenpakets auf Englisch ein.'
            },
            'project-contributors': {
              'first-name': 'Geben Sie den Vornamen der Projektmitarbeiter:in ein.',
              'middle-name': 'Geben Sie, falls vorhanden, den zweiten Vornamen der Projektmitarbeiter:in ein.',
              'last-name': 'Geben Sie den Nachnamen der Projektmitarbeiter:in ein.'
            },
            'curators': {
              'first-name': 'Geben Sie den Vornamen der Person ein, die an der Datenaufbereitung beteiligt ist.',
              'middle-name': 'Geben Sie, falls vorhanden, den zweiten Vornamen der Person ein.',
              'last-name': 'Geben Sie den Nachnamen der Person ein, die an der Datenaufbereitung beteiligt ist.'
            }
          },
          'all-data-packages-deleted-toast': 'Das Datenpaket des Datenaufbereitungsprojekts "{{id}}" wurde gelöscht.'
        },
        'tag-editor': {
          'label': {
            'german-tags': 'Deutsche Tags',
            'english-tags': 'Englische Tags'
          },
          'placeholder': 'Neuen Tag eingeben',
          'error': {
            'required': 'Es muss mindestens ein Tag eingetragen sein.'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
