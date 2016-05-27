'use strict';

function mockApis() {
  inject(function($httpBackend) {
    $httpBackend.whenGET(/api\/account.*/).respond({});
    $httpBackend.whenGET(/i18n\/de\/.+\.json/).respond({});
    $httpBackend.whenGET('scripts/home/home.html.tmpl').respond({});
  });
}
