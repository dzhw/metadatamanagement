'use strict';

angular.module('metadatamanagementApp').config(
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
            'not-saved': 'Attachment "{{ id }}" wurde nicht gespeichert.',
            'missing-survey-number': 'Das Attachment einer Erhebung aus dem Exceldatei aus dem Arbeitsblatt "attachments" in der Zeile {{ index }} hat keine Erhebungsnummer und wurde daher nicht gespeichert.',
            'missing-filename': 'Das Attachment einer Erhebung aus der Exceldatei aus dem Arbeitsblatt "attachments" in der Zeile {{index}} hat keinen Dateinamen und wurde daher nicht gespeichert.',
            'file-not-found': 'Die Datei {{ filename }} wurde nicht gefunden und wurde daher nicht gespeichert!'
          }
        },
        'detail': {
          'label': {
            'survey': 'Erhebung',
            'surveys': 'Erhebungen',
            'surveys-same-study': 'Alle Erhebungen der Studie',
            'field-period': 'Feldzeit',
            'population': 'Grundgesamtheit',
            'data-type': 'Erhebungsdatentyp',
            'survey-method': 'Erhebungsmethode',
            'sample': 'Stichprobe',
            'annotations': 'Anmerkungen',
            'grossSampleSize': 'Bruttostichprobe',
            'sampleSize': 'Nettostichprobe',
            'responseRate': 'Rücklaufquote',
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
            'save-survey-before-adding-attachment': 'Die Erhebung muss erst gespeichert werden, bevor Materialien hinzugefügt werden können.',
            'hints': {
              'filename': 'Wählen Sie eine Datei aus, die Sie der Erhebung hinzufügen wollen.',
              'language': 'Wählen Sie die Sprache, die in der Datei verwendet wurde, aus.',
              'title': 'Geben Sie den Titel der Datei in der Dokumentensprache ein.',
              'description': {
                'de': 'Geben Sie eine Beschreibung dieser Datei auf Deutsch ein.',
                'en': 'Geben Sie eine Beschreibung dieser Datei auf Englisch ein.'
              }
            }
          },
          'title': '{{ title }} ({{ surveyId }})',
          'response-rate-information': 'Weitere Informationen zum Rücklauf',
          'not-found': 'Die id {{id}} referenziert auf eine unbekannte Erhebung.',
          'not-found-references': 'Die id {{id}} hat keine Referenzen auf Erhebungen.',
          'response-rate-information-alt-text': 'Grafische Darstellung der Rücklaufquote',
          'not-released-toast': 'Die Erhebung "{{ id }}" wurde noch nicht für alle Benutzer freigegeben!',
          'tooltips': {
            'surveys': {
              'many': 'Klicken, um alle Erhebungen dieser Studie anzuzeigen'
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
            'studies': {
              'one': 'Klicken, um die Studie dieser Erhebung anzuzeigen'
            }
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
              'not-null': 'Die Stichprobe der Erhebung darf nicht leer sein!',
              'i18n-string-not-empty': 'Die Stichprobe der Erhebung muss mindestens in einer Sprache vorliegen.',
              'i18n-string-size': 'Die Maximallänge der Stichprobe der Erhebung ist 2048 Zeichen.'
            },
            'wave': {
              'not-null': 'Die Welle der Erhebung darf nicht leer sein!',
              'min': 'Die Anzahl der Wellen muss mindestens 1 sein!'
            },
            'response-rate': {
              'min': 'Die Rücklaufquote darf nicht kleiner als 0% sein.',
              'max': 'Die Rücklaufquote darf nicht größer als 100% sein.',
              'invalid-number': 'Geben Sie eine gültige Zahl ein.'
            },
            'survey-method': {
              'not-null': 'Die Erhebungsmethode darf nicht leer sein!',
              'i18n-string-not-empty': 'Die Erhebungsmethode muss mindestens in einer Sprache vorliegen.',
              'i18n-string-size': 'Die Maximallänge der Erhebungsmethode ist 512 Zeichen.'
            },
            'sample-size': {
              'min': 'Die Stichprobengröße darf nicht kleiner als 0 sein.',
              'not-null': 'Die Stichprobengröße der Erhebung darf nicht leer sein!',
              'invalid-number': 'Geben Sie eine gültige Zahl ein.'
            },
            'gross-sample-size': {
              'min': 'Die Stichprobengröße darf nicht kleiner als 0 sein.',
              'invalid-number': 'Geben Sie eine gültige Zahl ein.'
            },
            'unique-survey-number': 'Die Nummer einer Erhebung muss eindeutig innerhalb eines Datenaufbereitungsprojektes sein!',
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
            'title': {
              'not-null': 'Der Titel der Grundgesamtheit darf nicht leer sein!',
              'i18n-string-not-empty': 'Der Titel der Grundgesamtheit muss mindestens in einer Sprache vorliegen.',
              'i18n-string-size': 'Die Maximallänge des Titels der Grundgesamtheit ist 512 Zeichen.'
            },
            'description': {
              'not-null': 'Die Beschreibung der Grundgesamtheit darf nicht leer sein!',
              'i18n-string-not-empty': 'Die Beschreibung der Grundgesamtheit muss mindestens in einer Sprache vorliegen.',
              'i18n-string-size': 'Die Maximallänge des Beschreibung der Grundgesamtheit ist 2048 Zeichen.'
            }
          },
          'survey-attachment-metadata': {
            'survey-id': {
              'not-empty': 'Die ID der zugehörigen Erhebung darf nicht leer sein.'
            },
            'survey-number': {
              'not-null': 'Die Nummer der zugehörigen Erhebung darf nicht leer sein.'
            },
            'project-id': {
              'not-empty': 'Die ID des zugehörigen Datenaufbereitungsprojektes darf nicht leer sein.'
            },
            'description': {
              'not-null': 'Die Beschreibung des Attachments darf nicht leer sein.',
              'i18n-string-size': 'Die Beschreibung muss in mindestens einer Sprache angegeben werden und darf nicht länger als 512 Zeichen sein.',
              'i18n-string-not-empty': 'Die Beschreibung muss in mindestens einer Sprache vorhanden sein.'
            },
            'title': {
              'not-null': 'Der Title des Attachments darf nicht leer sein.',
              'string-size': 'Der Title des Attachments muss angegeben werden und darf nicht länger als 2048 Zeichen sein.'
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
          'edit-page-title': 'Erhebung {{surveyId}} bearbeiten',
          'create-page-title': 'Erhebung {{surveyId}} anlegen',
          'success-on-save-toast': 'Erhebung {{surveyId}} wurde erfolgreich gespeichert.',
          'error-on-save-toast': 'Ein Fehler trat beim Speichern von Erhebung {{surveyId}} auf!',
          'survey-has-validation-errors-toast': 'Die Erhebung wurde nicht gespeichert, weil es noch ungültige Felder gibt!',
          'previous-version-restored-toast': 'Die ältere Version von Erhebung {{ surveyId }} kann jetzt gespeichert werden.',
          'current-version-restored-toast': 'Die aktuelle Version von Erhebung {{ surveyId }} wurde wiederhergestellt.',
          'not-authorized-toast': 'Sie sind nicht berechtigt Erhebungen zu bearbeiten oder anzulegen!',
          'choose-unreleased-project-toast': 'Bitte wählen Sie ein Projekt aus, welches aktuell nicht freigegeben ist!',
          'survey-image-saved-toast': 'Die grafische Darstellung des Rücklaufs wurde gespeichert.',
          'survey-image-deleted-toast': 'Die grafische Darstellung des Rücklaufs wurde gelöscht.',
          'survey-deleted-toast': 'Die Erhebung {{ id }} wurde gelöscht.',
          'label': {
            'edit-survey': 'Erhebung bearbeiten:',
            'create-survey': 'Erhebung anlegen:',
            'title': 'Titel',
            'wave': 'Welle',
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
            'next-page-tooltip': 'Klicken, um ältere Versionen anzuzeigen.',
            'previous-page-tooltip': 'Klicken, um aktuellere Versionen anzuzeigen.',
            'title': 'Ältere Version der Erhebung {{ surveyId }} wiederherstellen',
            'text': 'Wählen Sie eine ältere Version der Erhebung aus, die wiederhergestellt werden soll:',
            'cancel-tooltip': 'Klicken, um ohne eine ältere Version der Erhebung auszuwählen zurückzukehren.',
            'no-versions-found': 'Es wurden keine älteren Versionen der Erhebung {{ surveyId }} gefunden.',
            'survey-title': 'Titel',
            'lastModified': 'Geändert',
            'lastModifiedBy': 'von',
            'current-version-tooltip': 'Dies ist die aktuelle Version!',
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
            'wave': 'Geben Sie die Nummer der Welle an bzw. lassen Sie die Nummer auf 1 falls nicht zutreffend.',
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
              'de': 'Beschreiben Sie die Stichprobe auf Deutsch.',
              'en': 'Beschreiben Sie die Stichprobe auf Englisch.'
            },
            'grossSampleSize': 'Geben Sie die Größe Ihrer Stichprobe (brutto) an.',
            'sampleSize': 'Geben Sie die tatsächliche Größe Ihrer Stichprobe (netto) an.',
            'responseRate': 'Geben Sie die Rücklaufquote in Prozent an.',
            'annotations': {
              'de': 'Geben Sie zusätzliche Anmerkungen zu der Erhebung hier auf Deutsch an.',
              'en': 'Geben Sie zusätzliche Anmerkungen zu der Erhebung hier auf Englisch an.',
            },
            'response-rate-image': {
              'available-after-save': 'Grafische Darstellungen des Verlaufs des Rücklaufs können nach dem Speichern der Erhebung hinzugefügt werden.',
              'de': 'Laden Sie eine grafische Darstellung des Rücklaufs (auf Deutsch) hoch.',
              'en': 'Laden Sie eine grafische Darstellung des Rücklaufs (auf Englisch) hoch.'
            },
            'survey-number': 'Wählen Sie eine freie Nummer für die neue Erhebung aus.'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
