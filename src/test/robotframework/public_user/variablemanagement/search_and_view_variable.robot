*** Settings ***
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening a variable page
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Looking for Absolventenpanel 2005s individual data bachelor in german
    Search for on startpage    Absolventenpanel 2005
    Click on first search result
    Click on variable tab
    Search for in details   Personendatensatz Bachelor studienberatung
    Click on first search result
    Page Should Contain    ordinal
    [Teardown]    Get back to german home page

Looking for Graduate Panel 2005s individual data bachelor in english
    [Setup]    Change language to english
    Search for on startpage    Graduate Panel 2005
    Click on first search result
    Click on variable tab
    Search for in details   individual data bachelor study guidance
    Click on first search result
    Page Should Contain    ordinal
    [Teardown]    Get back to german home page
