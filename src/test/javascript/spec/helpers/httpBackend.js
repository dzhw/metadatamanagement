'use strict';

function mockApis() {
  inject(function($httpBackend) {
    $httpBackend.whenGET(/api\/account.*/).respond({});
    $httpBackend.whenGET(/i18n\/de\/.+\.json/).respond({});
    $httpBackend.whenGET('scripts/common/navbar/views/navbar.html.tmpl').
    respond({});
    $httpBackend.whenGET('scripts/common/main/main.html.tmpl').respond({});
  });
}
