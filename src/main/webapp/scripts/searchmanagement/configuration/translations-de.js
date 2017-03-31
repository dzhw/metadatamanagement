'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var searchTranslations = {
      //jscs:disable
      'search-management': {
        'delete-messages': {
          'delete-variables-title': 'Alle Variablen löschen?',
          'delete-variables': 'Sind Sie sicher dass, Sie alle Variablen innerhalb des Datenaufbereitungsprojekts mit der FDZ-ID "{{ id }}" löschen möchten?',
          'delete-surveys-title': 'Alle Erhebungen ersetzen?',
          'delete-surveys': 'Sind Sie sicher dass, Sie alle Erhebungen innerhalb des Datenaufbereitungsprojekts "{{ id }}" mit den übergebenen Daten ersetzen möchten?',
          'delete-questions-title': 'Alle Fragen löschen?',
          'delete-questions': 'Sind Sie sicher dass, Sie alle Fragen innerhalb des Datenaufbereitungsprojekts mit der FDZ-ID "{{ id }}" löschen möchten?',
          'delete-data-sets-title': 'Alle Datensätze löschen?',
          'delete-data-sets': 'Sind Sie sicher dass, Sie alle Datensätze innerhalb des Datenaufbereitungsprojekts mit der FDZ-ID "{{ id }}" löschen möchten?',
          'delete-studies-title': 'Studie löschen?',
          'delete-studies': 'Sind Sie sicher dass, Sie die Studie des Datenaufbereitungsprojekts mit der FDZ-ID "{{ id }}" löschen möchten?',
          'delete-related-publications-title': 'Alle Publikationen löschen?',
          'delete-related-publications': 'Sind Sie sicher dass, Sie die Publikationen löschen möchten?',
          'delete-instruments-title': 'Alle Instrumente löschen?',
          'delete-instruments': 'Sind Sie sicher dass, Sie alle Instrumente innerhalb des Datenaufbereitungsprojekts mit der FDZ-ID "{{ id }}" löschen möchten?'
        },
        'detail': {
          'search': 'Suche'
        },
        'buttons': {
          'refresh': 'Suche aktualisieren',
          'upload-variables': 'Variablen für ausgewähltes Datenaufbereitungsprojekt hochladen',
          'upload-surveys': 'Erhebungen für ausgewähltes Datenaufbereitungsprojekt hochladen',
          'upload-data-sets': 'Datensätze für ausgewähltes Datenaufbereitungsprojekt hochladen',
          'upload-questions': 'Fragen für ausgewähltes Datenaufbereitungsprojekt hochladen',
          'upload-studies': 'Studie für ausgewähltes Datenaufbereitungsprojekt hochladen',
          'upload-related-publications': 'Publikationen hochladen',
          'post-validate-related-publications': 'Publikationen validieren',
          'upload-instruments': 'Instrumente für ausgewähltes Datenaufbereitungsprojekt hochladen'
        },
        'input-label': {
          'all': 'Suchen Sie Studien, Variablen, Fragen, Erhebungen, Datensätze, Instrumente oder Publikationen?',
          'variables': 'Suchen Sie Variablen?',
          'questions': 'Suchen Sie Fragen?',
          'surveys': 'Suchen Sie Erhebungen?',
          'data-sets': 'Suchen Sie Datensätze?',
          'studies': 'Suchen Sie Studien?',
          'related-publications': 'Suchen Sie Publikationen?',
          'instruments': 'Suchen Sie Instrumente?'
        },
        'tabs': {
          'variables': 'Variablen',
          'variables-tooltip': 'Klicken, um Tabreiter "Variablen" zu selektieren',
          'questions': 'Fragen',
          'questions-tooltip': 'Klicken, um Tabreiter "Fragen" zu selektieren',
          'surveys': 'Erhebungen',
          'surveys-tooltip': 'Klicken, um Tabreiter "Erhebungen" zu selektieren',
          'data_sets': 'Datensätze',
          'data_sets-tooltip': 'Klicken, um Tabreiter "Datensätze" zu selektieren',
          'studies': 'Studien',
          'studies-tooltip': 'Klicken, um Tabreiter "Studien" zu selektieren',
          'all': 'Alle',
          'all-tooltip': 'Klicken, um Tabreiter "Alle" zu selektieren',
          'related_publications': 'Publikationen',
          'related_publications-tooltip': 'Klicken, um Tabreiter "Publikationen" zu selektieren',
          'instruments': 'Instrumente',
          'instruments-tooltip': 'Klicken, um Tabreiter "Instrumente" zu selektieren'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', searchTranslations);
  });
