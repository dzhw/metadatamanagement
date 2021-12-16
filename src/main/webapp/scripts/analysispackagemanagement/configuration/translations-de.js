'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'analysis-package-management': {
        'detail': {
          'label': {
            'analyzed-data-package': {
              'data-package': 'FDZ-DZHW Datenpaket',
              'access-way': 'Zugangsweg',
              'available-access-ways': 'Verfügbare Zugangswege',
              'version': 'Version',
              'available-versions': 'Verfügbare Versionen',
              'available-data-packages': 'Verfügbare Datenpakete'
            },
            'external-data-package': {
              'available-availability-type': 'Verfügbarer Zugangsstatus',
              'availability-type': 'Zugangsstatus',
              'description': 'Datenpaketbeschreibung',
              'annotations': 'Anmerkungen',
              'data-source': 'Datenquelle',
              'data-source-url': 'Link zur Datenquelle'
            },
            'custom-data-package': {
              'available-availability-type': 'Verfügbarer Zugangsstatus',
              'availability-type': 'Zugangsstatus',
              'description': 'Datenpaketbeschreibung',
              'annotations': 'Anmerkungen',
              'data-source': 'Datenquelle',
              'dataSources': 'Datenquellen',
              'data-source-url': 'Link zur Datenquelle',
              'access-way': 'Zugangsweg',
              'available-access-ways': 'Verfügbare Zugangswege'
            },
            'attachments': {
              'title': 'Titel',
              'description': 'Beschreibung',
              'language': 'Dokumentensprache',
              'file': 'Datei'
            },
            'additional-links': 'Weiterführende Links',
            'analysisPackage': 'Analysepaket',
            'analysisPackages': 'Analysepakete',
            'analysis-data': 'Analysedaten',
            'type-of-data': 'Art der Daten',
            'annotations': 'Anmerkungen',
            'authors': 'Autor:innen',
            'data-curators': 'Datenkuratierung',
            'description': 'Analysepaketbeschreibung',
            'doi': 'DOI',
            'institution': 'Institution',
            'institutions': 'Institution(en)',
            'license': 'Lizenz',
            'scripts': 'Skripte',
            'sponsors': 'Gefördert von',
            'version': 'Version',
            'published-at': 'veröffentlicht am',
            'published': 'Veröffentlicht am',
            'generate-analysis-package-overview-tooltip': 'Klicken, um eine Übersicht über dieses Analysepaket als PDF zu erstellen.',
            'overview-generation-started-toast': 'Die Analysepaketübersicht wird jetzt erzeugt. Sie werden per E-Mail benachrichtigt, sobald der Vorgang abgeschlossen ist.',
            'tags': 'Schlagwörter',
            'title': 'Titel',
            'file': 'Datei',
            'save-file': 'Das Analysepaket muss erst gespeichert werden, bevor Skript Dateien hinzugefügt werden können.'
          },
          'script-attachment-metadata': {
            'attachment-deleted-toast': 'Datei "{{ filename }}" wurde gelöscht!',
            'delete-attachment-tooltip': 'Klicken, um die Datei aus diesem Skript zu löschen!',
            'add-attachment-tooltip': 'Klicken, um einen neue Datei zu diesem Skript hinzuzufügen.'
          },
          'scripts': {
            'table-title': 'Skripte zu diesem Analysepaket',
            'title': 'Titel',
            'software-package': 'Softwarepaket',
            'language': 'Skriptsprache',
            'version': 'Version des Softwarepakets',
            'file': 'Datei',
            'file-must-be-ordered': 'Das Skript ist nicht über einen direkten Download verfügbar, bitte bestellen Sie das Analysepaket über den Warenkorb.'
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
          'doi-tooltip': 'Klicken, um die DOI in einem neuen Tab zu öffnen.',
          'link-tooltip': 'Klicken, um den Link in einem neuen Tab zu öffnen.',
          'tag-tooltip': 'Klicken, um Analysepakete mit diesem Tag zu suchen.',
          'not-found': 'Die id {{id}} referenziert auf eine unbekanntes Analysepaket.',
          'not-found-references': 'Die id {{id}} hat keine Referenzen auf Analysepakete.',
          'not-yet-released': 'Aktuell nicht freigegeben',
          'not-released-toast': 'Das Analysepaket "{{ id }}" wird aktuell bearbeitet und ist daher nicht für alle Benutzer:innen freigegeben!',
          'beta-release-no-doi': 'Dieses Analysepaket hat noch keine DOI.',
          'publications-for-data-package': 'Publikationen zu diesem Analysepaket',
          'page-description': '{{ description }}',
          'title': 'Analysepaket (Skripte): {{ title }}'
        },
        'edit': {
          'add-analysis-data-tooltip': 'Klicken, um weitere Analysedaten zu diesem Analysepaket hinzuzufügen.',
          'move-analysis-data-up-tooltip': 'Klicken, um die ausgewählten Analyse Daten nach oben zu verschieben.',
          'move-analysis-data-down-tooltip': 'Klicken, um die ausgewählten Analyse Daten nach unten zu verschieben.',
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
          'delete-script-tooltip': 'Klicken, um dieses Skript aus diesem Analysepaket zu entfernen.',
          'move-institution-up-tooltip': 'Klicken, um die ausgewählte Institution nach oben zu verschieben.',
          'move-institution-down-tooltip': 'Klicken, um die ausgewählte Institution nach unten zu verschieben.',
          'add-institution-tooltip': 'Klicken, um eine weitere Institution diesem Analysepaket hinzuzufügen.',
          'add-link-tooltip': 'Klicken, um einen weiteren Link diesem Analysepaket hinzuzufügen.',
          'move-link-up-tooltip': 'Klicken, um den ausgewählten Link nach oben zu verschieben.',
          'move-link-down-tooltip': 'Klicken, um den ausgewählten Link nach unten zu verschieben.',
          'delete-link-tooltip': 'Klicken, um den ausgewählten Link aus diesem Analysepaket zu entfernen.',
          'delete-data-source-tooltip': 'Klicken, um die ausgewählten Datenquelle aus diesem Analysepaket zu entfernen.',
          'add-url-tooltip': 'Klicken, um eine weitere Datenquelle diesem benutzerdefinierten Datenpaket hinzuzufügen.',
          'add-data-source-tooltip': 'Klicken, um eine weitere Datenquelle diesem benutzerdefinierten Datenpaket hinzuzufügen.',
          'move-url-up-tooltip': 'Klicken, um die ausgewählte Datenquelle nach oben zu verschieben.',
          'move-url-down-tooltip': 'Klicken, um die ausgewählte Datenquelle nach unten zu verschieben.',
          'move-data-source-up-tooltip': 'Klicken, um die ausgewählte Datenquelle nach oben zu verschieben.',
          'move-data-source-down-tooltip': 'Klicken, um die ausgewählte Datenquelle nach unten zu verschieben.',
          'delete-url-tooltip': 'Klicken, um die Institution aus diesem benutzerdefinierten Datenpaket zu entfernen.',
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
            'version': 'Version',
            'script': {
              'used-language': 'Skriptsprache',
              'software-package': 'Softwarepaket',
              'software-package-version': 'Version des Softwarepakets'
            },
            'location': 'Ort',
            'additional-links': {
              'url': 'URL',
              'display-text': 'Anzeigetext'
            }
          },
          'hints': {
            'data-package': 'Bitte wählen Sie die Herkunftsart der Analysedaten aus.',
            'additional-links': {
              'url': 'Bitte tragen Sie die URL wie in dem folgenden Beispiel ein: https://www.dzhw.eu',
              'display-text': {
                'de': 'Optional: Geben Sie einen Text auf Deutsch an, der zur Anzeige des Links verwendet werden soll.',
                'en': 'Optional: Geben Sie einen Text auf Englisch an, der zur Anzeige des Links verwendet werden soll.'
              }
            },
            'authors': {
              'first-name': 'Geben Sie den Vornamen der Autor:in ein.',
              'middle-name': 'Geben Sie, falls vorhanden, den zweiten Vornamen der Autor:in ein.',
              'last-name': 'Geben Sie den Nachnamen der Autor:in ein.'
            },
            'annotations': {
              'de': 'Geben Sie zusätzliche Anmerkungen zum Analysepaket auf Deutsch ein.',
              'en': 'Geben Sie zusätzliche Anmerkungen zum Analysepaket auf Englisch ein.'
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
              'de': 'Geben Sie den deutschen Namen der Institution ein, die an dem Analysepaket beteiligt war.',
              'en': 'Geben Sie den englischen Namen der Institution ein, die an dem Analysepaket beteiligt war.'
            },
            'license': 'Wenn kein Vertrag unterzeichnet wird, benötigen wir eine Lizenz wie cc-by-sa',
            'script': {
              'title': {
                'de': 'Geben Sie den Titel des Skripts auf Deutsch ein.',
                'en': 'Geben Sie den Titel des Skripts auf Englisch ein.'
              },
              'used-language': 'Bitte geben Sie die Sprache an, die Sie für die Kommentare im Skript verwendet haben.',
              'software-package': 'Bitte wählen Sie das Softwarepaket aus, für das dieses Skript geschrieben wurde.',
              'software-package-version': 'Version des Softwarepakets'

            },
            'script-attachment-metadata': {
              'filename': 'Wählen Sie eine Datei aus, die Sie dem Skript hinzufügen wollen.'
            },
            'sponsor': {
              'de': 'Geben Sie den deutschen Namen der Geldgeber:in für dieses Analysepaket ein.',
              'en': 'Geben Sie den englischen Namen der Geldgeber:in für dieses Analysepaket ein.'
            },
            'title': {
              'de': 'Geben Sie den Titel des Analysepakets auf Deutsch ein.',
              'en': 'Geben Sie den Titel des Analysepakets auf Englisch ein.'
            },
            'external-data-package': {
              'title': {
                'de': 'Geben Sie den Titel des externen Datenpakets auf Deutsch ein.',
                'en': 'Geben Sie den Titel des externen Datenpakets auf Englisch ein.'
              },
              'description': {
                'de': 'Geben Sie eine Beschreibung des externen Datenpakets auf Deutsch ein.',
                'en': 'Geben Sie eine Beschreibung des externen Datenpakets auf Englisch ein.'
              },
              'annotations': {
                'de': 'Geben Sie zusätzliche Anmerkungen zum externen Datenpaket auf Deutsch ein.',
                'en': 'Geben Sie zusätzliche Anmerkungen zum externen Datenpaket auf Englisch ein.'
              },
              'data-source': {
                'de': 'Hier muss die Datenquelle angegeben werden, in der die Daten gespeichert werden (z.B. Name der Institution/des Repositoriums; private Speicherung).',
                'en': 'Hier muss die Datenquelle angegeben werden, in der die Daten gespeichert werden (z.B. Name der Institution/des Repositoriums; private Speicherung).'
              },
              'data-source-url': 'Bitte tragen Sie die URL wie in dem folgenden Beispiel ein: https://www.dzhw.eu',
              'availability-type': {
                'choose': 'Bitte wählen Sie einen Zugangsstatus.',
                'open': 'Keine oder nur geringfügige Zugangsbeschränkungen (z.B. Zustimmung zu einfachen Nutzungsbedingungen).',
                'restricted': 'Irgendeine höhergradige Form des beschränkten Zugangs (Registierung muss vor dem Datenzugang erfolgen; Ein Antragsprozess muss vor dem Datenzugang durchlaufen werden; sehr restriktive Nutzungsbedingungen).',
                'none': 'nicht zugänglich.'
              }
            },
            'custom-data-package': {
              'access-way': {
                'choose': 'Bitte wählen Sie ein Zugangsweg.'
              },
              'title': {
                'de': 'Geben Sie den Titel des benutzerdefinierten Datenpakets auf Deutsch ein.',
                'en': 'Geben Sie den Titel des benutzerdefinierten Datenpakets auf Englisch ein.'
              },
              'description': {
                'de': 'Geben Sie eine Beschreibung des benutzerdefinierten Datenpakets auf Deutsch ein.',
                'en': 'Geben Sie eine Beschreibung des benutzerdefinierten Datenpakets auf Englisch ein.'
              },
              'annotations': {
                'de': 'Geben Sie zusätzliche Anmerkungen zum benutzerdefinierten Datenpaket auf Deutsch ein.',
                'en': 'Geben Sie zusätzliche Anmerkungen zum benutzerdefinierten Datenpaket auf Englisch ein.'
              },
              'data-source': {
                'de': 'Die Datenquelle muss hier näher auf Deutsch beschrieben werden.',
                'en': 'Die Datenquelle muss hier näher auf Englisch beschrieben werden.'
              },
              'data-source-url': 'Geben Sie den Link zur Datenquelle ein.',
              'availability-type': {
                'choose': 'Bitte wählen Sie einen Zugangsstatus.',
                'open': 'Keine oder nur geringfügige Zugangsbeschränkungen (z.B. Zustimmung zu einfachen Nutzungsbedingungen).',
                'restricted': 'Irgendeine höhergradige Form des beschränkten Zugangs (Registierung muss vor dem Datenzugang erfolgen; Ein Antragsprozess muss vor dem Datenzugang durchlaufen werden; sehr restriktive Nutzungsbedingungen).',
                'none': 'nicht zugänglich.',
                'accessible': 'Lorem ipsum...',
                'not-accessible': 'Lorem ipsum...'
              }
            },
            'analyzed-data-package': {
              'data-package': 'Geben Sie den Titel des FDZ-DZHW Datenpakets ein.',
              'version': 'Bitte wählen Sie eine Version.',
              'access-way': {
                'choose': 'Bitte wählen Sie einen Zugangsweg.'
              }
            }
          }
        },
        'error': {
          'script': {
            'title': {
              'not-null': 'Der Titel eines Skripts darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge des Titels eines Skripts ist 2048 Zeichen.',
              'i18n-string-entire-not-empty': 'Der Titel eines Skripts muss in allen Sprachen vorhanden sein.'
            },
            'used-language': {
              'not-found': 'Keine gültige Skriptsprache gefunden!',
              'not-null': 'Die Sprache des Skripts darf nicht leer sein.',
              'not-valid': 'Bitte wählen Sie eine vorgeschlagene Sprache aus.'
            },
            'software-package': {
              'not-found': 'Kein gültiges Softwarepaket gefunden.',
              'not-null': 'Das Softwarepaket darf nicht leer sein.',
              'not-valid': 'Bitte wählen Sie ein vorgeschlagenes Softwarepaket aus.'
            },
            'software-package-version': {
              'string-size': 'Die Maximallänge der Version des Softwarepakets ist 32 Zeichen.',
              'string-entire-not-empty': 'Die Version des Softwarepakets muss vorhanden sein.'
            }
          },
          'script-attachment-metadata': {
            'script-uuid': {
              'not-unique': 'Eine Datei mit dieser Skript Uuid existiert bereits,',
              '.not-exists': 'Diese Skript Uuid extistiert nicht.'
            },
            'filename': {
              'not-empty': 'Es muss eine Datei ausgewählt sein',
              'not-unique': 'Eine Datei mit diesem Namen existiert bereits',
              'not-valid': 'Dieser Dateiname ist ungültig',
              'pattern': 'Es dürfen für die FDZ-ID nur alphanumerische Zeichen, deutsche Umlaute, ß, Minus und der Unterstrich verwendet werden.'
            },
            'file-not-found': 'Die Datei {{ filename }} wurde nicht gefunden und wurde daher nicht gespeichert!'
          },
          'analysis-package': {
            'title': {
              'not-null': 'Der Titel eines Analysepaket darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge des Titels eines Analysepaket ist 2048 Zeichen.',
              'i18n-string-entire-not-empty': 'Der Titel eines Analysepaket muss in allen Sprachen vorhanden sein.'
            },
            'annotations': {
              'i18n-string-size': 'Die Maximallänge der Anmerkungen ist 2048 Zeichen.'
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
            'license': {
              'string-size': 'Die Maximallänge der Lizenz ist 1048576 Zeichen.'
            },
            'sponsor': {
              'not-null': 'Die Geldgeber:in eines Analysepakets darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge der Geldgeber:in eines Analysepakets ist 512 Zeichen.',
              'i18n-string-entire-not-empty': 'Die Geldgeber:in eines Analysepakets muss in beiden Sprachen vorhanden sein.'
            },
            'additional-links': {
              'invalid-url': 'Die angegebene URL ist ungültig',
              'url-size': 'Die Maximallänge der URL ist 2000 Zeichen.',
              'url-not-empty': 'Die URL darf nicht leer sein.',
              'display-text-size': 'Die Maximallänge des Anzeigetextes ist 512 Zeichen.'
            },
            'analysis-data-packages': {
              'package-type': {
                'i18n-not-null': 'Der Typ eines Datenpaket darf nicht leer sein!'
              }
            }
          },
          'external-data-package': {
            'title': {
              'not-null': 'Der Titel eines externen Datenpakets darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge des Titels eines externen Datenpakets ist 2048 Zeichen.',
              'i18n-string-entire-not-empty': 'Der Titel eines externen Datenpakets muss in allen Sprachen vorhanden sein.'
            },
            'annotations': {
              'i18n-string-size': 'Die Maximallänge der Anmerkungen ist 2048 Zeichen.'
            },
            'description': {
              'not-null': 'Die Beschreibung eines externen Datenpakets darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge der Beschreibung eines externen Datenpakets ist 2048 Zeichen.',
              'i18n-string-not-empty': 'Die Beschreibung eines externen Datenpakets muss in beiden Sprachen vorhanden sein.'
            },
            'data-source': {
              'i18n-string-not-empty': 'Die Datenquelle eines externen Datenpakets darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge der Datenquelle eines externen Datenpakets ist 512 Zeichen.',
              'i18n-string-entire-not-empty': 'Die Anmerkung eines Datenquelle muss in beiden Sprachen vorhanden sein.'
            },
            'data-source-url': {
              'invalid-url': 'Die angegebene URL ist ungültig',
              'url-size': 'Die Maximallänge der URL ist 2000 Zeichen.',
              'url-not-empty': 'Die URL darf nicht leer sein.'
            },
            'availability-type': {
              'i18n-not-null': 'Der Zugangsstatus eines externen Datenpakets darf nicht leer sein!'
            }
          },
          'custom-data-package': {
            'title': {
              'not-null': 'Der Titel eines benutzerdefinierten Datenpaket darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge des Titels eines benutzerdefinierten Datenpaket ist 2048 Zeichen.',
              'i18n-string-entire-not-empty': 'Der Titel eines benutzerdefinierten Datenpaket muss in allen Sprachen vorhanden sein.'
            },
            'annotations': {
              'i18n-string-size': 'Die Maximallänge der Anmerkungen ist 2048 Zeichen.'
            },
            'description': {
              'not-null': 'Die Beschreibung eines benutzerdefinierten Datenpakets darf nicht leer sein!',
              'i18n-string-size': 'Die Maximallänge der Beschreibung eines benutzerdefinierten Datenpakets ist 2048 Zeichen.',
              'i18n-string-not-empty': 'Die Beschreibung eines benutzerdefinierten Datenpakets muss in beiden Sprachen vorhanden sein.'
            },
            'availability-type': {
              'i18n-not-null': 'Der Zugangsstatus eines benutzerdefinierten Datenpaket darf nicht leer sein!'
            },
            'access-way': {
              'i18n-not-null': 'Der Zugangsweg eines Datenpaket darf nicht leer sein!'
            },
            'data-source': {
              'i18n-not-null': 'Die Datenquelle eines benutzerdefinierten Datenpaket darf nicht leer sein!'
            }
          },
          'analyzed-data-package': {
            'data-package-master-id': {
              'not-empty': 'Es muss ein FDZ-DZHW Datenpaket ausgewählt sein.',
              'not-found': 'Kein gültiges FDZ-DZHW Datenpaket gefunden.',
              'not-valid': 'Please select one of the provided FDZ-DZHW Datenpakete.'
            },
            'version': {
              'not-empty': 'Die Version eines Datenpaket darf nicht leer sein!'
            },
            'access-way': {
              'not-empty': 'Der Zugangsweg eines Datenpaket darf nicht leer sein!'
            }
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
