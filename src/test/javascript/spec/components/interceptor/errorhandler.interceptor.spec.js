'use strict';

describe('Factory Tests ', function () {
  var errorHandlerInterceptor, $q, $scope;
    describe('errorHandlerInterceptor', function() {
        beforeEach(inject(function($injector) {
            errorHandlerInterceptor = $injector.get('errorHandlerInterceptor');
            $q = $injector.get('$q');
            $scope = $injector.get('$rootScope').$new();
            spyOn($q,'reject').and.callThrough();
        }));
        it('should call $q.reject', function() {
          var response = {
            'data':{
              'path':'/test'
            },
            'status':401
          }
        errorHandlerInterceptor.responseError(response);
        expect($q.reject).toHaveBeenCalled();
        });
      });
    });
