'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var startTranslation = {
      //jscs:disable
      'start': {
        'data-search': 'Forschungsdaten aus der Hochschul- und Wissenschaftsforschung finden',
        'pinned-data-package-title': 'Datenpaket im Fokus',
        'fdz-text': 'Das FDZ-DZHW stellt Datenpakete der Hochschul- und Wissenschaftsforschung zur Verfügung. Mit der Datensuche können Sie die Metadaten der beim FDZ-DZHW hinterlegten Datenpakete schnell und einfach durchsuchen. So finden Sie alle Informationen, die Sie brauchen, und können die entsprechenden Datenpakete direkt bestellen.',
        'show-all': 'Alle Datenpakete anzeigen (SUF, CUF)',
        'show-all-analysis-packages': 'Alle Analysepakete anzeigen (Skripte)'
      }
      //jscs:enable
    };
    $translateProvider.translations('de', startTranslation);
  });
