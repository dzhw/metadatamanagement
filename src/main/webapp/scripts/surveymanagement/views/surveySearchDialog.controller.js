/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveySearchDialogController',
    function($mdDialog, $scope, blockUI, paramObject, SurveySearchService) {
      var ctrl = this;
      ctrl.paramObject = paramObject;
      ctrl.count = Number(ctrl.paramObject.count);
      var blockArea = blockUI.instances.get('blockRelatedSurveyContainer');
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
          if (this.numLoaded_ >= ctrl.paramObject.count) {
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
                      blockArea.start();
                      SurveySearchService[ctrl.paramObject.methodName]
                      (searchTerms[this.pageToLoad])
                      .then(angular.bind(this, function(surveys) {
                            _.pullAllBy(surveys.docs, [{'found': false}],
                            'found');
                            this.items = _.concat(this.items, surveys.docs);
                            this.pageToLoad += 1;
                          })).finally(function() {
                            blockArea.stop();
                          });
                    } else {
                      blockArea.start();
                      SurveySearchService[ctrl.paramObject.methodName](
                        ctrl.paramObject.methodParams, this.from, this.size)
                        .then(angular.bind(this, function(surveys) {
                          this.items = _.concat(this.items, surveys.hits.hits);
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
