*** Settings ***#2983
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening its data package page
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Looking for Absolventenpanel 2005 in german
    Search for on startpage    Absolventenpanel 2005
    Click on first search result
    Page Should Contain    Wirtschafts- und Finanzkrise
    Page Should Contain Schema.org Metadata
    Page Should Dublin Core Metadata
    [Teardown]    Get back to german home page

Looking for Graduate Panel 2005 in english
    [Setup]    Change language to english
    Search for on startpage    Graduate Panel 2005
    Click on first search result
    Page Should Contain   economic and financial crisis
    Page Should Contain Schema.org Metadata
    Page Should Dublin Core Metadata
    [Teardown]    Get back to german home page

*** Keywords ***
Page Should Contain Schema.org Metadata
    Page Should Contain Element  xpath=//script[@type="application/ld+json" and contains(.,'"@type":"Dataset"')]  1
    Page Should Contain Element  xpath=//script[@type="application/ld+json" and contains(.,'"publisher":{"name":"FDZ-DZHW"')]  1
    Page Should Contain Element  xpath=//script[@type="application/ld+json" and contains(.,'"name":"Sören Isleib"')]  1

Page Should Dublin Core Metadata
    Page Should Contain Element  xpath=//link[@rel="schema.DC"]  1
    Page Should Contain Element  xpath=//meta[@name="DC.Type" and @content="Dataset"]  1
    Page Should Contain Element  xpath=//meta[@name="DC.Title" and @lang="de" and @content="DZHW-Absolventenpanel 2005"]  1
    Page Should Contain Element  xpath=//meta[@name="DC.Title" and @lang="en" and @content="DZHW Graduate Panel 2005"]  1
