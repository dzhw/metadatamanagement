*** Settings ***
Documentation     Roll back to previous version of a concept. the system.
Metadata          Info on data   Prerequisite to have "Roll Back Concept ${BROWSER} De" concept with at least one
...               version containing the concept name "Roll Back Concept ${BROWSER} De_Rollback"
Force Tags        noslowpoke
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot

*** Test Cases ***
Check roll back concept changes
    Get Back to german home page
    Click on concept tab
    Concept Roll Back

*** Keywords ***
Concept Roll Back
   Search for  Roll Back Concept ${BROWSER} De
   Click on first search result
   Click Element Through Tooltips  xpath=//button[@ng-click="ctrl.conceptEdit()"]//md-icon[contains(., "mode_edit")]
   Click Element Through Tooltips  xpath=//button[@ng-click="ctrl.openRestorePreviousVersionDialog($event)"]//md-icon[contains(., "history")]
   Click Element Through Tooltips  xpath=//md-dialog-content//table//tr//td[contains(., "Roll Back Concept ${BROWSER} De_Rollback")]
   Click Element Through Tooltips  xpath=//button[@ng-click="ctrl.saveConcept()"]//md-icon[contains(., "save")]
   Click Element Through Tooltips  xpath=//button[@ng-click="ctrl.openRestorePreviousVersionDialog($event)"]//md-icon[contains(., "history")]
   Click Element Through Tooltips  xpath=//md-dialog-content//table//tr[@class="ng-scope"]//td[contains(., "Roll Back Concept ${BROWSER} De")]
   Click Element Through Tooltips  xpath=//button[@ng-click="ctrl.saveConcept()"]//md-icon[contains(., "save")]
