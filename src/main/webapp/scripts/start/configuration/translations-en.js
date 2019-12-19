'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var startTranslation = {
      //jscs:disable
      'start': {
        'data-search': 'Find Higher Education and Science Research Data Packages',
        'image-title': 'Data from Study Serieses',
        'recommendation': 'Recommended Data Package',
        'fdz-text': '<p>With the data package search you can quickly and easily search the metadata of the data packages stored at the fdz.DZHW. This way you will find all the information you need and can order the corresponding data packages directly.</p><p style="margin-bottom: 0px;">Depending on the application purpose and level of detail, we provide the following access ways: <em>Scientific Usefiles</em> (SUF) for research and <em>Campus Usefiles</em> (CUF) for teaching. For SUFs we distinguish between <em>Download-SUF</em> (strong anonymisation), <em>Remote-Desktop-SUF</em> (medium anonymisation) and <em>Onsite-SUF</em> (you analyse the data locally at our site, but have the highest level of detail).</p>',
        'show-all': 'Show all data packages'
      }
      //jscs:enable
    };
    $translateProvider.translations('en', startTranslation);
  });
