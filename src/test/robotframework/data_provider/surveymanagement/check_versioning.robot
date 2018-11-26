*** Settings ***
Documentation     Test editing survey page and versioning
Force Tags        smoketest    chromeonly
Resource          ../../resources/login_resource.robot
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot

*** Test Cases ***
Editing survey and check versioning
    Login as dataprovider
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
    [Teardown]    Get back to home page and logout

*** Keywords ***
Get back to home page and logout
    Get back to german home page
    ${present}=    Run Keyword And Return Status    Page Should Contain    Sie haben ungespeicherte Änderungen.
    Run Keyword If    ${present} == 'True'    Click Element Through Tooltips    xpath=//button[contains(.,'Ja')]
    Click Element Through Tooltips    xpath=//button[@id='logout']

Click Submit Button
    Click Element Through Tooltips    xpath=//button[@type='submit']

Click Restore Dialogue
    Click Element Through Tooltips    xpath=//button[md-icon[text()='undo']]

Cancel Restore Dialoge
    Click Element Through Tooltips    xpath=//button[span[text()='Abbrechen']]
