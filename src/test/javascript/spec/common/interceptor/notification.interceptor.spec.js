'use strict';

describe('Factory Tests ', function () {
    var notificationInterceptor, AlertService, $scope;
    describe('notificationInterceptor', function() {
        beforeEach(inject(function($injector) {
              notificationInterceptor = $injector.get('notificationInterceptor');
            $scope = $injector.get('$rootScope').$new();
            AlertService = {
              success: function(){
                return {
                   then: function(callback){
                     return callback();
                   }
                };
              }};
            spyOn(AlertService,'success').and.callThrough();
        }));
        it('should call $q.reject', function() {
          var response = {
            'headers': function(){
              return 'headers';
            }
          };
          notificationInterceptor.response(response);
          expect(notificationInterceptor).toBeDefined();
        });
      });
    });
