'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var startTranslation = {
      //jscs:disable
      'start': {
        'data-search': 'Forschungsdaten aus der Hochschul- und Wissenschaftsforschung finden',
        'image-title': 'Daten aus Studienreihen',
        'recommendation': 'Empfohlenes Datenpaket',
        'fdz-text': '<p>Das fdz.DZHW stellt Datenpakete der Hochschul- und Wissenschaftsforschung zur Verfügung. Mit der Datenpaketsuche können Sie die Metadaten der beim fdz.DZHW hinterlegten Datenpakete schnell und einfach durchsuchen. So finden Sie alle Informationen, die Sie brauchen, und können die entsprechenden Datenpakete direkt bestellen.</p><p style="margin-bottom: 0px;">Je nach Anwendungszweck und Detaillierungsgrad stellen wir folgende Zugangswege zur Verfügung: <em>Scientific Usefiles</em> (SUF) für die Forschung und <em>Campus Usefiles</em> (CUF) für die Lehre. Bei SUFs unterscheiden wir zwischen <em>Download</em>-SUF (starke Anonymisierung), <em>Remote-Desktop</em>-SUF (mittlerer Anonymisierungsgrad) und <em>Onsite</em>-SUF (Sie analysieren die Daten lokal bei uns, haben dafür den höchsten Detailgrad). Informationen über die zur Verfügung stehenden Zugangswege finden Sie ebenfalls in der Datenpaketsuche.</p>',
        'show-all': 'Alle Datenpakete anzeigen'
      }
      //jscs:enable
    };
    $translateProvider.translations('de', startTranslation);
  });
