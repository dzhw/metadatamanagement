*** Settings ***
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening its data package page
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Looking for Absolventenpanel 2005 in german
    Search for on startpage    Absolventenpanel 2005
    Click on first search result
    Page Should Contain    Wirtschafts- und Finanzkrise
    [Teardown]    Get back to german home page

Looking for Graduate Panel 2005 in english
    [Setup]    Change language to english
    Search for on startpage    Graduate Panel 2005
    Click on first search result
    Page Should Contain   economic and financial crisis
    [Teardown]    Get back to german home page
