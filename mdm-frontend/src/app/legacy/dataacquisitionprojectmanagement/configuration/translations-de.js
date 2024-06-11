'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',

  function($translateProvider) {
    var translations = {
      //jscs:disable
      'data-acquisition-project-management': {
        'name': 'Name des Datenaufnahmeprojektes',
        'release': {
          'version': 'Version des Datenaufnahmeprojektes',
          'landing-page-de-title': 'Deutsch',
          'landing-page-en-title': 'Englisch',
          'landing-page-hint': 'Bitte wählen Sie die Sprache der DOI-Landingpage',
          'pin-to-start-page': 'Datenpaket auf Startseite anzeigen',
          'pin-to-start-page-hint': 'Markieren Sie dieses Kästchen, wenn das Datenpaket auf der Startseite des MDMs angezeigt werden soll.',
          'confirmed': {
            'local': 'Dies ist ein lokales System. Sind Sie sicher?',
            'test': 'Dies ist das Test-System! Sind Sie sicher?',
            'dev': 'Dies ist das Dev-System! Sind Sie sicher?',
            'prod': 'ACHTUNG: Dies ist das PRODUKTIV-System! Sind Sie sicher?',
          },
          'confirm-hint': 'Markieren Sie dieses Kästchen, wenn Sie das Projekt wirklich auf diesem System freigeben wollen!',
          'pre-release-hint': 'Das Projekt "{{id}}" ist mit dem Embargo-Datum {{date}} versehen und kann nur vorläufig freigegeben werden. Vorläufig freigegebene Projekte sind nicht in MDM such- oder bestellbar. Die vorläufige Freigabe kann bis zur endgültigen Freigabe nicht zurückgenommen werden. Die Bearbeitung des Projektes ist in der vorläufigen Freigabe weiterhin möglich. Nach Ablauf des Embargo-Datums kann das Projekt in der im Folgenden gewählten Version endgültig freigegeben werden.',
          'release-action': 'Freigeben',
          'pre-release-action': 'Vorläufig freigeben',
          'toTweet': "Auf X (ehemals Twitter) veröffentlichen. Der DOI-Link und die Version des Datenaufnahmeprojektes werden automatisch hinzugefügt.",
          'tweetTextLabel': "Tweet",
          'imagePath1': 'assets/images/thumb_twitter_fdz_1_de.jpg',
          'imagePath2': 'assets/images/thumb_twitter_fdz_2_de.jpg',
          'imagePath3': 'assets/images/thumb_twitter_fdz_3_de.jpg'
        },
        'home': {
          'title': 'Datenaufnahmeprojekte',
          'createLabel': 'Neues Datenaufnahmeprojekt anlegen',
          'releaseLabel': 'Das Datenaufnahmeprojekt "{{ id }}" freigeben',
          'prereleaseLabel': 'Das Datenaufnahmeprojekt "{{ id }}" vorläufig freigeben',
          'dialog-tooltip': {
            'create-ok': 'Klicken, um das Datenaufnahmeprojekt zu erzeugen',
            'create-cancel': 'Klicken, um den Dialog zu schließen ohne ein Projekt anzulegen',
            'release-ok': 'Klicken, um das Projekt freizugeben',
            'pre-release-ok': 'Klicken, um das Projekt vorläufig freizugeben',
            'release-cancel': 'Klicken, um den Dialog zu schließen ohne das Projekt freizugeben'
          }
        },
        'delete': {
          'question': 'Sind Sie sicher, dass Sie das Datenaufnahmeprojekt "{{ name }}" löschen möchten?'
        },
        'log-messages': {
          'data-acquisition-project': {
            'saved': 'Datenaufnahmeprojekt "{{ id }}" wurde erfolgreich gespeichert!',
            'server-error': 'Ein Fehler ist auf dem Server aufgetreten: ',
            'delete-title': 'Projekt "{{ id }}" löschen?',
            'delete': 'Möchten Sie wirklich das Projekt "{{ id }}" löschen? Das Projekt kann hiernach nicht wieder hergestellt werden.',
            'deleted-successfully-project': 'Das Datenaufnahmeprojekt "{{ id }}" wurde erfolgreich gelöscht.',
            'deleted-not-successfully-project': 'Das Datenaufnahmeprojekt "{{ id }}" konnte nicht gelöscht werden!',
            'released-successfully': 'Die Metadaten des Projektes wurden bei da|ra gespeichert und die Daten des Projektes "{{ id }}" werden in ca. 10 Minuten für alle Benutzer:innen sichtbar sein.',
            'released-beta-successfully': 'Die Daten des Projektes "{{ id }}" werden in ca. 10 Minuten für alle Benutzer:innen sichtbar sein. Es wurden keine Metadaten zu da|ra gesendet.',
            'dara-released-not-successfully': 'Die Daten des Projektes "{{ id }}" können nicht veröffentlicht werden. Der da|ra-Service zur Registrierung der DOI ist aktuell nicht verfügbar. Kontaktieren Sie den Administrator (fdz-feedback@dzhw.eu) und versuchen Sie es später erneut.',
            'unreleased-successfully': 'Die Daten des Projektes "{{ id }}" können jetzt bearbeitet werden.',
            'unrelease-title': 'Freigabe für Projekt "{{ id }}" zurücknehmen?',
            'unrelease': 'Möchten Sie wirklich die Freigabe zurücknehmen und die Metadaten des Projektes "{{ id }}" bearbeiten?',
            'release-not-possible-title': 'Projekt "{{ id }}" kann nicht freigegeben werden!',
            'release-not-possible': 'Das Projekt "{{ id }}" kann nicht freigegeben werden, weil bei der Post-Validierung Fehler aufgetreten sind!'
          }
        },
        'error': {
          'data-acquisition-project': {
            'assignee-group': {
              'not-null': 'Die zuständige Bearbeitergruppe (Publisher oder Datengeber:innen) darf nicht leer sein.',
              'not-assigned': 'Die Zuständigkeit für das Projekt kann nicht geändert werden, weil es Publishern zugewiesen ist.'
            },
            'configuration': {
              'not-null': 'Die Projektkonfiguration darf nicht leer sein'
            },
            'create': {
              'unauthorized': 'Projekte dürfen nur durch Publisher angelegt werden.'
            },
            'id': {
              'not-empty': 'Der Name des Datenaufnahmeprojekts darf nicht leer sein!',
              'pattern': 'Der Name eines Projektes darf nur aus Zahlen und kleinen Buchstaben (a-z) bestehen.',
              'size': 'Die Maximallänge des Names ist 32 Zeichen.',
              'exists': 'Es gibt bereits ein Datenaufnahmeprojekt mit diesem Namen.'
            },
            'has-been-released-before': {
              'not-null': 'Es muss angegeben sein, ob das Datenaufnahmeprojekts schon einmal veröffentlicht wurde oder nicht.'
            },
            'has-user-service-remarks': {
              'not-null': 'Es muss angegeben sein, ob ein Datenaufnahmeprojekt Bemerkungen für den User Service enthält oder nicht.'
            }
          },
          'configuration': {
            'data-providers': {
              'update-not-allowed': 'Es muss mindestens eine Datengeber:in eingetragen sein.'
            },
            'publishers': {
              'not-empty': 'Es muss mindestens ein Publisher eingetragen sein.',
              'unauthorized': 'Publisher dürfen nur durch andere Publisher gesetzt werden.'
            },
            'requirements': {
              'unauthorized': 'Pflichtfelder dürfen nur durch Publishers dieses Projekts geändert werden.',
              'publications-required-for-analysis-packages': 'Eine Publikation wird für ein Analysepaket benötigt.',
              'either-data-packages-or-analysis-packages-required': 'Es wird entweder ein Datenpaket oder Analysepaket benötigt.'
            }
          },
          'release': {
            'version': {
              'not-empty': 'Die Version darf nicht leer sein.',
              'pattern': 'Die Version muss von der Form "major.minor.patch" (z.B. "1.0.0") sein.',
              'not-parsable-or-not-incremented': 'Die Versionsnummer muss mindestens so hoch sein wie die letzte Version. Die letzte Version war "{{lastVersion}}".',
              'no-major-version-change': 'Die Versionsnummer muss mindestens so hoch sein wie die letzte Version ("{{lastVersion}}"). Sie muss kleiner sein als die nächste Major-Version ("{{nextMajorVersion}}").',
              'no-beta-version': 'Für das Projekt ist ein Embargo-Datum angegeben. Die Versionsnummer muss daher mindestens "1.0.0" sein.',
              'size': 'Die Version darf nicht länger als 32 Zeichen sein.'
            },
            'tweet': {
              'not-empty': 'Der Text darf nicht leer sein.',
              'size': 'Der Text darf nicht länger als 280 Zeichen sein.'
            }
          },
          'post-validation': {
            'project-has-no-dataPackage': 'Das Projekt mit der FDZ-ID {{ id }} enthält kein Datenpaket.',
            'project-has-no-analysisPackage': 'Das Projekt mit der FDZ-ID {{ id }} enthält kein Analysepaket.',
            'project-must-have-exactly-one-publication': 'Das Projekt mit der FDZ-ID {{ id }} muss genau eine Publikation enthalten.',
            'requirements-not-met': 'Es gibt noch Metadaten die nicht von den Publishern als "fertig" markiert wurden.',
            'no-embargo-date': 'Für das Projekt {{ id }} ist kein Embargo-Datum angegeben.',
            'project-has-no-survey': 'Das Projekt mit der FDZ-ID {{ id }} muss mindestens eine Erhebung enthalten.',
            'project-has-no-data-set': 'Das Projekt mit der FDZ-ID {{ id }} muss mindestens einen Datensatz enthalten.'
          },
          'project-update-access': {
            'project-selected': 'Für das Anlegen neuer Daten muss ein Projekt ausgewählt sein.',
            'update-for-publishers-allowed': 'Die Aktion ist nicht möglich, weil die Metadaten bereits durch den Publisher als "fertig" markiert wurden',
            'update-for-data-providers-allowed': 'Die Aktion ist nicht möglich, weil die Metadaten bereits durch die Publisher oder Datengeber:innen als "fertig" markiert wurden',
            'project-released': 'Die Aktion ist nicht möglich, weil das Projekt momentan für alle öffentlichen Nutzer:innen freigegeben ist.',
            'member-of-assigned-group': 'Die Aktion ist nicht möglich, weil das Projekt momentan der anderen Projektgruppe zugewiesen ist.',
            'embargo-date-not-expired': 'Die Aktion ist nicht möglich, da das Embargo-Datum noch nicht abgelaufen ist.',
            'assigned-to-project': 'Die Aktion ist nicht möglich, weil Sie dem Projekt nicht als Publisher oder Datengeber:in zugewiesen sind.',
            'not-required': 'Die Aktion ist nicht möglich, weil diese Metadaten in den Projekteinstellungen nicht als "erwartet" markiert wurden.',
            'prerequisite-missing-surveys': 'Die Aktion ist nicht möglich, weil das Projekt noch keine Erhebung enthält.',
            'prerequisite-missing-data_packages': 'Die Aktion ist nicht möglich, weil das Projekt noch kein Datenpaket enthält.'
          }
        },
        'projectstatuslabel': {
          'assigned-to': 'Zugewiesen an',
          'PUBLISHER': 'Publisher',
          'DATA_PROVIDER': 'Datengeber:innen'
        },
        'releasestatusbadge': {
          'released': 'Freigegeben',
          'unreleased': 'Nicht freigegeben',
          'pre-released': 'Vorläufig freigegeben'
        },
        'project-cockpit': {
          'title': 'Projekt-Cockpit ({{projectId}})',
          'header': 'Projekt-Cockpit',
          'search': {
            'placeholder': 'Suchen Sie Benutzer:innen...',
            'header-data-provider': 'Datengeber:innen dieses Projekts',
            'header-publisher': 'Publisher dieses Projekts',
            'no-users-found': 'Keine Benutzer:in gefunden!'
          },
          'tooltip': {
            'not-assigned': 'Das Projekt ist aktuell der anderen Benutzer:innengruppe zugewiesen.',
            'not-in-group': 'Sie gehören nicht dieser Benutzer:innengruppe an.',
            'not-in-publishers': 'Sie sind diesem Projekt nicht als Publisher zugewiesen'
          },
          'alert': {
            'title': 'Achtung',
            'noproject': 'Kein Projekt ausgewählt.',
            'close': 'Okay'
          },
          'label': {
            'ROLE_USER': 'User',
            'ROLE_ADMIN': 'Admin',
            'ROLE_DATA_PROVIDER': 'Datengeber:innen',
            'ROLE_PUBLISHER': 'Publisher'
          },
          'button': {
            'save': 'Klicken, um die Anpassungen zu speichern.',
            'save-assign': 'Klicken, um die Anpassungen zu speichern und das Projekt zuzuweisen.',
            'save-takeback': 'Klicken, um die Anpassungen zu speichern und das Projekt der Gruppe Publisher zuzuweisen.',
            'remove-user': 'Nutzer:in entfernen'
          },
          'list': {
            'empty-data-provider': 'Keine Datengeber:innen sind diesem Projekt zugeteilt.',
            'empty-publisher': 'Keine Publisher sind diesem Projekt zugeteilt.'
          },
          'tabs': {
            'status': 'Status',
            'config': 'Einstellungen',
            'versions': 'Versionen'
          },
          'release-settings': {
            'header': 'Einstellungen zur Veröffentlichung',
            'label': 'Embargo-Datum',
            'info': 'Wenn das Projekt vorerst vorläufig freigegeben werden soll, müssen Sie hier ein Embargo-Datum setzen, welches dem Zeitpunkt der geplanten Veröffentlichung entspricht.',
            'delete-tooltip': 'Embargo-Datum entfernen'
          },
          'requirements': {
            'header': 'Erwartete Metadaten',
            'dataPackages': 'Datenpaket',
            'surveys': 'Erhebungen',
            'instruments': 'Instrumente',
            'questions': 'Fragen',
            'dataSets': 'Datensätze',
            'variables': 'Variablen',
            'publications': 'Publikationen',
            'concepts': 'Konzepte',
            'setting-info': 'Die folgenden Metadaten müssen bereitgestellt werden, bevor dieses Projekt für alle Benutzer:innen freigegeben werden kann:',
            'analysisPackages': 'Analysepaket'
          },
          'config': {
            'assigned-group': 'Zugewiesene Benutzer:innengruppe',
            'released': 'Veröffentlicht',
            'expected': 'Erwartet',
            'ready': 'Fertig',
            'new': 'Neu',
            'edit': 'Bearbeiten',
            'upload': 'Hochladen',
            'delete': 'Löschen'
          },
          'message-dialog': {
            'title': 'Nachricht an {{recipient}}',
            'description': 'Geben Sie eine Nachricht ein, die an alle {{recipient}} dieses Projekts per E-Mail verschickt wird.',
            'label': 'Nachricht',
            'confirm': 'Zuweisen',
            'cancel': 'Abbrechen'
          },
          'no-data-providers-dialog': {
            'text': 'Es gibt keine eingetragenen Datengeber:innen für dieses Projekt. Wechseln Sie zu den Projekteinstellungen und tragen Sie mindestens eine Datengeber:in ein.'
          },
          'takeback-dialog': {
            'title': 'Projekt zurückziehen',
            'text': 'Möchten Sie das Projekt wirklich von der Gruppe Datengeber:innen zurückziehen?'
          },
          'versions': {
            'header': 'Liste aller Projektversionen (Schattenkopien):',
            'no-shadows': 'Das Projekt wurde noch nicht freigegeben.',
            'confirm-hide' : {
              'title': 'Freigegebene Version {{version}} des Projektes {{id}} verstecken?',
              'content': 'Sind Sie sicher, dass Sie die freigegebene Version {{version}} des Projektes {{id}} verstecken möchten? Datennutzer:innen können diese Version dann nicht mehr sehen.'
            },
            'confirm-unhide' : {
              'title': 'Version {{version}} des Projektes {{id}} wieder für alle sichtbar machen?',
              'content': 'Sind Sie sicher, dass Sie die Version {{version}} des Projektes {{id}} wieder für alle Nutzer:innen sichtbar machen möchten?'
            },
            'hiding-toast': 'Die Version {{version}} des Projektes {{id}} wird in ca. 10 Minuten nur noch für angemeldete Benutzer:innen sichtbar sein!',
            'unhiding-toast': 'Die Version {{version}} des Projektes {{id}} wird in ca. 10 Minuten wieder für alle Benutzer:innen sichtbar sein!',
            'button': {
              'hide-shadow': 'Diese Version ist aktuell für alle Benutzer:innen sichtbar. Klicken Sie hier, um die Version zu verstecken!',
              'unhide-shadow': 'Diese Version ist aktuell nicht für alle Benutzer:innen sichtbar. Klicken Sie hier, um die Version wieder sichtbar zu machen!',
              'pre-released': 'Diese Version unterliegt bis zum {{date}} einem Embargo durch die Datengeber:innen. Die Veröffentlichung kann erst nach diesem Datum erfolgen.'
            }
          }
        },
        'project-overview': {
          'header': 'Projektübersicht',
          'table': {
            'project-name': 'Projekt',
            'release-version': 'Aktuelle Version',
            'embargo-date': 'Embargo-Datum',
            'assigned-group': 'Zugewiesene Gruppe',
            'user-service-remarks': 'Bemerkungen User Service',
            'data-package-status': 'Datenpaket',
            'analysis-package-status': 'Analyse Paket',
            'surveys-status': 'Erhebungen',
            'instruments-status': 'Instrumente',
            'data-sets-status': 'Datensätze',
            'questions-status': 'Fragen',
            'variables-status': 'Variablen',
            'publications-status': 'Publikationen',
            'concepts-status': 'Konzepte',
            'publisher': 'Publisher',
            'data-provider': 'Datengeber:innen',
            'unreleased': 'nicht freigegeben',
            'not-available' : 'keine',
            'available': 'vorhanden',
            'tooltip': 'Klicken, um das Projekt-Cockpit dieses Projekts zu öffnen'
          },
          'pagination': {
            'previous': 'Klicken, um die vorherigen Projekte anzuzeigen',
            'next': 'Klicken, um die nächsten Projekte anzuzeigen',
            'current': 'Klicken, um die Projekte auf Seite {{number}} anzuzeigen'
          },
          'filter': {
            'label': {
              'assignee-group': 'Zugewiesen an',
              'release-state': 'Status',
              'datapackage-filter': 'Filter für Datenpakete',
              'user-service-remarks': 'Bemerkungen User Service'
            },
            'options': {
              'assignee-group': {
                'dataprovider': 'Datengeber:innen',
                'publisher': 'Publisher'
              },
              'release-state': {
                'released': 'Freigegeben',
                'unreleased': 'Nicht freigegeben',
                'pre-released': 'Vorläufig Freigegeben'
              },
              'datapackage-filter': {
                'variables': 'Variablen',
                'questions': 'Fragen',
                'publications': 'Publikationen',
                'concepts': 'Konzepte'
              },
              'user-service-remarks': {
                'with-additional-info': 'Bemerkungen vorhanden',
                'without-additional-info': 'keine Bemerkungen'
              }
            }
          },
          'no-project-msg': 'Ihrem Konto ist kein Projekt zugewiesen.',
          'no-search-results-msg': 'Keine Ergebnisse zu Ihrer Suchanfrage gefunden.',
          'pre-release-hint': '* Gekennzeichnete Projekte sind vorläufig freigegeben und unterliegen einem Embargo-Datum.'
        },
        'outdated-version-alert': 'Sie betrachten eine veraltete Version ({{oldVersion}}) dieser Seite. Wählen Sie die aktuelle Version ({{newVersion}}) im Seitenmenü.</a>',
        'version-not-found-alert': 'Ihr Link verweist auf eine Version ({{oldVersion}}) dieser Seite, die es nicht gibt. Hier wird die aktuelle Version ({{newVersion}}) dargestellt.',
        'not-master-alert': 'Sie betrachten eine {{hidden?"<u>versteckte</u>":""}} Schattenkopie ({{version}}). ',
        'no-order-allowed-alert': 'Sie betrachten eine {{hidden?"<u>versteckte</u>":""}} Schattenkopie ({{version}}), die nicht bestellbar ist. Wählen Sie eine bestellbare Version im Seitenmenü.',
        'current-version':'Hier geht es zur aktuellen Version!'
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  }]);
