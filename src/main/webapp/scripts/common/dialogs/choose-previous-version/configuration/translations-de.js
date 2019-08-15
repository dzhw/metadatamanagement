'use strict';

angular.module('metadatamanagementApp').config(
    function($translateProvider) {
      var translations = {
        //jscs:disable
        'choose-previous-version': {
          'cancel-tooltip': 'Klicken, um, ohne eine ältere Version auszuwählen, zurückzukehren.',
          'current-version-tooltip': 'Dies ist die aktuelle Version!',
          'deleted': 'Gelöscht',
          'lastModified': 'Geändert',
          'lastModifiedBy': 'von',
          'next-page-tooltip': 'Klicken, um ältere Versionen anzuzeigen.',
          'no-versions-found': 'Es wurden keine älteren Versionen von {{ id }} gefunden.',
          'previous-page-tooltip': 'Klicken, um aktuellere Versionen anzuzeigen.',
          'text': 'Wählen Sie eine ältere Version aus, die wiederhergestellt werden soll:',
          'title': 'Titel',
          'description': 'Beschreibung'
        }
        //jscs:enable
      };
      $translateProvider.translations('de', translations);
    });
