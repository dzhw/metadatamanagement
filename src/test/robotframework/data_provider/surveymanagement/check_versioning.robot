*** Settings ***
Documentation     Test editing survey page and versioning
Metadata          Info on data    This test suite uses the project with the name "fileuploadproject" which needs to be available in the testing environment
Force Tags        chromeonly
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot

*** Test Cases ***
Editing survey and check versioning
    Select project by name    fileuploadproject
    Click on surveys tab
    Click Survey Edit Button
    Input Text    name=titleDe    Test1337
    Click Submit Button
    Click Restore Dialogue
    Page Should Contain    vor ein paar Sekunden
    Cancel Restore Dialoge
    Input Text    name=titleDe    Test
    Click Submit Button
    [Teardown]    Get back to german home page

*** Keywords ***
Click Submit Button
    Click Element Through Tooltips    xpath=//button[@type='submit']

Click Restore Dialogue
    Click Element Through Tooltips    xpath=//button[md-icon[text()='history']]

Cancel Restore Dialoge
    Click Element Through Tooltips    xpath=//button[span[text()='Abbrechen']]
