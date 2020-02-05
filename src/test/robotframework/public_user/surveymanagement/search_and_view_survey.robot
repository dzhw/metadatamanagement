*** Settings ***
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening the survey page of the first wave for bachelor graduates
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Looking for Absolventenpanel 2005s first wave survey in german
    Search for on startpage    Absolventenpanel 2005
    Click on first search result
    Click on surveys tab
    Search for in details   Erste Welle Bachelor
    Click on first search result
    Page Should Contain    n = 1.622
    [Teardown]    Get back to german home page

Looking for Graduate Panel 2005s first wave survey in english
    [Setup]    Change language to english
    Search for on startpage    Graduate Panel 2005
    Click on first search result
    Click on surveys tab
    Search for in details   First Wave Bachelor
    Click on first search result
    Page Should Contain    n = 1,622
    [Teardown]    Get back to german home page
