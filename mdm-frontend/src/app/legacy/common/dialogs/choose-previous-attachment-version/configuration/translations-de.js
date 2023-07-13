'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',

    function($translateProvider) {
      var translations = {
        //jscs:disable
        'choose-previous-attachment-version': {
          'title': 'Ältere Version der Metadaten zu Datei "{{ filename }}" wiederherstellen',
          'text': 'Wählen Sie eine ältere Version der Metadaten zu Datei "{{ filename }}" aus, die wiederhergestellt werden soll:',
          'attachment-description': 'Beschreibung (auf Deutsch)',
          'lastModified': 'Geändert',
          'lastModifiedBy': 'von',
          'cancel-tooltip': 'Klicken, um, ohne eine ältere Version der Metadaten auszuwählen, zurückzukehren.',
          'current-version-tooltip': 'Dies ist die aktuelle Version!',
          'next-page-tooltip': 'Klicken, um ältere Versionen anzuzeigen.',
          'previous-page-tooltip': 'Klicken, um aktuellere Versionen anzuzeigen.',
          'attachment-deleted': 'Metadaten wurden gelöscht!',
          'no-versions-found': 'Es wurden keine älteren Versionen der Metadaten gefunden.'
        }
        //jscs:enable
      };
      $translateProvider.translations('de', translations);
    }]);
