'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var startTranslation = {
      //jscs:disable
      'start': {
        'data-search': 'Datenpakete aus der Hochschul- und Wissenschaftsforschung finden',
        'image-title': 'Daten aus Studienreihen',
        'recommendation': 'Empfohlenes Datenpaket',
        'fdz-text': 'Die Datenpaketsuche des FDZ.DZHW ermöglicht es Ihnen die Metadaten der bei uns hinterlegten Datenpakete zu durchsuchen. So finden unsere Nutzer*Innen die für sie relevanten Informationen und können sich informiert für ein (oder mehrere) Datenpaket(e) entscheiden und es direkt bestellen. Es gibt hierbei je nach Anonymisierungsgrad und Verwendungszweck verschiedene Zugangswege: Scientific Usefiles (SUF) für die Forschung und Campus Usefiles (CUF) für die Lehre. Bei SUFs unterscheiden wir zwischen Download-SUF (starke Anonymisierung), Remote-Desktop-SUF (mittlere Anonymisierung) und Onsite-SUF (sie verarbeiten die Daten lokal bei uns, haben dafür den höchsten Detaillierungsgrad).',
        'show-all': 'Alle Datenpakete anzeigen'
      }
      //jscs:enable
    };
    $translateProvider.translations('de', startTranslation);
  });
