*** Settings ***
Documentation     Tests the user experience of searching & finding a specific question of the Graduate Panel 2005
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Looking for Absolventenpanel 2005s Fragebogen Erste Welle in german
    Navigate to search
    Search for    Absolventenpanel 2005
    Click on first search result
    Click on questions tab
    Search for in details   Welche Erfahrungen haben Sie (bisher) in Ihrer Ausbildungs- bzw. Praktikumsphase gemacht
    Click on first search result
    Page Should Contain    Bilder zur Frage
    [Teardown]    Get back to german home page

Looking for Graduate Panel 2005s questionnaire first wave in english
    [Setup]    Change language to english
    Navigate to search
    Search for    Graduate Panel 2005
    Click on first search result
    Click on questions tab
    Search for in details   What experiences have you had (so far) during your training/internship?
    Click on first search result
    Page Should Contain    Images of this Question
    [Teardown]    Get back to german home page
