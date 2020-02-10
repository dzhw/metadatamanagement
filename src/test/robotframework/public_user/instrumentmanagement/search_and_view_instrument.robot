*** Settings ***
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening the questionnaire
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Looking for Absolventenpanel 2005s Fragebogen Erste Welle in german
    Navigate to search
    Search for    Absolventenpanel 2005
    Click on first search result
    Click on instruments tab
    Search for in details    Fragebogen Erste Welle
    Click on first search result
    Page Should Contain    Filterführungsdiagramm
    [Teardown]    Get back to german home page

Looking for Graduate Panel 2005s questionnaire first wave in english
    [Setup]    Change language to english
    Navigate to search
    Search for    Graduate Panel 2005
    Click on first search result
    Click on instruments tab
    Search for in details    Questionnaire First Wave
    Click on first search result
    Page Should Contain    Question Flow
    [Teardown]    Get back to german home page
