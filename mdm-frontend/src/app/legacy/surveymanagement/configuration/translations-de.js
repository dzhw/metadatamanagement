'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',

  function($translateProvider) {
    var translations = {
      //jscs:disable
      'survey-management': {
        'log-messages': {
          'survey': {
            'saved': 'Erhebung mit FDZ-ID {{ id }} erfolgreich gespeichert!',
            'not-saved': 'Erhebung mit FDZ-ID {{ id }} wurde nicht gespeichert!',
            'missing-number': 'Die Erhebung in der Exceldatei in dem Arbeitsblatt "surveys" in der Zeile {{index}} enthält keine Nummer und wurde daher nicht gespeichert!',
            'upload-terminated': 'Upload von {{ totalSurveys }} Erhebungen, {{ totalImages }} Bildern und {{totalAttachments}} Attachments mit {{ totalErrors }} Fehlern beendet!',
            'unable-to-upload-image-file': 'Die Bilddatei "{{ file }}" konnte nicht hochgeladen werden!',
            'unable-to-read-image-file': 'Die Bilddatei "{{ file }}" konnte nicht gelesen werden!',
            'image-file-not-found': 'Die Bilddatei "{{ file }}" konnte nicht gefunden werden!',
            'unable-to-delete': 'Die Erhebungen konnten nicht gelöscht werden!',
            'cancelled': 'Upload von Erhebungen Abgebrochen!',
            'duplicate-survey-number': 'Die Nummer ({{ number }}) der Erhebung aus der Exceldatei aus dem Arbeitsblatt "surveys" in der Zeile {{ index }} wurde bereits verwendet.'
          },
          'survey-attachment': {
            'missing-survey-number': 'Das Attachment einer Erhebung aus der Exceldatei aus dem Arbeitsblatt "attachments" in der Zeile {{ index }} hat keine Erhebungsnummer und wurde daher nicht gespeichert.',
            'missing-filename': 'Das Attachment einer Erhebung aus der Exceldatei aus dem Arbeitsblatt "attachments" in der Zeile {{index}} hat keinen Dateinamen und wurde daher nicht gespeichert.'
          }
        },
        'detail': {
          'label': {
            'survey': 'Erhebung',
            'surveys': 'Erhebungen',
            'surveys-same-data-package': 'Alle Erhebungen dieses Datenpakets',
            'field-period': 'Feldzeit',
            'population': 'Grundgesamtheit',
            'unit': 'Erhebungseinheit',
            'geographic-coverage': 'Untersuchungsgebiet',
            'geographic-coverages': 'Untersuchungsgebiete',
            'data-type': 'Erhebungsdatentyp',
            'survey-method': 'Erhebungsmethode',
            'sample': 'Stichprobenverfahren',
            'annotations': 'Anmerkungen',
            'grossSampleSize': 'Bruttostichprobe',
            'sampleSize': 'Nettostichprobe',
            'responseRate': 'Rücklaufquote',
            'serial-number': 'Ordnungsnummer',
            'attachments': {
              'title': 'Titel',
              'description': 'Beschreibung',
              'language': 'Dokumentensprache',
              'file': 'Datei'
            }
          },
          'attachments': {
            'table-title': 'Materialien zu der Erhebung',
            'attachment-deleted-toast': 'Datei "{{ filename }}" wurde gelöscht!',
            'delete-attachment-tooltip': 'Klicken, um die Datei "{{ filename }}" zu löschen!',
            'edit-attachment-tooltip': 'Klicken, um die Datei "{{ filename }}" zu bearbeiten.',
            'select-attachment-tooltip': 'Klicken, um Datei "{{ filename }}" zum Verschieben auszuwählen.',
            'move-attachment-up-tooltip': 'Klicken, um die ausgewählte Datei nach oben zu verschieben.',
            'move-attachment-down-tooltip': 'Klicken, um die ausgewählte Datei nach unten zu verschieben.',
            'save-attachment-order-tooltip': 'Klicken, um die geänderte Reihenfolge der Dateien zu speichern.',
            'attachment-order-saved-toast': 'Die geänderte Reihenfolge der Dateien wurde gespeichert.',
            'add-attachment-tooltip': 'Klicken, um einen neue Datei zu dieser Erhebung hinzuzufügen.',
            'edit-title': 'Datei "{{ filename }}" von Erhebung "{{ surveyId }}" bearbeiten',
            'create-title': 'Neue Datei zu Erhebung "{{ surveyId }}" hinzufügen',
            'change-file-tooltip': 'Klicken, um eine Datei auszuwählen.',
            'open-choose-previous-version-tooltip': 'Klicken, um eine ältere Version der Metadaten wiederherzustellen.',
            'current-version-restored-toast': 'Die aktuelle Version der Metadaten von Datei "{{ filename }}" wurde wiederhergestellt.',
            'previous-version-restored-toast': 'Die ältere Version der Metadaten von Datei "{{ filename }}" kann jetzt gespeichert werden.',
            'language-not-found': 'Keine gültige Sprache gefunden!',
            'save-survey-before-adding-attachment': 'Die Erhebung muss erst gespeichert werden, bevor Materialien hinzugefügt werden können.',
            'hints': {
              'filename': 'Wählen Sie eine Datei aus, die Sie der Erhebung hinzufügen wollen.'
            }
          },
          'title': '{{ title }}',
          'description': 'Grundgesamtheit: {{ population }}',
          'response-rate-information': 'Weitere Informationen zum Rücklauf',
          'not-found': 'Die id {{id}} referenziert auf eine unbekannte Erhebung.',
          'not-found-references': 'Die id {{id}} hat keine Referenzen auf Erhebungen.',
          'response-rate-information-alt-text': 'Grafische Darstellung der Rücklaufquote',
          'not-released-toast': 'Die Erhebung "{{ id }}" wurde noch nicht für alle Benutzer:innen freigegeben!',
          'tooltips': {
            'surveys': {
              'many': 'Klicken, um alle Erhebungen dieses Datenpakets anzuzeigen'
            },
            'data-sets': {
              'one': 'Klicken, um den Datensatz dieser Erhebung anzuzeigen',
              'many': 'Klicken, um alle Datensätze dieser Erhebung anzuzeigen'
            },
            'publications': {
              'one': 'Klicken, um die Publikation zu dieser Erhebung anzuzeigen',
              'many': 'Klicken, um alle Publikationen zu dieser Erhebung anzuzeigen'
            },
            'instruments': {
              'one': 'Klicken, um das bei dieser Erhebung verwendete Instrument anzuzeigen',
              'many': 'Klicken, um alle bei dieser Erhebung verwendeten Instrumente anzuzeigen'
            },
            'data-packages': {
              'one': 'Klicken, um das Datenpaket dieser Erhebung anzuzeigen'
            },
            'questions': {
              'one': 'Klicken, um die Frage dieser Erhebung anzuzeigen.',
              'many': 'Klicken, um die Fragen dieser Erhebung anzuzeigen.'
            },
            'concepts': {
              'one': 'Klicken, um das Konzept, welches in dieser Erhebung gemessen wurde, anzuzeigen',
              'many': 'Klicken, um alle Konzepte, die in dieser Erhebung gemessen wurden, anzuzeigen'
            },
            'show-serial-number-help': 'Klicken, um Erläuterungen zur Ordnungsnummer zu anzuzeigen.'
          },
          'serial-number-info': {
            'title': 'Ordnungsnummer',
            'content': '<p>Die Ordnungsnummer der Erhebung repräsentiert die Nummer der Erhebung, wie sie im Erhebungsdesign angelegt ist (z.B. Nummer der Panelwelle).</p>',
            'close-tooltip': 'Klicken, um diese Erläuterungen zu schließen.'
          }
        },
        'error': {
          'survey': {
            'id': {
              'valid-survey-id-name': 'Die FDZ-ID der Erhebung entspricht nicht dem Muster: "sur-" + {FDZID} + "-sy" + {Number} + "$".',
              'not-empty': 'Die FDZ-ID der Erhebung darf nicht leer sein!',
              'size': 'Die Maximallänge der FDZ-ID der Erhebung ist 512 Zeichen.',
              'pattern': 'Es dürfen für die FDZ-ID nur alphanumerische Zeichen, deutsche Umlaute, ß, Ausrufezeichen, Minus und der Unterstrich verwendet werden.'
            },
            'title': {
              'i18n-string-size': 'Die Maximallänge des Titels der Erhebung ist 2048 Zeichen.',
              'i18n-string-entire-not-empty': 'Der Titel der Erhebung muss in allen Sprachen vorhanden sein.'
            },
            'field-period': {
              'not-null': 'Der Zeitraum der Erhebung darf nicht leer sein!'
            },
            'data-acquisition-project': {
              'id': {
                'not-empty': 'Die FDZ-ID des Projektes darf bei der Erhebung nicht leer sein!'
              }
            },
            'population': {
              'not-null': 'Die Grundgesamtheit der Erhebung darf nicht leer sein!'
            },
            'sample': {
              'not-null': 'Das Stichprobenverfahren der Erhebung darf nicht leer sein!'
            },
            'serial-number': {
              'not-null': 'Die Ordnungsnummer der Erhebung darf nicht leer sein!',
              'min': 'Die Ordnungsnummer muss mindestens 1 sein!',
              'invalid-number': 'Geben Sie eine gültige Zahl (kleiner als 2.147.483.648) ein.'
            },
            'response-rate': {
              'min': 'Die Rücklaufquote darf nicht kleiner als 0% sein.',
              'max': 'Die Rücklaufquote darf nicht größer als 100% sein.',
              'invalid-number': 'Geben Sie eine gültige Zahl ein.'
            },
            'survey-method': {
              'not-null': 'Die Erhebungsmethode darf nicht leer sein!',
              'i18n-string-entire-not-empty': 'Die Erhebungsmethode muss in beiden Sprachen vorliegen.',
              'i18n-string-size': 'Die Maximallänge der Erhebungsmethode ist 512 Zeichen.'
            },
            'sample-size': {
              'min': 'Die Stichprobengröße darf nicht kleiner als 0 sein.',
              'max': 'Die Nettostichprobe muss kleiner oder gleich der Bruttostichprobe sein bzw. kleiner als 2.147.483.648.',
              'not-null': 'Die Stichprobengröße der Erhebung darf nicht leer sein!',
              'invalid-number': 'Geben Sie eine gültige Zahl ein.'
            },
            'gross-sample-size': {
              'min': 'Die Bruttostichprobe muss größer oder gleich der Nettostichprobe sein. Sie darf auch leer sein.',
              'invalid-number': 'Geben Sie eine gültige Zahl (kleiner als 2.147.483.648) ein.'
            },
            'unique-survey-number': 'Die Nummer einer Erhebung muss eindeutig innerhalb eines Datenaufnahmeprojektes sein!',
            'number': {
              'not-null': 'Die Nummer der Erhebung darf nicht leer sein!'
            },
            'annotations': {
              'i18n-string-size': 'Die Maximallänge der Anmerkungen ist 2048 Zeichen.'
            },
            'data-type': {
              'not-null': 'Der Erhebungsdatentyp der Erhebung darf nicht leer sein.',
              'valid-data-type': 'Der Erhebungsdatentyp einer Erhebung darf nur folgende Werte einnehmen: de = "Quantitative Daten" und en = "Quantitative Data" oder de = "Qualitative Daten" und en = "Qualitative Data" '
            }
          },
          'population': {
            'description': {
              'not-null': 'Die Beschreibung der Grundgesamtheit darf nicht leer sein!',
              'i18n-string-not-empty': 'Die Beschreibung der Grundgesamtheit muss in beiden Sprachen vorliegen.',
              'i18n-string-size': 'Die Maximallänge des Beschreibung der Grundgesamtheit ist 2048 Zeichen.'
            }
          }
        },
        'edit': {
          'edit-page-title': 'Erhebung {{surveyId}} bearbeiten',
          'create-page-title': 'Erhebung {{surveyId}} anlegen',
          'success-on-save-toast': 'Erhebung {{surveyId}} wurde erfolgreich gespeichert.',
          'error-on-save-toast': 'Ein Fehler trat beim Speichern von Erhebung {{surveyId}} auf!',
          'survey-has-validation-errors-toast': 'Die Erhebung wurde nicht gespeichert, weil es noch ungültige Felder gibt!',
          'previous-version-restored-toast': 'Die ältere Version von Erhebung {{ surveyId }} kann jetzt gespeichert werden.',
          'current-version-restored-toast': 'Die aktuelle Version von Erhebung {{ surveyId }} wurde wiederhergestellt.',
          'not-authorized-toast': 'Sie sind nicht berechtigt Erhebungen zu bearbeiten oder anzulegen!',
          'choose-unreleased-project-toast': 'Erhebungen dürfen nur bearbeitet werden, wenn das Projekt aktuell nicht freigegeben ist!',
          'survey-image-saved-toast': 'Die grafische Darstellung des Rücklaufs wurde gespeichert.',
          'survey-image-deleted-toast': 'Die grafische Darstellung des Rücklaufs wurde gelöscht.',
          'survey-deleted-toast': 'Die Erhebung {{ id }} wurde gelöscht.',
          'label': {
            'edit-survey': 'Erhebung bearbeiten:',
            'create-survey': 'Erhebung anlegen:',
            'title': 'Titel',
            'field-period-start': 'Beginn Feldzeit',
            'field-period-end': 'Ende Feldzeit',
            'population': {
              'title': 'Titel der Grundgesamtheit',
              'description': 'Beschreibung der Grundgesamtheit'
            }
          },
          'open-choose-previous-version-tooltip': 'Klicken, um eine ältere Version dieser Erhebung wieder herzustellen.',
          'save-tooltip': 'Klicken, um die Erhebung zu speichern.',
          'choose-previous-version': {
            'title': 'Ältere Version der Erhebung {{ surveyId }} wiederherstellen',
            'text': 'Wählen Sie eine ältere Version der Erhebung aus, die wiederhergestellt werden soll:',
            'cancel-tooltip': 'Klicken, um ohne eine ältere Version der Erhebung auszuwählen zurückzukehren.',
            'no-versions-found': 'Es wurden keine älteren Versionen der Erhebung {{ surveyId }} gefunden.',
            'survey-deleted': 'Die Erhebung wurde gelöscht!'
          },
          'choose-survey-number': {
            'title': 'Auswahl einer freien Erhebungsnummer',
            'label': 'Freie Erhebungsnummern',
            'ok-tooltip': 'Klicken, um die Auswahl der Erhebungsnummer zu bestätigen.'
          },
          'response-rate-image': {
            'add-german-image-tooltip': 'Klicken, um ein deutschsprachiges Bild auszuwählen.',
            'add-english-image-tooltip': 'Klicken, um ein englischsprachiges Bild auszuwählen.',
            'delete-german-image-tooltip': 'Klicken, um das deutschsprachige Bild zu löschen.',
            'delete-english-image-tooltip': 'Klicken, um das englischsprachige Bild zu löschen.',
            'upload-or-delete-german-image-tooltip': 'Klicken, um die Änderungen am deutschsprachigen Bild zu speichern.',
            'upload-or-delete-english-image-tooltip': 'Klicken, um die Änderungen am englischsprachigen Bild zu speichern.'
          },
          'hints': {
            'title': {
              'de': 'Geben Sie den Titel der Erhebung auf Deutsch ein.',
              'en': 'Geben Sie den Titel der Erhebung auf Englisch ein.'
            },
            'serial-number': 'Bitte tragen Sie hier die Ordnungsnummer der Erhebung ein, wie sie im Erhebungsdesign angelegt ist (z.B. Nummer der Panelwelle).',
            'field-period-start': 'Geben Sie den Beginn der Feldzeit ein.',
            'field-period-end': 'Geben Sie das Ende der Feldzeit ein.',
            'survey-method': {
              'de': 'Beschreiben Sie die Erhebungsmethode auf Deutsch.',
              'en': 'Beschreiben Sie die Erhebungsmethode auf Englisch.'
            },
            'data-type': 'Wählen Sie den Erhebungsdatentyp aus.',
            'population': {
              'title': {
                'de': 'Geben Sie einen Titel für die Grundgesamtheit auf Deutsch an.',
                'en': 'Geben Sie einen Titel für die Grundgesamtheit auf Englisch an.'
              },
              'description': {
                'de': 'Beschreiben Sie die Grundgesamtheit auf Deutsch.',
                'en': 'Beschreiben Sie die Grundgesamtheit auf Englisch.'
              }
            },
            'sample': {
              'de': 'Beschreiben Sie das Stichprobenverfahren auf Deutsch.',
              'en': 'Beschreiben Sie das Stichprobenverfahren auf Englisch.'
            },
            'grossSampleSize': 'Geben Sie die Größe Ihrer Stichprobe (brutto) an.',
            'sampleSize': 'Geben Sie die tatsächliche Größe Ihrer Stichprobe (netto) an.',
            'responseRate': 'Geben Sie die Rücklaufquote in Prozent an.',
            'annotations': {
              'de': 'Geben Sie zusätzliche Anmerkungen zu der Erhebung hier auf Deutsch an.',
              'en': 'Geben Sie zusätzliche Anmerkungen zu der Erhebung hier auf Englisch an.'
            },
            'response-rate-image': {
              'available-after-save': 'Grafische Darstellungen des Verlaufs des Rücklaufs können nach dem Speichern der Erhebung hinzugefügt werden.',
              'de': 'Laden Sie eine grafische Darstellung des Rücklaufs (auf Deutsch) hoch.',
              'en': 'Laden Sie eine grafische Darstellung des Rücklaufs (auf Englisch) hoch.'
            },
            'survey-number': 'Wählen Sie eine freie Nummer für die neue Erhebung aus.'
          },
          'all-surveys-deleted-toast': 'Alle Erhebungen des Datenaufnahmeprojekts "{{id}}" wurden gelöscht.'
        },
        'sample-type-picker': {
          'label': 'Stichprobenverfahren',
          'error': {
            'required': 'Ein Stichprobenverfahren muss angegeben werden',
            'no-match': 'Keine passendes Stichprobenverfahren gefunden'
          }
        },
        'geographic-coverage-list': {
          'tooltip': {
            'move-item-up': 'Klicken, um dieses Untersuchungsgebiet nach oben zu verschieben',
            'move-item-down': 'Klicken, um dieses Untersuchungsgebiet nach unten zu verschieben',
            'add-geographic-coverage': 'Klicken, um ein neues Untersuchungsgebiet hinzuzufügen'
          },
          'hint': {
            'empty-list': 'Es sind keine Untersuchungsgebiete angegeben. Klicken Sie auf den Button unten links, um ein neues Untersuchungsgebiet anzulegen.'
          },
          'errors': {
            'empty': 'Es muss mindestens ein Untersuchungsgebiet angegeben werden!'
          }
        },
        'geographic-coverage': {
          'tooltip': {
            'delete': 'Klicken, um dieses Untersuchungsgebiet zu entfernen'
          },
          'label': {
            'country': 'Land',
            'country-not-found': 'Zu Ihrer Suche wurde kein Land gefunden',
            'description': {
              'de': 'Optionale Angaben zum Untersuchungsgebiet (in Deutsch)',
              'en': 'Optionale Angaben zum Untersuchungsgebiet (in Englisch)'
            }
          },
          'hints': {
            'country': 'Wählen Sie das Land aus, in dem die Erhebung durchgeführt wurde.',
            'description': 'Sie können hier zusätzliche Angaben zum Untersuchungsgebiet machen.'
          },
          'errors': {
            'required': 'Bitte wählen Sie ein Land aus!',
            'no-match': 'Das ist keine gültige Auswahl!',
            'maxlength': 'Die optionalen Angaben dürfen nicht mehr als 512 Zeichen enthalten!'
          }
        },
        'unit-value-picker': {
          'label': 'Wählen Sie eine Erhebungseinheit aus',
          'hints': {
            'unit': 'Wählen Sie eine Erhebungseinheit aus'
          },
          'errors': {
            'required': 'Eine Erhebungseinheit muss ausgewählt sein',
            'no-match': 'Keine Erhebungseinheit mit diesem Namen gefunden'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  }]);
