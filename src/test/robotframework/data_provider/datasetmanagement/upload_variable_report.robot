*** Settings ***
Documentation     Upload a variable report template to generate variable report. Folder upload does not work in this case.
Library   OperatingSystem
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot

*** Variables ***
${TOAST_MSSG_COMP}  Tex Dokument erfolgreich erzeugt
${TOAST_MSSG_SINGLE}   Erzeugen von Tex Dokument Abgebrochen!

*** Test Cases ***
Upload single file in Dataset editor
    [Tags]  firefoxonly
    Select project by name    gra2005
    Click on data set tab
    Click on search result by id    dat-gra2005-ds2$
    Upload Variable Report Template Single File
    Click on protocol to check the error messages
    Assert the error messages for missing files
    Close the protocol dialog
    Get back to home page and deselect project

*** Keywords ***
Get back to home page and deselect project
    Get back to german home page
    Click Element Through Tooltips    xpath=//md-sidenav//project-navbar-module//button[@aria-label='Clear Input']

Upload Variable Report Template Single File
    Choose File   xpath=//input[@type='file' and @ngf-select='ctrl.uploadTexTemplate($files)'][1]   ${CURDIR}${/}singlefile${/}Variablelist.tex  # singlefile folder contains only a single file

Click on protocol to check the error messages
    Click Element Through Tooltips   xpath=//button[@ng-click="showLog()"]//span[contains(., "Protokoll")]

Assert the error messages for missing files
    Page Should Contain Element  xpath=//md-dialog[@class="_md md-transition-in"]//li[contains(.,"Es fehlte die Datei: Main.tex")]
    Page Should Contain Element  xpath=//md-dialog[@class="_md md-transition-in"]//li[contains(.,"Es fehlte die Datei: variables/Variable.tex")]

Close the protocol dialog
    Click Element Through Tooltips  xpath=//button[@ng-click="closeDialog()"][contains(., "Schließen")]
