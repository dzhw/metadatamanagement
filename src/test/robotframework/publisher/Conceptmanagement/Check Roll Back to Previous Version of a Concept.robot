*** Settings ***
Documentation     Roll back to previous version of a concept. Prerequisite to have "Roll Back Concept ${BROWSER} De" concept in the system.
Force Tags        noslowpoke
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot

*** Test Cases ***
Check already used concept can not be deleted
    Get Back to german home page
    Click on concept tab
    Concept Roll Back

*** Keywords ***
Concept Roll Back
   Search for  Roll Back Concept ${BROWSER} De
   Run Keyword if  '${BROWSER}' == 'chrome'  Sleep  3s  #chrome is too fast
   Click on the first search result
   Click Element Through Tooltips  xpath=//button[@ng-click="ctrl.conceptEdit()"]//md-icon[contains(., "mode_edit")]
   Click Element Through Tooltips  xpath=//button[@ng-click="ctrl.openRestorePreviousVersionDialog($event)"]//md-icon[contains(., "undo")]
   Click Element Through Tooltips  xpath=//md-dialog-content//table//tr//td[contains(., "Roll Back Concept ${BROWSER} De_Rollback")]
   Click Element Through Tooltips  xpath=//button[@ng-click="ctrl.saveConcept()"]//md-icon[contains(., "save")]
   Click Element Through Tooltips  xpath=//button[@ng-click="ctrl.openRestorePreviousVersionDialog($event)"]//md-icon[contains(., "undo")]
   Click Element Through Tooltips  xpath=//md-dialog-content//table//tr[@class="ng-scope"]//td[contains(., "Roll Back Concept ${BROWSER} De")]
   Click Element Through Tooltips  xpath=//button[@ng-click="ctrl.saveConcept()"]//md-icon[contains(., "save")]

Click on the first search result
    Click Element Through Tooltips   xpath=//a[@class='fdz-search-result'][1]
