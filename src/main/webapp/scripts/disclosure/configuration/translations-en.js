'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'disclosure': {
        'title': 'Legal Disclosure',
        'phone': 'Phone: ',
        'fax': 'Fax: ',
        'or': 'or',
        'responsible-urls-text': 'All links are proofed regularly. Responsibility for the contents of links which find themselves outside of these parameters is explicitly denied.',
        'supervisory': 'Head of the supervisory Board: Ministerial Conductor ',
        'scientific-director-female': 'Scientific Director: ',
        'scientific-director-male': 'Scientific Director: ',
        'executive-director-female': 'Executive Director: ',
        'executive-director-male': 'Executive Director: ',
        'register-court': 'Register Court: Municipal Court',
        'identification-number': 'VAT-Identification Number: ',
        'responsiblepress-law': 'Person responsible with regard to the press law for the editorial content: ',
        'copyright': 'Copyright: ',
        'copyright-general': 'Unless otherwise stated, copyright and all other rights for the internet pages of the Deutsches Zentrum für Hochschul- und Wissenschaftsforschung under <a id="externalLink" href="#!/en/">metadatamanagement.cfapps.io</a> lie with the DZHW. Further use and passing on of content, even in excerpts, for pedagogical, scientific or private use is allowed within the scope of the proper citation of the source. Permission must first be obtained for the use of copyright material in commercial business.',
        'copyright-employees': 'For the case that inapplicable information is published on our website or that our programmes or data bases contain errors, liability will only be accepted in instances of gross negligence on the part of the DZHW GmbH or its employees.',
        'copyright-editorial': 'The editorial office claims no liability for manuscripts, photos and illustrations which are sent to us without solicitation.'
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
