'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',

  function($translateProvider) {
    var translations = {
      //jscs:disable
      'disclosure': {
        'title': 'Impressum',
        'phone': 'Tel: ',
        'fax': 'Fax: ',
        'or': 'oder',
        'responsible-urls-text': 'Für die Inhalte der verlinkten Seiten sind ausschließlich deren Betreiber verantwortlich. Trotz sorgfältiger Prüfung übernehmen wir keine Haftung für die Inhalte externer Links. Bei Bekanntwerden von Rechtsverletzungen werden wir derartige Links umgehend entfernen.',
        'supervisory': 'Vorsitzender des Aufsichtsrats: Ministerialdirigent ',
        'scientific-director-female': 'Wissenschaftliche Geschäftsführerin: ',
        'scientific-director-male': 'Wissenschaftlicher Geschäftsführer: ',
        'executive-director-female': 'Administrative Geschäftsführerin: ',
        'executive-director-male': 'Administrativer Geschäftsführer: ',
        'register-court': 'Registergericht: Amtsgericht ',
        'identification-number': 'Umsatzsteuer-Identifikationsnummer: ',
        'responsiblepress-law': 'Verantwortlich für den redaktionellen Inhalt: ',
        'copyright': 'Copyright: ',
        'copyright-general': 'Für die Internet-Seiten des Deutschen Zentrums für Hochschul- und Wissenschaftsforschung unter <a id="externalLink" href="/de/">https://metadata.fdz.dzhw.eu/</a> liegen Copyright und alle weiteren Rechte beim DZHW - sofern nichts anderes an der entsprechenden Stelle angegeben ist. Die Weiterverbreitung, auch in Auszügen, für pädagogische, wissenschaftliche oder private Zwecke ist unter Angabe der Quelle gestattet. Eine Verwendung im gewerblichen Bereich bedarf der Genehmigung durch das DZHW.',
        'company': 'Deutsches Zentrum für Hochschul- und Wissenschaftsforschung GmbH',
        'copyright-employees': 'Für den Fall, dass auf unserer Website unzutreffende Informationen veröffentlicht oder in Programmen oder Datenbanken Fehler enthalten sein sollten, kommt eine Haftung nur bei grober Fahrlässigkeit der DZHW GmbH oder ihrer Mitarbeiter:innen in Betracht.',
        'copyright-editorial': 'Die Redaktion übernimmt keine Haftung für unverlangt eingesandte Manuskripte, Fotos und Illustrationen.'
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  }]);
