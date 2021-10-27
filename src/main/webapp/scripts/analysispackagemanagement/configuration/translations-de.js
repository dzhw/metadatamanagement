'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'analysis-package-management': {
        'detail': {
          'label': {
            'additional-links': 'Weiterführende Links',
            'analysisPackage': 'Analysepaket',
            'analysisPackages': 'Analysepakete',
            'annotations': 'Anmerkungen',
            'authors': 'Autor:innen',
            'data-curators': 'Data Curation',
            'description': 'Analysepaketbeschreibung',
            'doi': 'DOI',
            'institution': 'Institution',
            'institutions': 'Institution(en)',
            'license': 'Lizenz',
            'scripts': 'Skripte',
            'sponsors': 'Gefördert von',
            'title': 'Titel',
            'script-attachments': {
              'file': 'Datei hinzufügen'
            },
            'attachments': {
              'title': 'Titel',
              'description': 'Beschreibung',
              'language': 'Dokumentensprache',
              'file': 'Datei'
            },
            'software-package': 'Bitte wählen Sie das/die Softwarepaket(e) ein, für die dieses Skript geschrieben wurde.',
            'version': 'Version des Softwarepaketes'
          },
          'script-attachments': {
            'add-attachment-tooltip': 'Klicken, um einen neue Datei zu diesem Analysepaket hinzuzufügen.',
            'edit-attachment-tooltip': 'Klicken, um die Datei "{{ filename }}" zu bearbeiten.',
            'length-attachment-tooltip': 'Es darf nicht mehr als eine Datei pro Skript vorhanden sein.',
            'table-title': 'Materialien zu diesem Skript',
            'delete-attachment-tooltip': 'Klicken, um die Datei "{{ filename }}" zu löschen!',
            'file': 'Datei hinzufügen',
            'save-analysis-package-before-adding-attachment': 'The analysis package has to be saved to enable attaching documents.'
          },
          'attachments': {
            'table-title': 'Materialien zu diesem Analysepaket',
            'delete-attachment-tooltip': 'Klicken, um die Datei "{{ filename }}" zu löschen!',
            'edit-attachment-tooltip': 'Klicken, um die Datei "{{ filename }}" zu bearbeiten.',
            'select-attachment-tooltip': 'Klicken, um Datei "{{ filename }}" zum Verschieben auszuwählen.',
            'move-attachment-up-tooltip': 'Klicken, um die ausgewählte Datei nach oben zu verschieben.',
            'move-attachment-down-tooltip': 'Klicken, um die ausgewählte Datei nach unten zu verschieben.',
            'save-attachment-order-tooltip': 'Klicken, um die geänderte Reihenfolge der Dateien zu speichern.',
            'add-attachment-tooltip': 'Klicken, um einen neue Datei zu diesem Analysepaket hinzuzufügen.',
            'change-file-tooltip': 'Klicken, um eine Datei auszuwählen.',
            'open-choose-previous-version-tooltip': 'Klicken, um eine ältere Version der Metadaten wiederherzustellen.',
            'current-version-restored-toast': 'Die aktuelle Version der Metadaten von Datei "{{ filename }}" wurde wiederhergestellt.',
            'previous-version-restored-toast': 'Die ältere Version der Metadaten von Datei "{{ filename }}" kann jetzt gespeichert werden.',
            'language-not-found': 'Keine gültige Sprache gefunden!',
            'save-analysis-package-before-adding-attachment': 'Das Analysepaket muss erst gespeichert werden, bevor Materialien hinzugefügt werden können.',
            'create-title': 'Neue Datei zu Analysepaket "{{ analysisPackageId }}" hinzufügen',
            'edit-title': 'Datei "{{ filename }}" von Analysepaket "{{ analysisPackageId }}" bearbeiten',
            'attachment-deleted-toast': 'Datei "{{ filename }}" wurde gelöscht!',
            'attachment-order-saved-toast': 'Die geänderte Reihenfolge der Dateien wurde gespeichert.',
            'hints': {
              'filename': 'Wählen Sie eine Datei aus, die Sie des Analysepakets hinzufügen wollen.'
            }
          },
          'not-found': 'Die id {{id}} referenziert auf eine unbekanntes Analysepaket.'
        },
        'edit': {
          'all-analysis-packages-deleted-toast': 'Das Analysepaket des Datenaufbereitungsprojekts "{{id}}" wurde gelöscht.',
          'open-choose-previous-version-tooltip': 'Klicken, um eine ältere Version dieses Analysepakets wieder herzustellen.',
          'save-tooltip': 'Klicken, um das Analysepaket zu speichern.',
          'move-sponsor-up-tooltip': 'Klicken, um die ausgewählte Geldgeber:in nach oben zu verschieben.',
          'move-sponsor-down-tooltip': 'Klicken, um die ausgewählte Geldgeber:in nach unten zu verschieben.',
          'add-sponsor-tooltip': 'Klicken, um eine weitere Geldgeber:in diesem Analysepaket hinzuzufügen.',
          'delete-sponsor-tooltip': 'Klicken, um die Geldgeber:in aus diesem Analysepaket zu entfernen.',
          'move-script-up-tooltip': 'Klicken, um das ausgewählte Skript nach oben zu verschieben.',
          'move-script-down-tooltip': 'Klicken, um das ausgewählte Skript nach unten zu verschieben.',
          'add-script-tooltip': 'Klicken, um ein weiteres Skript diesem Analysepaket hinzuzufügen.',
          'delete-script-tooltip': 'Klicken, um das Skript aus diesem Analysepaket zu entfernen.',
          'move-institution-up-tooltip': 'Klicken, um die ausgewählte Institution nach oben zu verschieben.',
          'move-institution-down-tooltip': 'Klicken, um die ausgewählte Institution nach unten zu verschieben.',
          'add-institution-tooltip': 'Klicken, um eine weitere Institution diesem Analysepaket hinzuzufügen.',
          'add-link-tooltip': 'Klicken, um einen weiteren Link diesem Analysepaket hinzuzufügen.',
          'move-link-up-tooltip': 'Klicken, um den ausgewählten Link nach oben zu verschieben.',
          'move-link-down-tooltip': 'Klicken, um den ausgewählten Link nach unten zu verschieben.',
          'delete-institution-tooltip': 'Klicken, um die Institution aus diesem Analysepaket zu entfernen.',
          'add-package-author-tooltip': 'Klicken, um eine neue Autor:in diesem Analysepaket hinzuzufügen.',
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
          'label': {
            'create-analysis-package': 'Analysepaket anlegen:',
            'edit-analysis-package': 'Analysepaket bearbeiten:',
            'first-name': 'Vorname',
            'middle-name': 'Zweiter Vorname',
            'last-name': 'Nachname',
            'tags': 'Tags (Schlagwörter) zum Analysepaket',
            'publication-year': 'Jahr der Veröffentlichung',
            'institution': 'Institution',
            'sponsor': 'Geldgeber:in',
            'script': {
              'language': 'Skript Sprache',
              'software-package': 'Software Paket',
              'version': 'Version des Softwarepaketes'
            },
            'location': 'Ort',
            'additional-links': {
              'url': 'URL',
              'display-text': 'Anzeigetext'
            }
          },
          'hints': {
            'annotations': {
              'de': 'Geben Sie zusätzliche Anmerkungen zum Analysepaket auf Deutsch ein.',
              'en': 'Geben Sie zusätzliche Anmerkungen zum Analysepaket auf Englisch ein.'
            },
            'authors': {
              'first-name': 'Geben Sie den Vornamen der Autor:in ein.',
              'middle-name': 'Geben Sie, falls vorhanden, den zweiten Vornamen der Autor:in ein.',
              'last-name': 'Geben Sie den Nachnamen der Autor:in ein.'
            },
            'curators': {
              'first-name': 'Geben Sie den Vornamen der Person ein, die an der Datenaufbereitung beteiligt ist.',
              'middle-name': 'Geben Sie, falls vorhanden, den zweiten Vornamen der Person ein.',
              'last-name': 'Geben Sie den Nachnamen der Person ein, die an der Datenaufbereitung beteiligt ist.'
            },
            'description': {
              'de': 'Geben Sie eine Beschreibung des Analysepakets auf Deutsch ein.',
              'en': 'Geben Sie eine Beschreibung des Analysepakets auf Englisch ein.'
            },
            'institution': {
              'de': 'Geben Sie den deutschen Namen der Institution ein, die an dem Analysepaket beteiligt waren.',
              'en': 'Geben Sie den englischen Namen der Institution ein, die an dem Analysepaket beteiligt waren.'
            },
            'license': 'Wenn kein Vertrag unterzeichnet wird, benötigen wir eine Lizenz wie cc-by-sa',
            'scripts': {
              'language': 'Bitte geben Sie die Sprache an, die Sie für die Kommentare im Drehbuch verwendet haben.',
              'software-package': 'Bitte wählen Sie das/die Softwarepaket(e) ein, für die dieses Skript geschrieben wurde.',
              'version': 'Version des Softwarepaketes'
            },
            'sponsor': {
              'de': 'Geben Sie den deutschen Namen der Geldgeber:in für dieses Analysepaket ein.',
              'en': 'Geben Sie den englischen Namen der Geldgeber:in für dieses Analysepaket ein.'
            },
            'title': {
              'de': 'Geben Sie den Titel des Analysepakets auf Deutsch ein.',
              'en': 'Geben Sie den Titel des Analysepakets auf Englisch ein.'
            }
          }
        },
        'error': {
          'analysis-package': {
            'annotations': {
              'i18n-string-size': 'Die Maximallänge der Anmerkungen ist 2048 Zeichen.'
            },
            'attachment': {
              'filename': {
                'not-empty': 'Es muss eine Datei ausgewählt sein',
                'not-unique': 'Eine Datei mit diesem Namen existiert bereits',
                'not-valid': 'Dieser Dateiname ist ungültig',
                'pattern': 'Es dürfen für die FDZ-ID nur alphanumerische Zeichen, deutsche Umlaute, ß, Minus und der Unterstrich verwendet werden.'
              },
              'file-not-found': 'Die Datei {{ filename }} wurde nicht gefunden und wurde daher nicht gespeichert!'
            },
            'description': {
              'not-null': 'Die Beschreibung eines Analysepakets darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge der Beschreibung eines Analysepakets ist 2048 Zeichen.',
              'i18n-string-not-empty': 'Die Beschreibung eines Analysepakets muss in beiden Sprachen vorhanden sein.'
            },
            'institution': {
              'not-null': 'Die Institution eines Analysepakets darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge der Institution eines Analysepakets ist 512 Zeichen.',
              'i18n-string-entire-not-empty': 'Die Institution eines Analysepakets muss in beiden Sprachen vorhanden sein.'
            },
            'language': {
              'not-found': 'Keine gültige Sprache gefunden!',
              'not-null': 'Die Sprache des Skripts darf nicht leer sein.',
              'not-valid': 'Bitte wählen Sie eine vorgeschlagene Sprache aus.'
            },
            'software-package': {
              'not-found': 'Kein gültiges Sofware Paket gefunden.',
              'not-null': 'Das Software Paket darf nicht leer sein.',
              'not-valid': 'Bitte wählen Sie ein vorgeschlagenes Software Paket aus.'
            },
            'software-package-version': {
              'string-size': 'Die Maximallänge der Software Paket Version ist 32 Zeichen.',
              'string-entire-not-empty': 'Die Software Paket Version muss vorhanden sein.'
            },
            'license': {
              'string-size': 'Die Maximallänge der Lizenz ist 1048576 Zeichen.'
            },
            'sponsor': {
              'not-null': 'Die Geldgeber:in eines Analysepakets darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge der Geldgeber:in eines Analysepakets ist 512 Zeichen.',
              'i18n-string-entire-not-empty': 'Die Geldgeber:in eines Analysepakets muss in beiden Sprachen vorhanden sein.'
            },
            'title': {
              'not-null': 'Der Titel eines Analysepaket darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge des Titels eines Analysepaket ist 2048 Zeichen.',
              'i18n-string-entire-not-empty': 'Der Titel eines Analysepaket muss in allen Sprachen vorhanden sein.'
            }
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
