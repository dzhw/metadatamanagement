/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('InstrumentSearchDialogController',
    function($mdDialog, $scope, blockUI, paramObject, InstrumentSearchService) {
      var ctrl = this;
      ctrl.paramObject = paramObject;
      var blockArea = blockUI.instances.get('blockRelatedInstrumentContainer');
      ctrl.infiniteItems = {
        numLoaded_: 0,
        toLoad_: 0,
        pageToLoad: 0,
        from: 0,
        size: 5,
        items: [],
        getItemAtIndex: function(index) {
                  if (index > this.numLoaded_) {
                    this.fetchMoreItems_(index);
                    return null;
                  }
                  return this.items[index];
                },
        getLength: function() {
          if (this.numLoaded_ >= ctrl.count) {
            return this.items.length;
          }
          return this.items.length + this.size;
        },
        fetchMoreItems_: function(index) {
                  if (this.toLoad_ < index) {
                    this.toLoad_ += this.size;
                    if (_.isArray(ctrl.paramObject.methodParams)) {
                      var searchTerms = _.chunk(ctrl.paramObject
                        .methodParams, this.size);
                      ctrl.count = ctrl.paramObject.methodParams.length;
                      blockArea.start();
                      InstrumentSearchService[ctrl.paramObject.methodName]
                      (searchTerms[this.pageToLoad])
                      .then(angular.bind(this, function(instruments) {
                            _.pullAllBy(instruments.docs, [{'found': false}],
                            'found');
                            ctrl.count = instruments.docs.length;
                            this.items = _.concat(this.items, instruments.docs);
                            this.numLoaded_ = this.items.length;
                            this.pageToLoad += 1;
                          })).finally(function() {
                            blockArea.stop();
                          });
                    } else {
                      blockArea.start();
                      InstrumentSearchService[ctrl.paramObject.methodName](
                        ctrl.paramObject.methodParams, this.from, this.size,
                        ctrl.paramObject.instrumentId)
                        .then(angular.bind(this, function(instruments) {
                          ctrl.count = instruments.hits.total;
                          this.items = _.concat(this.items,
                            instruments.hits.hits);
                          this.numLoaded_ = this.items.length;
                          this.from += this.size;
                        })).finally(function() {
                          blockArea.stop();
                        });
                    }
                  }
                }
      };
      ctrl.closeDialog = $mdDialog.hide;
      $scope.$on('$stateChangeStart', function() {
        ctrl.closeDialog();
      });
    });
