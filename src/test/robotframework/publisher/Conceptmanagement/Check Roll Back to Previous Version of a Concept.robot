*** Settings ***
Documentation     Roll back to previous version of a concept. Prerequisite to have "Bereits referenziertes Konzept mit Instrument" concept in the system.
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot

*** Test Cases ***
Check already used concept can not be deleted
    Get Back to german home page
    Click on concept tab
    Click on Concept Edit Button
    Click on Restore Button
    Click on the previous concept version
    Click on Save Button
    Click on Restore Button
    Get back to the actual concept verison
    Click on Save Button

*** Keywords ***
Click on Concept Edit Button
   Click Element Through Tooltips  xpath=//concept-search-result//div[@data-has-any-authority="ROLE_PUBLISHER"]//md-icon[contains(., "mode_edit")]

Click on Restore Button
   Click Element Through Tooltips  xpath=//button[@ng-click="ctrl.openRestorePreviousVersionDialog($event)"]//md-icon[contains(., "undo")]

Click on the previous concept version
   Click Element Through Tooltips  xpath=//md-dialog-content//table//tr//td[contains(., "Bereits referenziertes Konzept mit Instrument_rollback")]

Get back to the actual concept verison
   Click Element Through Tooltips  xpath=//md-dialog-content//table//tr[@class="ng-scope"]//td[contains(., "Bereits referenziertes Konzept mit Instrument")]

Click on Save Button
   Click Element Through Tooltips  xpath=//button[@ng-click="ctrl.saveConcept()"]//md-icon[contains(., "save")]
