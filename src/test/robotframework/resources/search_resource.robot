*** Settings ***
Documentation     Resource used by all search and view test cases
Library           ExtendedSelenium2Library
Resource          click_element_resource.robot
Variables         ../common_variables.yaml

*** Keywords ***
Search for
    [Arguments]    ${query}
    Click Element Through Tooltips    xpath=//input[@id='query']
    Input Text    id=query    ${query}
    Wait Until Angular Ready    10s

Search for on startpage
    [Arguments]    ${query}
    Click Element Through Tooltips    xpath=//input[@id='query']
    Input Text    id=query    ${query}
    Press Key     id=query    \\13
    Wait Until Angular Ready    10s

Search for in details
    [Arguments]    ${query}
    Click Element Through Tooltips    xpath=//input[@id='detailSearchQuery']
    Input Text    id=detailSearchQuery    ${query}
    Wait Until Angular Ready    10s

Select project by name
    [Arguments]    ${projectname}
    Input Text    xpath=//md-sidenav//project-navbar-module//md-autocomplete//input    ${projectname}
    Run Keyword And Ignore Error  Click Element Through Tooltips    xpath=//md-virtual-repeat-container//span[text()='${projectname}']

Delete project by name
    [Arguments]    ${projectname}
    Input Text    xpath=//input[@placeholder = 'Projekt auswählen']    ${projectname}
    Click Element Through Tooltips    xpath=//md-sidenav/descendant::button[md-icon[text()='']]
    Click Element Through Tooltips    xpath=//button[text()='OK']
    Run Keyword If    '${BROWSER}' == 'safari'    Sleep  10s 

Click on search result by id
    [Arguments]    ${id}
    Click Element Through Tooltips    xpath=//a//span[text()='${id}']

Click on first search result
    Click Element Through Tooltips    xpath=(//md-card[contains(@class,'fdz-search-result')]//md-card-header//a)[1]

Activate Filter by name
    [Arguments]    ${filtername}
    Click Element Through Tooltips    xpath=//search-filter-panel//md-select
    Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., '${filtername}')]

Choose Filter Option by id
    [Arguments]    ${id}
    Click Element Through Tooltips    xpath=//md-virtual-repeat-container//li//span[text()='${id}']

Click on study tab
    ${url} =    Get Location
    Run Keyword If    '/de/' in '${url}'    Click Element Through Tooltips    xpath=//md-pagination-wrapper/md-tab-item[contains(.,'Studie')]
    Run Keyword If    '/en/' in '${url}'    Click Element Through Tooltips    xpath=//md-pagination-wrapper/md-tab-item[contains(.,'Study')]

Click on surveys tab
    ${url} =    Get Location
    Run Keyword If    '/de/' in '${url}'    Click Element Through Tooltips    xpath=//md-pagination-wrapper/md-tab-item[contains(.,'Erhebung')]
    Run Keyword If    '/en/' in '${url}'    Click Element Through Tooltips    xpath=//md-pagination-wrapper/md-tab-item[contains(.,'Survey')]

Click on instruments tab
    ${url} =    Get Location
    Run Keyword If    '/de/' in '${url}'    Click Element Through Tooltips    xpath=//md-pagination-wrapper/md-tab-item[contains(.,'Instrumente')]
    Run Keyword If    '/en/' in '${url}'    Click Element Through Tooltips    xpath=//md-pagination-wrapper/md-tab-item[contains(.,'Instruments')]

Click on questions tab
    ${url} =    Get Location
    Run Keyword If    '/de/' in '${url}'    Click Element Through Tooltips    xpath=//md-pagination-wrapper/md-tab-item[contains(.,'Fragen')]
    Run Keyword If    '/en/' in '${url}'    Click Element Through Tooltips    xpath=//md-pagination-wrapper/md-tab-item[contains(.,'Questions')]

Click on data set tab
    ${url} =    Get Location
    Run Keyword If    '/de/' in '${url}'    Click Element Through Tooltips    xpath=//md-pagination-wrapper/md-tab-item[contains(.,'Datensätze')]
    Run Keyword If    '/en/' in '${url}'    Click Element Through Tooltips    xpath=//md-pagination-wrapper/md-tab-item[contains(.,'Data Sets')]

Click on variable tab
    ${url} =    Get Location
    Run Keyword If    '/de/' in '${url}'    Click Element Through Tooltips    xpath=//md-pagination-wrapper/md-tab-item[contains(.,'Variablen')]
    Run Keyword If    '/en/' in '${url}'    Click Element Through Tooltips    xpath=//md-pagination-wrapper/md-tab-item[contains(.,'Variables')]

Click on publications tab
    ${url} =    Get Location
    Run Keyword If    '/de/' in '${url}'    Click Element Through Tooltips    xpath=//md-pagination-wrapper/md-tab-item[contains(.,'Publikationen')]
    Run Keyword If    '/en/' in '${url}'    Click Element Through Tooltips    xpath=//md-pagination-wrapper/md-tab-item[contains(.,'Publications')]

Click on concept tab
    ${url} =    Get Location
    Run Keyword If    '/de/' in '${url}'    Click Element Through Tooltips    xpath=//md-pagination-wrapper/md-tab-item[contains(.,'Konzepte')]
    Run Keyword If    '/en/' in '${url}'    Click Element Through Tooltips    xpath=//md-pagination-wrapper/md-tab-item[contains(.,'Concepts')]

Click Survey Edit Button
    Click Element Through Tooltips    xpath=//ui-view//button[normalize-space()='Bearbeiten']

Click on Cockpit Button
    Click Element Through Tooltips    xpath=//project-navbar-module//a[contains(@class, 'md-accent')]
