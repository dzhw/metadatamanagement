*** Settings ***
Documentation     Prerequisite to have this "Referenced Concept ${BROWSER} De" in the system. This test case check this concept referenced with an instrument can not be deleted.
Force Tags        noslowpoke
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/project_management_resource.robot

*** Test Cases ***
Check already used concept can not be deleted
    Get Back to german home page
    Click on concept tab
    Click on Concept Delete Button
    Discard Changes Yes
    Assert concept can not be deleted toast message
    Close The Toast Message

*** Keywords ***
Click on Concept Delete Button
   Search for  Referenced Concept ${BROWSER} De
   Click Element Through Tooltips  xpath=(//concept-search-result//md-card-actions//button[normalize-space()="Löschen"])[1]

Assert concept can not be deleted toast message
   Page Should Contain  Das Konzept kann nicht gelöscht werden

Close The Toast Message
   Click Element Through Tooltips  xpath=//button[@ng-click="closeToast()"]//following::md-icon[contains(.,"close")]
