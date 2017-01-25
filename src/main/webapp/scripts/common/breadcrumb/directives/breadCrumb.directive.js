'use strict';

angular.module('metadatamanagementApp').directive('breadCrumb',
  function() {
    var link = function(scope) {
      scope.stateNames = {
        'search': {
          'de': 'Suche',
          'en': 'Search'
        },
        'data-setDetail': {
          'de': 'Datensätze',
          'en': 'Data Sets'
        },
        'disclosure': {
          'de': 'Impressum',
          'en': 'Disclosure'
        },
        'instrumentDetail': {
          'de': 'Instrumente',
          'en': 'Instruments'
        },
        'questionDetail': {
          'de': 'Fragen',
          'en': 'Questions'
        },
        'relatedPublicationDetail': {
          'de': 'Publikationen',
          'en': 'Publications'
        },
        'studyDetail': {
          'de': 'Studien',
          'en': 'Studies'
        },
        'surveyDetail': {
          'de': 'Erhebungen',
          'en': 'Surveys'
        },
        'variableDetail': {
          'de': 'Variablen',
          'en': 'Variables'
        }
      };
    };
    return {
      restrict: 'E',
      link: link,
      templateUrl: 'scripts/common/breadcrumb/directives/breadCrumb.html.tmpl',
      scope: {
        items: '=',
        language: '='
      }
    };
  });
