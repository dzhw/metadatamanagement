'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var startTranslation = {
      //jscs:disable
      'start': {
        'data-search': 'Find Higher Education and Science Research Data Packages',
        'pinned-data-package-title': 'Featured Data Package',
        'fdz-text': 'With the data search you can quickly and easily search the metadata of the data packages stored at the FDZ-DZHW. This way you will find all the information you need and can order the corresponding data packages directly.',
        'show-all': 'Show all Data Packages (SUF, CUF)',
        'show-all-analysis-packages': 'Show all Analysis Packages (Scripts)'
      }
      //jscs:enable
    };
    $translateProvider.translations('en', startTranslation);
  });
