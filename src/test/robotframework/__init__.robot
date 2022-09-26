*** Setting ***
Documentation     Common setup and teardown for all tests
Suite Setup       Setup Tests
Suite Teardown    Finish Tests
Library           SeleniumLibrary
Library           Collections
Library           OperatingSystem
Resource          ./resources/click_element_resource.robot
Resource          ./resources/search_resource.robot
Resource          ./resources/login_resource.robot
Variables         common_variables.yaml

*** Variables ***
${USE_SAUCELABS}    ${EMPTY}
${BROWSER}          chrome
${SAUCELABS_URL}    https://%{SAUCE_USERNAME}:%{SAUCE_ACCESS_KEY}@ondemand.us-west-1.saucelabs.com:443/wd/hub
${BUILD_NUMBER}     local
@{NEEDED_PROJECTS}  robotprojectrelease4${BROWSER}   robotproject4${BROWSER}   robotproject   cmp2014   gra2005   conceptproject${BROWSER}   fileuploadproject   testanalysepaket
@{TEMP_PROJECTS}    temprobotcheckaccess${BROWSER}   temprobotassignroles${BROWSER}  temprobotdeleteroles${BROWSER}   atemprobotcheckicons${BROWSER}   tempdatapackage${BROWSER}   tempanalysis${BROWSER}

*** Keywords ***
Open Local Browser
    Set To Dictionary    ${CAPABILITIES.${BROWSER}}    platform=${EMPTY}
    Open Browser    ${WEBSITE}    ${BROWSER}    desired_capabilities=${CAPABILITIES.${BROWSER}}

Open Saucelabs Browser
    ${BUILD_NUMBER} =    Get Environment Variable    GITHUB_RUN_NUMBER    ${EMPTY}
    Run Keyword If    '${BUILD_NUMBER}' != '${EMPTY}'    Set To Dictionary    ${CAPABILITIES.${BROWSER}}    build=${BUILD_NUMBER}
    Open Browser    ${WEBSITE}    ${BROWSER}    remote_url=${SAUCELABS_URL}    desired_capabilities=${CAPABILITIES.${BROWSER}}

Open Home Page
    Run Keyword If    '${USE_SAUCELABS}' == '${EMPTY}'    Open Local Browser
    Run Keyword If    '${USE_SAUCELABS}' != '${EMPTY}'    Open Saucelabs Browser
    Set Window Size    800    600
    Maximize Browser Window
    Close Tracking Consent
    Close Speech Bubble

Close Tracking Consent
    Click Element Through Tooltips  xpath=//user-consent-component//button[@ng-click='consentGiven()']

Close Speech Bubble
    Click Element Through Tooltips  xpath=//report-publications-component//button[@ng-click='closeSpeechBubble()']

Check Needed Projects Exist
    [Documentation]  Checks if needed projects exist. If not fails with fatal error and stops complete execution after teardown.
    Login as publisher
    FOR  ${PROJECT}  IN  @{NEEDED_PROJECTS}
       ${passed}  Run Keyword and Return Status   Check project exists  ${PROJECT}
       Run Keyword If    '${passed}' == 'False'    Fatal Error    Needed project with name ${PROJECT} is not available.
    END
    Publisher Logout

Setup Tests
    Open Home Page
    Check Needed Projects Exist

Finish Tests
    [Documentation]  Deletes temporary test projects and finishes the execution
    Any User Logout
    Login as publisher
    ${PROJECTNAMES}  Create List   tempdatapackage${BROWSER}   tempanalysis${BROWSER}   temprobotcheckaccess${BROWSER}   temprobotassignroles${BROWSER}   temprobotdeleteroles${BROWSER}    atemprobotcheckicons${BROWSER}
    FOR   ${PROJECT}   IN   @{PROJECTNAMES}
      Run Keyword And Ignore Error  Delete project by name  ${PROJECT}
    END
    Publisher Logout

    Run Keyword If    '${USE_SAUCELABS}' != '${EMPTY}'    Import Library    SauceLabs
    Run Keyword If    '${USE_SAUCELABS}' != '${EMPTY}'    Report test status    ${CAPABILITIES.${BROWSER}.name}    ${SUITE STATUS}    ${EMPTY}    ${SAUCELABS_URL}
    Close All Browsers
