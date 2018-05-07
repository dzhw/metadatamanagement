*** Settings ***
Documentation     Resource used by all test cases of the public user
Library           ExtendedSelenium2Library
Library           Collections
Library           OperatingSystem
Library           String
Variables         ../common_variables.yaml

*** Variables ***
${USE_SAUCELABS}  ${EMPTY}
${BROWSER}        chrome
${SAUCELABS_URL}  https://%{SAUCE_USERNAME}:%{SAUCE_ACCESS_KEY}@ondemand.saucelabs.com/wd/hub
${BUILD_NUMBER}   local

*** Keywords ***
Open Local Browser
    Open Browser  ${WEBSITE}  ${BROWSER}
    ...           desired_capabilities=${CAPABILITIES.${BROWSER}}

Open Saucelabs Browser
    ${BUILD_NUMBER} =   Get Environment Variable  TRAVIS_BUILD_NUMBER  ${EMPTY}
    Run Keyword If    '${BUILD_NUMBER}' != '${EMPTY}'
    ...           Set To Dictionary   ${CAPABILITIES.${BROWSER}}  build=${BUILD_NUMBER}
    Open Browser  ${WEBSITE}  ${BROWSER}
    ...           remote_url=${SAUCELABS_URL}
    ...           desired_capabilities=${CAPABILITIES.${BROWSER}}

Open Home Page
    Run Keyword If    '${USE_SAUCELABS}' == '${EMPTY}'    Open Local Browser
    Run Keyword If    '${USE_SAUCELABS}' != '${EMPTY}'    Open Saucelabs Browser
    Set Window Size  800  600
    Maximize Browser Window

Finish Test
    Run Keyword If    '${USE_SAUCELABS}' != '${EMPTY}'
    ...               Import Library    SauceLabs
    Run Keyword If    '${USE_SAUCELABS}' != '${EMPTY}'
    ...               Report test status  ${CAPABILITIES.${BROWSER}.name}
    ...               ${SUITE STATUS}  ${EMPTY}  ${SAUCELABS_URL}
    Close All Browsers

Click Element Through Tooltips
    [Documentation]
    ...     Can be used to click hidden elements
    ...     Dependencies
    ...         SeleniumLibrary
    ...         String
    [Arguments]     ${xpath_string}
    # escape " characters of xpath
    ${var} =  Replace String      ${xpath_string}        xpath=  ${EMPTY}
    ${element_xpath} =  Replace String      ${var}        \"  \\\"
    Run Keyword If  '${BROWSER}' == 'firefox'
    ...             Execute JavaScript  window.document.evaluate("${element_xpath}", document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null).snapshotItem(0).click();
    Run Keyword If  '${BROWSER}' != 'firefox'
    ...             Mouse Over  ${xpath_string}
    Run Keyword If  '${BROWSER}' != 'firefox'
    ...             Wait Until Keyword Succeeds  5s  1s  Click Element  ${xpath_string}
