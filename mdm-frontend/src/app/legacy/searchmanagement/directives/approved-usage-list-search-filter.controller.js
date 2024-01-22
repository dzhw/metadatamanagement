/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ApprovedUsageListSearchFilterController', [
    '$scope',
    function($scope) {
      
      $scope.approvedUsageListItems = [
        "SCIENTIFIC_USE",
        "TEACHING_PURPOSES",
        "NON_COMMERCIAL_USE",
        "COMMERCIAL_USE"
      ];

      $scope.currentApprovedUsageList = {
        "SCIENTIFIC_USE": false,
        "TEACHING_PURPOSES": false,
        "NON_COMMERCIAL_USE": false,
        "COMMERCIAL_USE": false,
      }

      // Indicator if an "and"-logic should be used to filter approved usages
      $scope.useAndLogicApprovedUsage = false;

      // This is used in case a user provides an unsupported usage via url-param
      $scope.errorMsg = "";

      /**
       * Init method that reads the $scope.currentSearchParams.filter (from url-params)
       * and sets local variables accordingly. If the search page is called with an url-param
       * (e.g. "?approved-usage-list=SCIENTIFIC_USE"), $scope.currentSearchParams.filter is not empty
       */
      var init = function() {
        if ($scope.currentSearchParams.filter &&
            $scope.currentSearchParams.filter["approved-usage-list"]) {
          var errors = "";
          var usages = $scope.currentSearchParams.filter["approved-usage-list"].split("||")          
          for (const usage of usages) {
            if ($scope.currentApprovedUsageList.hasOwnProperty(usage)){
              $scope.currentApprovedUsageList[usage] = true;
            } else {
              errors += usage + " ";
            }
          }
          $scope.useAndLogicApprovedUsage = $scope.currentSearchParams.filter["useAndLogicApprovedUsage"];
          $scope.errorMsg = errors;
        }
      };

      /**
       * Method that is been called when a checkbox, or the "useAndLogic"-switch is clicked.
       */
      $scope.onApprovedUsageListChanged = function() {
        $scope.errorMsg = "";
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (noApprovedUsageListSelected()) {
          delete $scope.currentSearchParams.filter["approved-usage-list"];
        } else {
          var usages = "";
          for (const usage in $scope.currentApprovedUsageList) {
            if ($scope.currentApprovedUsageList[usage]) {
              usages += usage + "||"
            }
          }
          usages = usages.slice(0, -2);
          $scope.currentSearchParams.filter["approved-usage-list"] = usages

          // provide info about and/or-logic as "useAndLogicApprovedUsage" filter
          $scope.currentSearchParams.filter["useAndLogicApprovedUsage"] = $scope.useAndLogicApprovedUsage;
        }        
        $scope.approvedUsageListChangedCallback();
      }

      /**
       * This method gets an approved usage id (e.g. 'SCIENTIFIC_USE')
       * and returns the corresponding translation path.
       * 
       * @param {*} id id of the approved usage
       * @returns path to the translation of the approved usage
       */
      $scope.getTranslationPathFromApprovedUsageId = function(id) {
        switch(id) {
          case 'SCIENTIFIC_USE':
            return "data-package-management.common.approvedUsage.scientificUse"
          case 'TEACHING_PURPOSES':
            return "data-package-management.common.approvedUsage.teachingPurposes"
          case 'NON_COMMERCIAL_USE':
            return "data-package-management.common.approvedUsage.nonCommercialUse"
          case 'COMMERCIAL_USE':
            return "data-package-management.common.approvedUsage.commercialUse"
        }
      }

      /**
       * Method to check if at least one approved usage list element is selected or not.
       * Returns true if no approved usage list element is selected.
       * It at least one is selected, this method returns false.
       */
      noApprovedUsageListSelected = function() {
        for (const usage in $scope.currentApprovedUsageList) {
          if ($scope.currentApprovedUsageList[usage]) {
            return false;
          }
        }
        return true;
      }

      $scope.$watch('currentSearchParams.filter.approved-usage-list', function() {
        init();
      });
    }
  ]);
