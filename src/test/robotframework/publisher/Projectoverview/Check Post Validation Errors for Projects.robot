*** Settings ***
Documentation     Automatically test post validation errors and messages of a selected project. Prerequisite to have validation errors for the selected project.
Metadata          Info on data    This test suite uses the project with the name "fileuploadproject" which needs to be available in the testing environment
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Check Post Validation Errors and Assertion of Messages for a Project
   Select project by name  fileuploadproject
   Click on post validate button
   Click on protocol to check the error messages
   Assert the errors and messages for the project
   Close the protocol dialog
   Get back to home page and deselect project  # to sync with the next the test case

*** Keywords ***
Click on post validate button
   Click Element Through Tooltips   xpath=//button[@ng-click="ctrl.postValidateProject()"]//md-icon[contains(., "playlist_add_check")]
   Sleep  2s   #explicit wait for validation

Click on protocol to check the error messages
   Click Element Through Tooltips   xpath=//button[@ng-click="showLog()"]//span[contains(., "Protokoll")]

Assert the errors and messages for the project
    Page Should Contain Element  xpath=//md-dialog[@class="_md md-transition-in"]//span//span[contains(., "Fehler")]
    Page Should Contain Element  xpath=//md-dialog[@class="_md md-transition-in"]//li[contains(., "Es gibt noch Metadaten die nicht von den Publishern")]
    Page Should Contain Element  xpath=//md-dialog[@class="_md md-transition-in"]//li[contains(.,"Fehlern beendet.")]

Close the protocol dialog
    Click Element Through Tooltips  xpath=//button[@ng-click="closeDialog()"][contains(., "Schließen")]

Get back to home page and deselect project
    Get back to german home page
    Click Element Through Tooltips    xpath=//md-sidenav//project-navbar-module//button[@aria-label='Clear Input']
    Get back to german home page  #we need it again to focus on projectoverview button
