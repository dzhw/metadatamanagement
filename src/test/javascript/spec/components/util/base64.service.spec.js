'use strict';

describe('Services Tests ', function() {
  describe('Base64', function() {
    var mock;
    var base64;
    var storageService;

    //Before all Tests
    beforeEach(inject(function(Base64, StorageService) {
      mock = {alert: jasmine.createSpy()};
      base64 = Base64;
      storageService = StorageService;
    }));

    //After all tests
    afterEach(function() {});

    it('encode a Base64 string', function() {
      spyOn(base64, 'encode').and.callThrough();
      var encodedVarLocal = base64.encode('TestVariable');
      expect('VGVzdFZhcmlhYmxl').toEqual(encodedVarLocal);
      expect(base64.encode).toHaveBeenCalled();
    });

    it('decode a Base64 string', function() {
      spyOn(base64, 'decode').and.callThrough();
      var decodedVarLocal = base64.decode('TestVariable');
      expect(base64.decode).toHaveBeenCalled();
    });

    it('check storage service methods', function() {
      spyOn(storageService, 'get').and.callThrough();
      spyOn(storageService, 'save').and.callThrough();
      spyOn(storageService, 'remove').and.callThrough();
      spyOn(storageService, 'clearAll').and.callThrough();

      storageService.get('Example');
      storageService.save('Example', 'data');
      storageService.remove('Example');
      storageService.clearAll();

      expect(storageService.get).toHaveBeenCalled();
      expect(storageService.save).toHaveBeenCalled();
      expect(storageService.remove).toHaveBeenCalled();
      expect(storageService.clearAll).toHaveBeenCalled();
    });
  });
});
