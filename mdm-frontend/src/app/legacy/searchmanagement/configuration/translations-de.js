'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',

  function($translateProvider) {
    var searchTranslations = {
      //jscs:disable
      'search-management': {
        'packages': {
          'label': 'Wonach suchen Sie?',
          'data-packages': 'Datenpakete (SUFs, CUFs)',
          'analysis-packages': 'Analysepakete (Skripte)',
          'publications': 'Publikationen'
        },
        'delete-messages': {
          'delete-variables-title': 'Alle Variablen ersetzen?',
          'delete-variables': 'Sind Sie sicher, dass Sie alle Variablen innerhalb des Datenaufnahmeprojekts "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-surveys-title': 'Alle Erhebungen ersetzen?',
          'delete-surveys': 'Sind Sie sicher, dass Sie alle Erhebungen innerhalb des Datenaufnahmeprojekts "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-questions-title': 'Alle Fragen ersetzen?',
          'delete-questions': 'Sind Sie sicher, dass Sie alle Fragen innerhalb des Datenaufnahmeprojekts mit der FDZ-ID "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-data-sets-title': 'Alle Datensätze ersetzen?',
          'delete-data-sets': 'Sind Sie sicher, dass Sie alle Datensätze innerhalb des Datenaufnahmeprojekts mit der FDZ-ID "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-data-packages-title': 'Datenpaket ersetzen?',
          'delete-data-packages': 'Sind Sie sicher, dass Sie das Datenpaket des Datenaufnahmeprojekts mit der FDZ-ID "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-related-publications-title': 'Alle Publikationen ersetzen?',
          'delete-related-publications': 'Sind Sie sicher, dass Sie die Publikationen mit den übergebenen Daten ersetzen möchten?',
          'delete-instruments-title': 'Alle Instrumente ersetzen?',
          'delete-instruments': 'Sind Sie sicher, dass Sie alle Instrumente innerhalb des Datenaufnahmeprojekts mit der FDZ-ID "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
        },
        'search-result': {
          'dataPackageSearch': 'Suche "{{ searchQuery }}" im Datenpaket',
          'analysisPackageSearch': 'Suche "{{ searchQuery }}" im Analysepaket',
          'relatedPublicationsSearch': 'Suche "{{ searchQuery }}" in Publikation'
        },
        'detail': {
          'search': 'Datensuche',
          'dataPackageSearch': 'Datensuche',
          'noresult': 'Keine Suchergebnisse gefunden.',
          'version': 'Version',
          'access-way': 'Zugangsweg',
          'cart': 'In den Warenkorb legen',
          'data-packages-info': 'Datenpakete enthalten Scientific Use Files und/oder Campus Use Files, die für eine breite Nutzung in Wissenschaft bzw. Lehre vorgesehen sind.',
          'analysis-packages-info': 'Analysepakete enthalten Analyseskripte und Analysedaten, die zur Erstellung einer spezifischen Publikation verwendet wurden und ermöglichen damit deren Replikation.',
          'related-publications-info': 'Publikationen enthalten bibliografische Informationen zu Veröffentlichungen, die die Daten aus referenzierten Daten- und Analysepakete verwenden'
        },
        'buttons': {
          'refresh-tooltip': 'Klicken, um die Suche zu aktualisieren',
          'upload-variables-tooltip': 'Klicken, um Variablen für das ausgewählte Datenaufnahmeprojekt hochzuladen',
          'upload-surveys-tooltip': 'Klicken, um Erhebungen für das ausgewählte Datenaufnahmeprojekt hochzuladen',
          'upload-data-sets-tooltip': 'Klicken, um Datensätze für das ausgewählte Datenaufnahmeprojekt hochzuladen',
          'upload-questions-tooltip': 'Klicken, um Fragen für das ausgewählte Datenaufnahmeprojekt hochzuladen',
          'upload-data-packages-tooltip': 'Klicken, um das Datenpaket mit Attachments hochzuladen',
          'upload-or-create-data-packages-tooltip': 'Datenpaket erstellen',
          'upload-or-create-surveys-tooltip': 'Erhebung erstellen',
          'upload-or-create-instruments-tooltip': 'Instrument erstellen',
          'upload-or-create-data-sets-tooltip': 'Datensatz erstellen',
          'upload-or-edit-data-packages-tooltip': 'Klicken, um das Datenpaket zu bearbeiten',
          'create-data-package-tooltip': 'Klicken, um das Datenpaket zu erstellen',
          'create-analysis-package-tooltip': 'Klicken, um das Analysepaket zu erstellen',
          'create-survey-tooltip': 'Klicken, um eine neue Erhebung zu erstellen',
          'create-concept-tooltip': 'Klicken, um ein neues Konzept zu erstellen',
          'edit-data-package-tooltip': 'Klicken, um das Datenpaket zu bearbeiten',
          'edit-analysis-package-tooltip': 'Klicken, um das Analysepaket zu bearbeiten',
          'edit-concept-tooltip': 'Klicken, um das Konzept zu bearbeiten',
          'edit-survey-tooltip': 'Klicken, um die Erhebung zu bearbeiten',
          'delete-survey-tooltip': 'Klicken, um die Erhebung zu löschen',
          'delete-concept-tooltip': 'Klicken, um das Konzept zu löschen',
          'create-instrument-tooltip': 'Klicken, um ein neues Instrument zu erstellen',
          'edit-instrument-tooltip': 'Klicken, um das Instrument zu bearbeiten',
          'delete-instrument-tooltip': 'Klicken, um das Instrument zu löschen',
          'delete-all-instruments-tooltip': 'Klicken, um alle Instrumente des Datenaufnahmeprojekts zu löschen',
          'create-data-set-tooltip': 'Klicken, um einen neuen Datensatz zu erstellen',
          'edit-data-set-tooltip': 'Klicken, um den Datensatz zu bearbeiten',
          'delete-data-set-tooltip': 'Klicken, um den Datensatz zu löschen',
          'upload-related-publications-tooltip': 'Klicken, um Publikationen hochzuladen',
          'upload-instruments-tooltip': 'Klicken, um Instrumente für das ausgewählte Datenaufnahmeprojekt hochzuladen',
          'previous-search-result-tooltip': 'Klicken (oder STRG+"\u21E6"), um das {{ index }}. Suchergebnis ({{ id }}) anzuzeigen',
          'next-search-result-tooltip': 'Klicken (oder STRG+"\u21E8"), um das {{ index }}. Suchergebnis ({{ id }}) anzuzeigen',
          'delete-all-data-packages-tooltip': 'Klicken, um das Datenpaket des Datenaufnahmeprojekts zu löschen',
          'delete-all-analysis-packages-tooltip': 'Klicken, um das Analysepaket des Datenaufnahmeprojekts zu löschen',
          'delete-all-questions-tooltip': 'Klicken, um alle Fragen des Datenaufnahmeprojekts zu löschen',
          'delete-all-variables-tooltip': 'Klicken, um alle Variablen des Datenaufnahmeprojekts zu löschen',
          'delete-all-surveys-tooltip': 'Klicken, um alle Erhebungen des Datenaufnahmeprojekts zu löschen',
          'delete-all-data-sets-tooltip': 'Klicken, um alle Datensätze des Datenaufnahmeprojekts zu löschen',
          'edit-surveys-tooltip': 'Klicken, um die vorhandenen Erhebungen zu bearbeiten',
          'edit-instruments-tooltip': 'Klicken, um die vorhandenen Instrumente zu bearbeiten',
          'edit-data-sets-tooltip': 'Klicken, um die vorhandenen Datensätze zu bearbeiten',
          'edit-concepts-tooltip': 'Klicken, um die vorhandenen Konzepte zu bearbeiten',
          'edit-publications-tooltip': 'Klicken, um Publikationen zum Datenpaket hinzuzufügen bzw. zu entfernen',
          'delete-publications-tooltip': 'Klicken, um alle Publikationen von dem Datenpaket des aktuellen Projektes zu entfernen',
          'open-filter-panel': 'Klicken, um die Suchfilter anzuzeigen'
        },
        'input-label': {
          'all': 'Suchen Sie Datenpakete, Analysepakete, Variablen, Fragen, Erhebungen, Datensätze, Instrumente oder Publikationen...',
          'variables': 'Suchen Sie nach Variablen...',
          'questions': 'Suchen Sie nach Fragen...',
          'surveys': 'Suchen Sie nach Erhebungen...',
          'data-sets': 'Suchen Sie nach Datensätzen...',
          'data-packages': 'Suchen Sie nach Datenpaketen...',
          'analysis-packages': 'Suchen Sie nach Analysepaketen...',
          'related-publications': 'Suchen Sie nach Publikationen...',
          'instruments': 'Suchen Sie nach Instrumenten...',
          'concepts': 'Suchen Sie nach Konzepten...'
        },
        'no-results-text': {
          'all': 'Keine Ergebnisse zu Ihrer Suchanfrage gefunden.',
          'variables': 'Keine Variablen zu Ihrer Suchanfrage gefunden.',
          'questions': 'Keine Fragen zu Ihrer Suchanfrage gefunden.',
          'surveys': 'Keine Erhebungen zu Ihrer Suchanfrage gefunden.',
          'data-sets': 'Keine Datensätze zu Ihrer Suchanfrage gefunden.',
          'data-packages': 'Keine Datenpakete zu Ihrer Suchanfrage gefunden.',
          'analysis-packages': 'Keine Analysepakete zu Ihrer Suchanfrage gefunden.',
          'related-publications': 'Keine Publikationen zu Ihrer Suchanfrage gefunden.',
          'instruments': 'Keine Instrumente zu Ihrer Suchanfrage gefunden.',
          'concepts': 'Keine Konzepte zu Ihrer Suchanfrage gefunden.',
          'sponsors': 'Keine Ergebnisse zu Ihrer Suchanfrage gefunden'
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
          'concepts': 'Konzepte',
          'concepts-found': '{number} {number, plural, =0{Konzepte} =1{Konzept} other{Konzepte}} gefunden.',
          'concepts-tooltip': 'Klicken, um nach Konzepten zu suchen',
          'data_packages': 'Datenpakete',
          'data_packages-found': '{number} {number, plural, =0{Datenpakete} =1{Datenpaket} other{Datenpakete}} gefunden.',
          'data_packages-tooltip': 'Klicken, um nach Datenpaketen zu suchen',
          'analysis_packages': 'Analysepakete',
          'analysis_packages-found': '{number} {number, plural, =0{Analysepakete} =1{Analysepaket} other{Analysepakete}} gefunden.',
          'analysis_packages-tooltip': 'Klicken, um nach Analysepaketen zu suchen',
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
          'data-package-tooltip': 'Klicken, um das Datenpaket "{{id}}" anzuzeigen',
          'concept-tooltip': 'Klicken, um das Konzept "{{id}}" anzuzeigen',
          'publication-tooltip': 'Klicken, um die Publikation "{{id}}" anzuzeigen'
        },
        'filter': {
          'no-record': 'Keine Daten erfasst.',
          'data-package': 'Datenpaket',
          'analysis-package': 'Analysepaket',
          'concept': 'Konzept',
          'data-set': 'Datensatz',
          'question': 'Frage',
          'related-publication': 'Publikation',
          'repeated-measurement-identifier': 'Wiederholungsmessungen',
          'derived-variables-identifier': 'Abgeleitete Variablen',
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
          'transmissionViaVerbundFdb': 'Datenmeldung über den VerbundFDB',
          'floating-label': {
            'survey': 'Nach welcher Erhebung wollen Sie filtern?',
            'instrument': 'Nach welchem Instrument wollen Sie filtern?',
            'data-package': 'Nach welchem Datenpaket wollen Sie filtern?',
            'concept': 'Nach welchem Konzept wollen Sie filtern?',
            'data-set': 'Nach welchem Datensatz wollen Sie filtern?',
            'related-publication': 'Nach welcher Publikation wollen Sie filtern?',
            'repeated-measurement-identifier': 'Nach welcher Wiederholungsmessung wollen Sie filtern?',
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
            'data_packages': 'Filter für die Datenpaketsuche...',
            'analysis_packages': 'Filter für die Analysepaketsuche...',
            'surveys': 'Filter für die Erhebungssuche...',
            'instruments': 'Filter für die Instrumentensuche...',
            'questions': 'Filter für die Fragensuche...',
            'data_sets': 'Filter für die Datensatzsuche...',
            'variables': 'Filter für die Variablensuche...',
            'concepts': 'Filter für die Konzeptsuche...',
            'related_publications': 'Filter für die Publikationssuche...'
          },
          'clear-filters-tooltip': 'Klicken, um alle Filter zu entfernen',
          'uncollapse-filters-tooltip': {
            'true': 'Klicken, um die ausgewählten Filter einzublenden',
            'false': 'Klicken, um die ausgewählten Filter auszublenden'
          },
          'filter-search-input-label': 'Filtern nach...',
          'data-package-filter': {
            'not-found': 'Kein Datenpaket gefunden!',
            'no-valid-selected': 'Kein gültiges Datenpaket ausgewählt!'
          },
          'concept-filter': {
            'not-found': 'Kein Konzept gefunden!',
            'no-valid-selected': 'Keine gültiges Konzept ausgewählt!'
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
          'repeated-measurement-identifier-filter': {
            'not-found': 'Keine Wiederholungsmessung gefunden!',
            'no-valid-selected': 'Keine gültige Wiederholungsmessung ausgewählt!'
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
  }]);
