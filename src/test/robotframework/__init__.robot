*** Setting ***
Documentation     Common setup and teardown for all tests
Suite Setup       Open Home Page
Suite Teardown    Finish Tests
Library           ExtendedSelenium2Library
Library           Collections
Library           OperatingSystem
Variables         common_variables.yaml

*** Variables ***
${USE_SAUCELABS}    ${EMPTY}
${BROWSER}        chrome
${SAUCELABS_URL}    https://%{SAUCE_USERNAME}:%{SAUCE_ACCESS_KEY}@ondemand.us-west-1.saucelabs.com:443/wd/hub
${BUILD_NUMBER}    local

*** Keywords ***
Open Local Browser
    Open Browser    ${WEBSITE}    ${BROWSER}    desired_capabilities=${CAPABILITIES.${BROWSER}}

Open Saucelabs Browser
    ${BUILD_NUMBER} =    Get Environment Variable    TRAVIS_BUILD_NUMBER    ${EMPTY}
    Run Keyword If    '${BUILD_NUMBER}' != '${EMPTY}'    Set To Dictionary    ${CAPABILITIES.${BROWSER}}    build=${BUILD_NUMBER}
    Open Browser    ${WEBSITE}    ${BROWSER}    remote_url=${SAUCELABS_URL}    desired_capabilities=${CAPABILITIES.${BROWSER}}

Open Home Page
    Run Keyword If    '${USE_SAUCELABS}' == '${EMPTY}'    Open Local Browser
    Run Keyword If    '${USE_SAUCELABS}' != '${EMPTY}'    Open Saucelabs Browser
    Set Window Size    800    600
    Maximize Browser Window

Finish Tests
    Run Keyword If    '${USE_SAUCELABS}' != '${EMPTY}'    Import Library    SauceLabs
    Run Keyword If    '${USE_SAUCELABS}' != '${EMPTY}'    Report test status    ${CAPABILITIES.${BROWSER}.name}    ${SUITE STATUS}    ${EMPTY}    ${SAUCELABS_URL}
    Close All Browsers
