'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var startTranslation = {
      //jscs:disable
      'start': {
        'data-search': 'Datensuche',
        'image-title': 'Schnellauswahl',
        'recommendation': 'Empfohlenes Datenpaket',
        'fdz-text': 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et',
        'show-all': 'Alle Datenpakete anzeigen'
      }
      //jscs:enable
    };
    $translateProvider.translations('de', startTranslation);
  });

