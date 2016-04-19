/*global it:false */
/*global beforeEach:false */
/*global expect:false */
/*global spyOn:false */
/*global jasmine:false */
/*global inject:false */
/*global afterEach:false */
/*global describe:false */

/* @author: Daniel Katzberg */

'use strict';

describe('Service Tests: Base 64 ', function() {
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

  // TESTS
  it('encode a Base64 string', function() {
      spyOn(base64, 'encode').and.callThrough();
      var encodedVarLocal = base64.encode('TestVariable');
      expect('VGVzdFZhcmlhYmxl').toEqual(encodedVarLocal);
      expect(base64.encode).toHaveBeenCalled();
    });

  it('decode a Base64 string', function() {
      spyOn(base64, 'decode').and.callThrough();
      base64.decode('TestVariable');
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
