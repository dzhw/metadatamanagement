*** Settings ***
Documentation     Tests downloading an instrument attachment
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Looking for Absolventenpanel 2005s Fragebogen Erste Welle in german
    Pass Execution If    '${BROWSER}' == 'edge'    Switching windows fails in EDGE
    Click on instruments tab
    Search for    Absolventenpanel 2005 Fragebogen Erste Welle
    Click on search result by id    ins-gra2005-ins1$
    Click on questionnaire
    Sleep    2s
    Switch windows forth and back
    [Teardown]    Get back to german home page

*** Keywords ***
Click on questionnaire
    Click Element Through Tooltips    xpath=//md-card[@ng-if="ctrl.attachments"]//a[contains(text(),'gra2005_W1_Questionnaire_de.pdf')]

Switch windows forth and back
    Select Window    NEW
    Close Window
    Select Window    MAIN
