*** Settings ***
Documentation     Tests downloading an instrument attachment
Force Tags        noslowpoke
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Looking for Absolventenpanel 2005s Fragebogen Erste Welle in german
    Navigate to search
    Search for    Absolventenpanel 2005
    Click on first search result
    Click on instruments tab
    Search for in details   Fragebogen Erste Welle
    Click on first search result
    Click on questionnaire
    Sleep    2s
    Switch windows forth and back
    [Teardown]    Get back to german home page

*** Keywords ***
Click on questionnaire
   Click Element Through Tooltips    xpath=//md-card//a[contains(text(),'gra2005_W1_Questionnaire_de.pdf')]

Switch windows forth and back
    Select Window    NEW
    Close Window
    Select Window    MAIN
