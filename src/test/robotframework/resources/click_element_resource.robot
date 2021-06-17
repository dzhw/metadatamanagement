*** Settings ***
Documentation     Enabling clicks which avoid overlay problems caused by tooltips
Library           SeleniumLibrary
Library           String
Library           AngularJSLibrary  root_selector=[data-ng-app]
Variables         ../common_variables.yaml

*** Variables ***
${BROWSER}        chrome

*** Keywords ***
Click Element Through Tooltips
    [Arguments]    ${xpath_string}
    [Documentation]    Can be used to click hidden elements
    ...    Dependencies
    ...    SeleniumLibrary
    ...    String
    # escape " characters of xpath
    ${var} =    Replace String    ${xpath_string}    xpath=    ${EMPTY}
    ${element_xpath} =    Replace String    ${var}    \"    \\\"
    Run Keyword If    '${BROWSER}' != 'ie'    Wait Until Keyword Succeeds    10s    0.5s    Execute JavaScript    window.document.evaluate("${element_xpath}", document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null).snapshotItem(0).click();
    Run Keyword If    '${BROWSER}' == 'ie'    Wait Until Keyword Succeeds    10s    0.5s    Set Focus To Element    ${xpath_string}
    Run Keyword If    '${BROWSER}' == 'ie'    Wait Until Keyword Succeeds    10s    0.5s    Mouse Over    ${xpath_string}
    Run Keyword If    '${BROWSER}' == 'ie'    Wait Until Keyword Succeeds    10s    0.5s    Click Element    ${xpath_string}
    Wait for Angular    2s
