'use strict';

describe('Factory Tests ', function () {
  var Language, $q, $translate, tmhDynamicLocale, $location, deferred, $scope;
    describe('Language', function() {
        beforeEach(inject(function($injector) {
            Language = $injector.get('Language');
            $q = $injector.get('$q');
            $translate = $injector.get('$translate');
            $location = $injector.get('$location');
            deferred = $q.defer();
            $scope = $injector.get('$rootScope').$new();
            tmhDynamicLocale = {
              set: function(){
                return {
                   then: function(callback){
                     return callback();
                   }
                };
              }
            };
            spyOn(tmhDynamicLocale, 'set').and.callThrough();
        }));
        it('Language should be defined', function() {
          expect(Language).toBeDefined();
        });
        it('Language should be defined', function() {
          var ret = Language.getAll();
          expect(ret).toBeDefined();
        });
        it('Language should be defined', function() {
          var ret = Language.getAll();
          expect(ret).toBeDefined();
        });
      });
  });
