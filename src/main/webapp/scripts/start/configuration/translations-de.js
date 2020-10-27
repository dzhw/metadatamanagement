'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var startTranslation = {
      //jscs:disable
      'start': {
        'data-search': 'Forschungsdaten aus der Hochschul- und Wissenschaftsforschung finden',
        'image-title': 'Ausgewählte Daten',
        'pinned-data-package-title': 'Datenpaket im Fokus',
        'fdz-text': '<p>Das fdz.DZHW stellt Datenpakete der Hochschul- und Wissenschaftsforschung zur Verfügung. Mit der Datensuche können Sie die Metadaten der beim fdz.DZHW hinterlegten Datenpakete schnell und einfach durchsuchen. So finden Sie alle Informationen, die Sie brauchen, und können die entsprechenden Datenpakete direkt bestellen.</p>',
        'show-all': 'Alle Datenpakete anzeigen'
      }
      //jscs:enable
    };
    $translateProvider.translations('de', startTranslation);
  });
