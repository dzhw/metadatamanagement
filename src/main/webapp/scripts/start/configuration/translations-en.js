'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var startTranslation = {
      //jscs:disable
      'start': {
        'data-search': 'Find Higher Education and Science Research Data Packages',
        'image-title': 'Selected Data',
        'fdz-text': '<p>With the data search you can quickly and easily search the metadata of the data packages stored at the fdz.DZHW. This way you will find all the information you need and can order the corresponding data packages directly.</p>',
        'show-all': 'Show all Data Packages'
      }
      //jscs:enable
    };
    $translateProvider.translations('en', startTranslation);
  });
