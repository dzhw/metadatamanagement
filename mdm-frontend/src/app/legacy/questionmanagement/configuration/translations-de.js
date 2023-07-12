'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'question-management': {
        'log-messages': {
          'question': {
            'saved': 'Frage mit FDZ-ID {{ id }} erfolgreich gespeichert!',
            'not-saved': 'Frage mit FDZ-ID {{ id }} wurde nicht gespeichert!',
            'missing-id': 'Die {{ index }}. Frage enthält keine FDZ-ID und wurde nicht gespeichert!',
            'upload-terminated': 'Upload von {{ totalQuestions }} Fragen und {{ totalImages }} Bildern mit {{totalWarnings}} Warnungen und {{ totalErrors }} Fehlern beendet!',
            'unable-to-delete': 'Die Fragen konnten nicht gelöscht werden!',
            'unable-to-read-image-file': 'Die Bilddatei "{{ file }}" in Instrument "{{instrument}}" konnte nicht gelesen werden!',
            'cancelled': 'Upload von Fragen Abgebrochen!',
            'technical-representation-success-copy-to-clipboard': 'Die technische Representation wurde erfolgreich in die Zwischenablage kopiert.',
            'unable-to-parse-json-file': 'Die JSON Datei "{{file}}" in Instrument "{{instrument}}" enthält kein valides JSON!',
            'unable-to-read-file': 'Die Datei "{{file}}" in Instrument "{{instrument}}" konnte nicht gelesen werden!',
            'non-unique-index-in-instrument': 'Mindestens zwei Fragen ({{firstQuestionId}}, {{secondQuestionId}}) haben den gleichen Index "{{index}}" innerhalb eines Instrument.'
          },
          'question-image': {
            'not-saved': 'Die Bilddatei "{{ id }}" in Instrument "{{instrument}}" konnte nicht hochgeladen werden!'
          },
          'question-image-metadata': {
            'unable-to-parse-json-file': 'Die JSON Datei "{{file}}" zur Fragenummer "{{questionNumber}}" enthält kein valides JSON!',
            'unable-to-read-file': 'Die Datei "{{file}}" zur Fragenumber "{{questionNumber}}" konnte nicht gelesen werden!',
            'unable-to-read-resolution': 'Die Auflösung des Bildes "{{file}}" zur Fragenummer "{{questionNumber}}" konnte nicht ausgegelesen werden!',
            'not-found-image-and-metadata-file': 'Zu der Frage "{{questionNumber}}" in Instrument "{{instrument}}" konnte kein Bild mit dem Namen "{{imageFilename}}" und keine dazugehörige JSON Metadatendatei mit dem Namen "{{metadataFilename}}" gefunden werden!',
            'not-found-image-file': 'Zu der Frage "{{questionNumber}}" in Instrument "{{instrument}}" konnte kein Bild mit dem Namen "{{imageFilename}}" gefunden werden!',
            'not-found-metadata-file': 'Zu der Frage "{{questionNumber}}" in Instrument "{{instrument}}" konnte keine dazugehörige JSON Metadatendatei mit dem Namen "{{metadataFilename}}" gefunden werden!',
            'not-depending-image-metadata': 'Zu der Frage "{{questionNumber}}" in Instrument "{{instrument}}" konnten keine Bild-Metadaten zugeordnet werden. Daher wurden keine Bilder zu dieser Frage hochgeladen.'
          }
        },
        'home': {
          'title': 'Fragen'
        },
        'detail': {
          'label': {
            'question': 'Frage',
            'questions': 'Fragen',
            'annotations': 'Anmerkungen',
            'type': 'Fragetyp',
            'topic': 'Thema',
            'instruction': 'Anleitung',
            'introduction': 'Einführung',
            'number': 'Fragenummer',
            'questionText': 'Fragetext',
            'language': 'Sprache',
            'note': 'Hinweis',
            'resolution': 'Auflösung',
            'resolution-hint': 'Abhängig von der Bildschirmauflösung wurde die Frage unterschiedlich dargestellt.',
            'has-annotations': 'Dieses Bild enthält zusätzliche Informationen, die den Befragten nicht dargestellt wurden.'
          },
          'predecessors': 'Vorangegangene Fragen im Fragebogen',
          'successors': 'Nachfolgende Fragen im Fragebogen',
          'images-for-question': 'Bilder zur Frage',
          'technical-representation': 'Technische Repräsentation',
          'title': 'Frage {{ questionNumber }}: {{ instrumentDescription }}',
          'description': 'Fragetext: {{ questionText }}',
          'no-predecessors': 'Keine vorangegangenen Fragen im Fragebogen',
          'no-successors': 'Keine nachfolgenden Fragen im Fragebogen',
          'not-found': 'Die id {{id}} referenziert auf eine unbekannte Frage.',
          'not-found-references': 'Die id {{id}} hat keine Referenzen auf Fragen.',
          'copy-technical-representation-to-clipboard-tooltip': 'Klicken, um den Inhalt der technischen Representation in die Zwischenablage zu kopieren',
          'show-complete-technical-representation-tooltip': {
            'true': 'Klicken, um die gesamte technische Representation zu zeigen',
            'false': 'Klicken, um den Inhaltsbereich der technischen Representation zu minimieren'
          },
          'change-image-language-tooltip': 'Klicken, um die Sprache der Bilder zu ändern',
          'not-released-toast': 'Die Frage "{{ id }}" wurde noch nicht für alle Benutzer:innen freigegeben!',
          'tooltips': {
            'publications': {
              'one': 'Klicken, um die Publikation zu dieser Frage anzuzeigen',
              'many': 'Klicken, um alle Publikationen zu dieser Frage anzuzeigen'
            },
            'instruments': {
              'one': 'Klicken, um das Instrument dieser Frage anzuzeigen'
            },
            'variables': {
              'one': 'Klicken, um die Variable dieser Frage anzuzeigen',
              'many': 'Klicken, um alle Variablen dieser Frage anzuzeigen'
            },
            'data-packages': {
              'one': 'Klicken, um das Datenpaket anzuzeigen, in dem diese Frage verwendet wurde'
            },
            'concepts': {
              'one': 'Klicken, um das Konzept, welches mit dieser Frage gemessen wurde, anzuzeigen',
              'many': 'Klicken, um alle Konzepte, die mit dieser Frage gemessen wurden, anzuzeigen'
            },
          }
        },
        'error': {
          'question': {
            'valid-question-id-name': 'Die FDZ-ID der Frage entspricht nicht dem Muster: "que" + {FDZID} + "-ins" + {InstrumentNummer} + "-" + {FrageNummer} + "$".',
            'unique-question-name': 'Der Name einer Frage muss eindeutig innerhalb einer Befragung sein.',
            'unique-question-number': 'Die Nummer einer Frage muss eindeutig innerhalb einer Befragung sein.',
            'id': {
              'not-empty': 'Die FDZ-ID der Frage darf nicht leer sein!',
              'size': 'Die Maximallänge der FDZ-ID ist 512 Zeichen.',
              'pattern': 'Es dürfen nur alphanumerische Zeichen, deutsche Umlaute, ß, Leerzeichen, Ausrufezeichen und Minus für die FDZ - ID verwendet werden.'
            },
            'number': {
              'not-empty': 'Die Nummer der Frage darf nicht leer sein.',
              'size': 'Die Maximallänge der Nummer ist 32 Zeichen.'
            },
            'question-text': {
              'not-null': 'Der Fragetext einer Frage darf nicht leer sein!',
              'i18n-string-not-empty': 'Der Fragetext muss in mind. einer Sprache vorliegen.',
              'i18n-string-size': 'Die Maximallänge der des Fragetextes ist 2048 Zeichen.'
            },
            'indexInInstrument': {
              'not-null': 'Der "Index innerhalb eines Instrumentes" einer Frage darf nicht leer sein!'
            },
            'instruction': {
              'i18n-string-size': 'Die Maximallänge der Instruktion ist 2048 Zeichen.'
            },
            'introduction': {
              'i18n-string-size': 'Die Maximallänge der Einleitung ist 2048 Zeichen.'
            },
            'type': {
              'not-null': 'Der Typ der Frage darf nicht leer sein.',
              'valid-question-type': 'Der Typ der Frage verwendet entweder nicht die gültigen Werte oder ist zwischen den Sprachen inkonsistent.'
            },
            'additional-question-text': {
              'i18n-string-size': 'Die Maximallänge des zusätzlichen Fragetextes ist 1048576 Zeichen.'
            },
            'topic': {
              'i18n-string-size': 'Die Maximallänge des Topics ist 2048 Zeichen.'
            },
            'data-acquisition-project-id': {
              'not-empty': 'Die FDZ-ID des Projektes darf nicht leer sein!'
            },
            'data-package-id': {
              'not-empty': 'Die Datenpaket-ID der Frage darf nicht leer sein!'
            },
            'instrument-number': {
              'not-null': 'Die Nummer des Instruments darf nicht leer sein.'
            },
            'annotations': {
              'i18n-string-size': 'Die Maximallänge der Anmerkungen ist 2048 Zeichen.'
            },
            'concept-ids': {
              'not-exists': 'Es gibt kein Konzept mit der angegebenen ID "{{invalidValue}}".'
            }
          },
          'question-image-metadata': {
            'image-type': {
              'not-null': 'Der Bildtyp bei den Bildmetadaten der Frage darf nicht leer sein.',
              'valid-question-image-type': 'Der Bildtyp bei den Bildmetadaten der Frage muss PNG sein.'
            },
            'language': {
              'not-empty': 'Die Sprache bei den Bildmetadaten der Frage darf nicht leer sein.',
              'size': 'Die Maximallänge der Sprache bei den Bildmetadaten ist 32 Zeichen.',
              'not-supported': 'Die Sprache muss eine gültige zweibuchstabige Abkürzung gemäß ISO 639-1 sein.'
            },
            'file-name': {
              'not-empty': 'Der Dateiname bei den Bildmetadaten der Frage darf nicht leer sein.',
              'not-valid': 'Der Dateiname ist ungültig.'
            },
            'contains-annotations': {
              'not-null': 'Der boolesche Wert "Contains Annotations" bei den Bildmetadaten der Frage darf nicht leer sein.'
            },
            'index-in-question': {
              'not-null': 'Der Index eines Bildes darf nicht leer sein.'
            },
            'data-acquisition-project-id': {
              'not-empty': 'Die FDZ-ID des Projektes darf nicht leer sein!'
            },
            'question-id': {
              'not-empty': 'Die Id der Frage darf nicht leer sein!'
            }
          },
          'technical-representation': {
            'type': {
              'size': 'Die Maximallänge des Typs einer technischen Repräsentation ist 32 Zeichen.',
              'not-empty': 'Der Typ der technischen Repräsentation darf nicht leer sein.'
            },
            'language': {
              'size': 'Die Maximallänge der Sprache einer technischen Repräsentation ist 32 Zeichen.',
              'not-empty': 'Die Sprache der technischen Repräsentation darf nicht leer sein.'
            },
            'source': {
              'size': 'Die Maximallänge des Quellcodes der technischen Repräsentation ist 1048576 Zeichen.',
              'not-empty': 'Der Quellcode der technischen Repräsentation darf nicht leer sein.'
            }
          },
          'post-validation': {
            'question-has-invalid-instrument-id': 'Die Frage {{id}} referenziert auf einen unbekannten Fragebogen ({{toBereferenzedId}}).',
            'question-has-invalid-successor': 'Die Frage {{id}} referenziert auf eine unbekannte Nachfolgefrage ({{toBereferenzedId}}).',
            'question-has-invalid-survey-id': 'Die Frage {{id}} referenziert auf einen unbekannte Erhebung ({{toBereferenzedId}}).',
            'non-unique-question-number-in-instrument': 'Die Frage {{id}} mit der Nummer {{additionalId}} ist nicht eindeutig innerhalb des Instruments {{toBereferenzedId}}.'
          }
        },
        'edit': {
          'all-questions-deleted-toast': 'Alle Fragen des Datenaufnahmeprojekts "{{id}}" wurden gelöscht.'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
