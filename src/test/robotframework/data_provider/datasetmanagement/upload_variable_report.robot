*** Settings ***
Documentation     Upload a variable report template to generate variable report
Force Tags        chromeonly
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot

*** Variables ***
${TOAST_MSSG_COMP}  Tex Dokument erfolgreich erzeugt

*** Test Cases ***
Upload file in Dataset editor
    Select project by name    gra2005
    Click on data set tab
    Click on search result by id    dat-gra2005-ds2$
    Click to generate variable report
    Upload Varaible Report Template
    Close The Toast Message for Complete Template   ${TOAST_MSSG_COMP}
    Sleep    5s   # Sleep is needed to wait for uploading the variable report
    Get back to home page and deselect project

*** Keywords ***
Get back to home page and deselect project
    Get back to german home page
    Click Element Through Tooltips    xpath=//md-sidenav//project-navbar-module//button[@aria-label='Clear Input']

Click to generate variable report
    Click Element Through Tooltips    xpath=//ui-view//button/md-icon[text()='note_add']

Upload Varaible Report Template
    Press Key    xpath=//input[@type='file' and @ngf-select='ctrl.uploadTexTemplate($files)'][1]   ${CURDIR}/template

Close The Toast Message for Complete Template
    [Arguments]  ${TOAST_MSSG}
    Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Tex Dokument erfolgreich erzeugt")]
    Element Should Contain  xpath=//md-toast//span[contains(.,"Tex Dokument erfolgreich erzeugt")]  ${TOAST_MSSG}  # Assert the validation of toast message
    Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]
