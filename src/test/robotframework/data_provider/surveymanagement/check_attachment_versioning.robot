*** Settings ***
Documentation     Test editing survey page and versioning #Prerequisite to have fileuploadproject
Force Tags        chromeonly
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot

*** Test Cases ***
Editing survey attachement and check versioning
    Select project by name    fileuploadproject
    Click on surveys tab
    Click Survey Edit Button
    Click Edit Attachment Button
    Input Text    name=title    Test1337
    Click Attachment Save Button
    Click Edit Attachment Button
    Click Attachment Restore Dialogue
    Page Should Contain    vor ein paar Sekunden
    Revise to second latest version
    Click Attachment Save Button
    [Teardown]    Get back to german home page

*** Keywords ***
Click Edit Attachment Button
    Click Element Through Tooltips    xpath=//button[md-icon[text()='mode_edit']]

Click Attachment Save Button
    Click Element Through Tooltips    xpath=//form//button[md-icon[text()='save']]

Revise to second latest version
    Click Element Through Tooltips    xpath=//md-dialog//table//tr[2]

Click Attachment Restore Dialogue
    Click Element Through Tooltips    xpath=//form//button[md-icon[text()='history']]

Close Attachment Dialog
    Click Element Through Tooltips    xpath=//button[md-icon[text()='clear']]
