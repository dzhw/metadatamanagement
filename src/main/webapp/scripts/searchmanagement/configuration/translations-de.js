'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var searchTranslations = {
      //jscs:disable
      'search-management': {
        'delete-messages': {
          'delete-variables-title': 'Alle Variablen ersetzen?',
          'delete-variables': 'Sind Sie sicher, dass Sie alle Variablen innerhalb des Datenaufbereitungsprojekts "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-surveys-title': 'Alle Erhebungen ersetzen?',
          'delete-surveys': 'Sind Sie sicher, dass Sie alle Erhebungen innerhalb des Datenaufbereitungsprojekts "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-questions-title': 'Alle Fragen ersetzen?',
          'delete-questions': 'Sind Sie sicher, dass Sie alle Fragen innerhalb des Datenaufbereitungsprojekts mit der FDZ-ID "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-data-sets-title': 'Alle Datensätze ersetzen?',
          'delete-data-sets': 'Sind Sie sicher, dass Sie alle Datensätze innerhalb des Datenaufbereitungsprojekts mit der FDZ-ID "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-studies-title': 'Studie ersetzen?',
          'delete-studies': 'Sind Sie sicher, dass Sie die Studie des Datenaufbereitungsprojekts mit der FDZ-ID "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-related-publications-title': 'Alle Publikationen ersetzen?',
          'delete-related-publications': 'Sind Sie sicher, dass Sie die Publikationen mit den übergebenen Daten ersetzen möchten?',
          'delete-instruments-title': 'Alle Instrumente ersetzen?',
          'delete-instruments': 'Sind Sie sicher, dass Sie alle Instrumente innerhalb des Datenaufbereitungsprojekts mit der FDZ-ID "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
        },
        'detail': {
          'search': 'Datensuche'
        },
        'buttons': {
          'refresh-tooltip': 'Klicken, um die Suche zu aktualisieren',
          'upload-variables-tooltip': 'Klicken, um Variablen für das ausgewählte Datenaufbereitungsprojekt hochzuladen',
          'upload-surveys-tooltip': 'Klicken, um Erhebungen für das ausgewählte Datenaufbereitungsprojekt hochzuladen',
          'upload-data-sets-tooltip': 'Klicken, um Datensätze für das ausgewählte Datenaufbereitungsprojekt hochzuladen',
          'upload-questions-tooltip': 'Klicken, um Fragen für das ausgewählte Datenaufbereitungsprojekt hochzuladen',
          'upload-studies-tooltip': 'Klicken, um die Studie mit Attachments hochzuladen',
          'upload-or-create-studies-tooltip': 'Studie erstellen',
          'upload-or-create-surveys-tooltip': 'Erhebung erstellen',
          'upload-or-create-instruments-tooltip': 'Instrument erstellen',
          'upload-or-create-data-sets-tooltip': 'Datensatz erstellen',
          'upload-or-edit-studies-tooltip': 'Klicken, um die Studie zu bearbeiten',
          'create-study-tooltip': 'Klicken, um die Studie zu erstellen',
          'create-survey-tooltip': 'Klicken, um eine neue Erhebung zu erstellen',
          'edit-study-tooltip': 'Klicken, um die Studie zu bearbeiten',
          'edit-survey-tooltip': 'Klicken, um die Erhebung zu bearbeiten',
          'delete-survey-tooltip': 'Klicken, um die Erhebung zu löschen',
          'create-instrument-tooltip': 'Klicken, um ein neues Instrument zu erstellen',
          'edit-instrument-tooltip': 'Klicken, um das Instrument zu bearbeiten',
          'delete-instrument-tooltip': 'Klicken, um das Instrument zu löschen',
          'delete-all-instruments-tooltip': 'Klicken, um alle Instrumente des Datenaufbereitungsprojekts zu löschen',
          'create-data-set-tooltip': 'Klicken, um einen neuen Datensatz zu erstellen',
          'edit-data-set-tooltip': 'Klicken, um den Datensatz zu bearbeiten',
          'delete-data-set-tooltip': 'Klicken, um den Datensatz zu löschen',
          'upload-related-publications-tooltip': 'Klicken, um Publikationen hochzuladen',
          'upload-instruments-tooltip': 'Klicken, um Instrumente für das ausgewählte Datenaufbereitungsprojekt hochzuladen',
          'previous-search-result-tooltip': 'Klicken (oder STRG+"\u21E6"), um das {{ index }}. Suchergebnis ({{ id }}) anzuzeigen',
          'next-search-result-tooltip': 'Klicken (oder STRG+"\u21E8"), um das {{ index }}. Suchergebnis ({{ id }}) anzuzeigen',
          'delete-all-studies-tooltip': 'Klicken, um alle Studien des Datenaufbereitungsprojekts zu löschen',
          'delete-all-questions-tooltip': 'Klicken, um alle Fragen des Datenaufbereitungsprojekts zu löschen',
          'delete-all-variables-tooltip': 'Klicken, um alle Variablen des Datenaufbereitungsprojekts zu löschen',
          'delete-all-surveys-tooltip': 'Klicken, um alle Erhebungen des Datenaufbereitungsprojekts zu löschen',
          'delete-all-data-sets-tooltip': 'Klicken, um alle Datensätze des Datenaufbereitungsprojekts zu löschen',
          'edit-surveys-tooltip': 'Klicken, um die vorhandenen Erhebungen zu bearbeiten',
          'edit-instruments-tooltip': 'Klicken, um die vorhandenen Instrumente zu bearbeiten',
          'edit-data-sets-tooltip': 'Klicken, um die vorhandenen Datensätze zu bearbeiten'
        },
        'input-label': {
          'all': 'Suchen Sie Studien, Variablen, Fragen, Erhebungen, Datensätze, Instrumente oder Publikationen...',
          'variables': 'Suchen Sie nach Variablen...',
          'questions': 'Suchen Sie nach Fragen...',
          'surveys': 'Suchen Sie nach Erhebungen...',
          'data-sets': 'Suchen Sie nach Datensätzen...',
          'studies': 'Suchen Sie nach Studien...',
          'related-publications': 'Suchen Sie nach Publikationen...',
          'instruments': 'Suchen Sie nach Instrumenten...'
        },
        'no-results-text': {
          'all': 'Keine Ergebnisse zu Ihrer Suchanfrage gefunden.',
          'variables': 'Keine Variablen zu Ihrer Suchanfrage gefunden.',
          'questions': 'Keine Fragen zu Ihrer Suchanfrage gefunden.',
          'surveys': 'Keine Erhebungen zu Ihrer Suchanfrage gefunden.',
          'data-sets': 'Keine Datensätze zu Ihrer Suchanfrage gefunden.',
          'studies': 'Keine Studien zu Ihrer Suchanfrage gefunden.',
          'related-publications': 'Keine Publikationen zu Ihrer Suchanfrage gefunden.',
          'instruments': 'Keine Instrumente zu Ihrer Suchanfrage gefunden.',
          'sponsor': 'Keine Ergebnisse zu Ihrer Suchanfrage gefunden'
        },
        'tabs': {
          'variables': 'Variablen',
          'variables-found': '{number} {number, plural, =0{Variablen} =1{Variable} other{Variablen}} gefunden.',
          'variables-tooltip': 'Klicken, um nach Variablen zu suchen',
          'questions': 'Fragen',
          'questions-found': '{number} {number, plural, =0{Fragen} =1{Frage} other{Fragen}} gefunden.',
          'questions-tooltip': 'Klicken, um nach Fragen zu suchen',
          'surveys': 'Erhebungen',
          'surveys-found': '{number} {number, plural, =0{Erhebungen} =1{Erhebung} other{Erhebungen}} gefunden.',
          'surveys-tooltip': 'Klicken, um nach Erhebungen zu suchen',
          'data_sets': 'Datensätze',
          'data_sets-found': '{number} {number, plural, =0{Datensätze} =1{Datensatz} other{Datensätze}} gefunden.',
          'data_sets-tooltip': 'Klicken, um nach Datensätzen zu suchen',
          'studies': 'Studien',
          'studies-found': '{number} {number, plural, =0{Studien} =1{Studie} other{Studien}} gefunden.',
          'studies-tooltip': 'Klicken, um nach Studien zu suchen',
          'all': 'Alle',
          'all-tooltip': 'Klicken, um nach allen Objekten zu suchen',
          'related_publications': 'Publikationen',
          'related_publications-found': '{number} {number, plural, =0{Publikationen} =1{Publikation} other{Publikationen}} gefunden.',
          'related_publications-tooltip': 'Klicken, um nach Publikationen zu suchen',
          'instruments': 'Instrumente',
          'instruments-found': '{number} {number, plural, =0{Instrumente} =1{Instrument} other{Instrumente}} gefunden.',
          'instruments-tooltip': 'Klicken, um nach Instrumenten zu suchen'
        },
        'cards': {
          'question-tooltip': 'Klicken, um die Frage "{{id}}" anzuzeigen',
          'variable-tooltip': 'Klicken, um die Variable "{{id}}" anzuzeigen',
          'data-set-tooltip': 'Klicken, um den Datensatz "{{id}}" anzuzeigen',
          'instrument-tooltip': 'Klicken, um das Instrument "{{id}}" anzuzeigen',
          'survey-tooltip': 'Klicken, um die Erhebung "{{id}}" anzuzeigen',
          'study-tooltip': 'Klicken, um die Studie "{{id}}" anzuzeigen',
          'publication-tooltip': 'Klicken, um die Publikation "{{id}}" anzuzeigen'
        },
        'filter': {
          'study': 'Studie',
          'data-set': 'Datensatz',
          'question': 'Frage',
          'related-publication': 'Publikation',
          'panel-identifier': 'Panelkennung',
          'derived-variables-identifier': 'Abgeleiteten Variablen',
          'access-way': 'Zugangsweg',
          'instrument': 'Instrument',
          'variable': 'Variable',
          'survey': 'Erhebung',
          'study-series': 'Studienreihe',
          'study-series-de': 'Studienreihe',
          'study-series-en': 'Studienreihe',
          'institution-de': 'Institut',
          'institution-en': 'Institut',
          'sponsor-de': 'Sponsor',
          'sponsor-en': 'Sponsor',
          'survey-method-de': 'Erhebungsmethode',
          'survey-method-en': 'Erhebungsmethode',
          'floating-label': {
            'survey': 'Nach welcher Erhebung wollen Sie filtern?',
            'instrument': 'Nach welchem Instrument wollen Sie filtern?',
            'study': 'Nach welcher Studie wollen Sie filtern?',
            'data-set': 'Nach welchem Datensatz wollen Sie filtern?',
            'related-publication': 'Nach welcher Publikation wollen Sie filtern?',
            'panel-identifier': 'Nach welcher Panelkennung wollen Sie filtern?',
            'derived-variables-identifier': 'Nach welchen abgeleiteten Variablen wollen Sie filtern?',
            'access-way': 'Nach welchem Zugangsweg wollen Sie filtern?',
            'variable': 'Nach welcher Variable wollen Sie filtern?',
            'question': 'Nach welcher Frage wollen Sie filtern?',
            'study-series': 'Nach welcher Studienreihe wollen Sie filtern?',
            'institution': 'Nach welchem Institut wollen Sie filtern?',
            'sponsor': 'Nach welchem Sponsor wollen Sie filtern?',
            'survey-method': 'Nach welcher Erhebungsmethode wollen Sie filtern?'
          },
          'input-label': {
            'studies': 'Filter für die Studiensuche...',
            'surveys': 'Filter für die Erhebungssuche...',
            'instruments': 'Filter für die Instrumentensuche...',
            'questions': 'Filter für die Fragensuche...',
            'data_sets': 'Filter für die Datensatzsuche...',
            'variables': 'Filter für die Variablensuche...',
            'related_publications': 'Filter für die Publikationssuche...'
          },
          'clear-filters-tooltip': 'Klicken, um alle Filter zu entfernen',
          'uncollapse-filters-tooltip': {
            'true': 'Klicken, um die ausgewählten Filter einzublenden',
            'false': 'Klicken, um die ausgewählten Filter auszublenden'
          },
          'filter-search-input-label': 'Filtern nach...',
          'study-filter': {
            'not-found': 'Keine Studie gefunden!',
            'no-valid-selected': 'Keine gültige Studie ausgewählt!'
          },
          'survey-filter': {
            'not-found': 'Keine Erhebung gefunden!',
            'no-valid-selected': 'Keine gültige Erhebung ausgewählt!'
          },
          'instrument-filter': {
            'not-found': 'Kein Instrument gefunden!',
            'no-valid-selected': 'Kein gültiges Instrument ausgewählt!'
          },
          'question-filter': {
            'not-found': 'Keine Frage gefunden!',
            'no-valid-selected': 'Keine gültige Frage ausgewählt!'
          },
          'data-set-filter': {
            'not-found': 'Kein Datensatz gefunden!',
            'no-valid-selected': 'Kein gültiger Datensatz ausgewählt!'
          },
          'variable-filter': {
            'not-found': 'Keine Variable gefunden!',
            'no-valid-selected': 'Keine gültige Variable ausgewählt!'
          },
          'related-publication-filter': {
            'not-found': 'Keine Publikation gefunden!',
            'no-valid-selected': 'Keine gültige Publikation ausgewählt!'
          },
          'panel-identifier-filter': {
            'not-found': 'Keine Panelkennung gefunden!',
            'no-valid-selected': 'Keine gültige Panelkennung ausgewählt!'
          },
          'derived-variables-identifier-filter': {
            'not-found': 'Keine abgeleitete Variablenkennung gefunden!',
            'no-valid-selected': 'Keine gültige abgeleitete Variablenkennung ausgewählt!'
          },
          'access-way-filter': {
            'not-found': 'Kein Zugangsweg gefunden!',
            'no-valid-selected': 'Kein gültiger Zugangsweg ausgewählt!'
          },
          'study-series-filter': {
            'not-found': 'Keine vorhandene Studienreihe gefunden!',
            'no-valid-selected': 'Keine gültige Studienreihe ausgewählt!'
          },
          'sponsor-filter': {
            'not-found': 'Kein vorhandener Sponsor gefunden!',
            'no-valid-selected': 'Kein gültiger Sponsor ausgewählt!'
          },
          'institution-filter': {
            'not-found': 'Kein vorhandenes Institut gefunden!',
            'no-valid-selected': 'Kein gültiges Institut ausgewählt!'
          },
          'survey-method-filter': {
            'not-found': 'Keine vorhandene Erhebungsmethode gefunden!',
            'no-valid-selected': 'Keine gültige Erhebungsmethode ausgewählt!'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', searchTranslations);
  });
