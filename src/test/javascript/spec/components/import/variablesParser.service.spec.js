/* global describe */
/* global beforeEach */
/* global inject */
/* global it */
/* global spyOn */

'use strict';

describe('VariablesParser', function() {
  var VariablesParser;
  var XLSX;
  var JSON;

  beforeEach(module(function($provide) {
      $provide.value('XLSX', {
        read: function() {
          return {
            SheetNames: ['TestScheet'],
            Sheets: {
            }
          };
        },
        utils: {
          // jscs:disable
          sheet_to_json: function() {
            // jscs:enable
            return [{id: 'TestId'}];
          }
        }
      });
      $provide.value('JSON', {
        parse: function() {
          return {
            SheetNames: ['TestScheet'],
            Sheets: {
            }
          };
        }
      });
    }));
  beforeEach(inject(function($injector) {
    VariablesParser = $injector.get('VariablesParser');
    XLSX = $injector.get('XLSX');
    JSON = $injector.get('JSON');
    spyOn(JSON,'parse').and.callThrough();
    spyOn(XLSX,'read').and.callThrough();
    spyOn(XLSX.utils,'sheet_to_json').and.callThrough();
  }));
  it('should return DataSet resource ', function() {
    try {
      var data = {
        files: {
          xlsx: {
            name: 'test.xlsx',
            asBinary: function() {
              return 454545;
            }
          }
        }
      };
      VariablesParser.getVariables(data,'rdc');
    }catch (e) {
    }
  });
  it('should return DataSet resource ', function() {
    try {
      var data = {
        files: {
          json: {
            name: 'values/test',
            asBinary: function() {
              return 454545;
            }
          }
        }
      };
      VariablesParser.getVariables(data,'rdc');
    }catch (e) {
    }
  });
});
