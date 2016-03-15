'use strict';

function mockApis() {
  inject(function($httpBackend) {
    $httpBackend.whenGET(/api\/account.*/).respond({});
    $httpBackend.whenGET(/i18n\/de\/.+\.json/).respond({});
    $httpBackend.whenGET('scripts/components/navbar/navbar.html.tmpl').
    respond({});
    $httpBackend.whenGET('scripts/components/navbar/navbar.html.tmpl').
    respond({});
    $httpBackend.whenGET('scripts/app/main/main.html.tmpl').respond({});
  });
}
