*** Setting ***
Documentation     Common setup and teardown for all tests
Suite Setup       Open Home Page
Suite Teardown    Finish Tests
Library           ExtendedSelenium2Library
Library           Collections
Library           OperatingSystem
Variables         common_variables.yaml

*** Variables ***
${USE_BROWSERSTACK}    ${EMPTY}
${BROWSER}        chrome
${BROWSERSTACK_URL}    https://%{BROWSERSTACK_USERNAME}:%{BROWSERSTACK_ACCESS_KEY}@hub-cloud.browserstack.com/wd/hub
${BUILD_NUMBER}    local

*** Keywords ***
Open Local Browser
    Open Browser    ${WEBSITE}    ${BROWSER}    desired_capabilities=${CAPABILITIES.${BROWSER}}

Open Browserstack Browser
    ${BUILD_NUMBER} =    Get Environment Variable    TRAVIS_BUILD_NUMBER    ${EMPTY}
    Run Keyword If    '${BUILD_NUMBER}' != '${EMPTY}'    Set To Dictionary    ${CAPABILITIES.${BROWSER}}    build=${BUILD_NUMBER}
    Open Browser    ${WEBSITE}    ${BROWSER}    remote_url=${BROWSERSTACK_URL}    desired_capabilities=${CAPABILITIES.${BROWSER}}

Open Home Page
    Run Keyword If    '${USE_BROWSERSTACK}' == '${EMPTY}'    Open Local Browser
    Run Keyword If    '${USE_BROWSERSTACK}' != '${EMPTY}'    Open Browserstack Browser
    Set Window Size    800    600
    Maximize Browser Window

Finish Tests
    Run Keyword If    '${USE_BROWSERSTACK}' != '${EMPTY}'    Import Library    Browserstack
    Run Keyword If    '${USE_BROWSERSTACK}' != '${EMPTY}'    Report test status    ${CAPABILITIES.${BROWSER}.name}    ${SUITE STATUS}    ${EMPTY}    ${BROWSERSTACK_URL}
    Close All Browsers
