'use strict';

function mockApis() {
  inject(function($httpBackend) {
    $httpBackend.whenGET(/api\/account.*/).respond({});
    $httpBackend.whenGET('scripts/common/navbar/views/navbar.html.tmpl').
    respond({});
    $httpBackend.whenGET('scripts/common/toolbar/views/toolbar.html.tmpl').
    respond({});
    $httpBackend.whenGET('scripts/searchmanagement/views/search.html.tmpl')
      .respond({});
  });
}
