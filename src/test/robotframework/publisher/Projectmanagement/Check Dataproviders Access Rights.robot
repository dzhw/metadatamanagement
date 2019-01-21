*** Settings ***
Documentation     Publisher Create a new Project and Assign role and check the project as dataprovider
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/project_management_resource.robot


*** Variables ***
${PROJECT_NAME}  hossainrobot
${TOAST_MSSG}  Die Aktion ist nicht möglich

*** Test Cases ***
Check Publisher Can Change Accordingly
   Create Project  ${PROJECT_NAME}${BROWSER}
   Assign a dataprovider  dataprovider  1
   Select Survey Checkbox
   Select Instruments Checkbox
   Select Questions Checkbox
   Select Datasets Checkbox
   Select Variable Checkbox
   Save Changes
   Switch To Status Tab
   Ensure Survey Creation is Possible
   Go Back
   Sleep  1s   #to avoid failling edge test
   Ensure Instrument Create Button is Restricted
   Ensure Dataset Create Button is Restricted
   Publisher Logout

Check Dataprovider Cannot Change Anything
   Login as dataprovider
   Select project by name  ${PROJECT_NAME}${BROWSER}
   Click on Cockpit Button
   Ensure Data Providers Ready Checkbox is Disabled
   Ensure Publisher Ready Checkbox is Disabled
   Ensure Project Release Button is Disabled
   Ensure Project Assign Role Button is Disabled
   Ensure Study Create Button is Restricted
   Ensure Question Upload Button is Restricted
   Switch To Settings Tab
   Ensure Expected Metadata Fields are Disabled
   Data Provider Logout
   Login as publisher
   Delete project by name  ${PROJECT_NAME}${BROWSER}


*** Keywords ***
Ensure Study Create Button is Restricted
    Click Element Through Tooltips  xpath=//md-card[@type="studies"]//button[contains(.,"Neu")]
    Close The Toast Message  ${TOAST_MSSG}

Ensure Question Upload Button is Restricted
    Click Element Through Tooltips  xpath=//md-card[@type="questions"]//button[contains(.,"Hochladen")]
    Run Keyword if  '${BROWSER}' == 'chrome'  Close The Toast Message  ${TOAST_MSSG}
    Run Keyword if  '${BROWSER}' == 'firefox'  Close The Toast Message  ${TOAST_MSSG}
    Run Keyword if  '${BROWSER}' == 'edge'  Close The Toast Message  ${TOAST_MSSG}
    Run Keyword if  '${BROWSER}' == 'ie'  Close The Toast Message for Upload Button in IE

Ensure Instrument Create Button is Restricted
    Click Element Through Tooltips  xpath=//md-card[@type="instruments"]//button[contains(.,"Neu")]
    Close The Toast Message  ${TOAST_MSSG}

Ensure Dataset Create Button is Restricted
    Click Element Through Tooltips  xpath=//md-card[@type="dataSets"]//button[contains(.,"Neu")]
    Close The Toast Message  ${TOAST_MSSG}

Ensure Expected Metadata Fields are Disabled
    Page Should Contain Element  xpath=//md-card[@ng-if="ctrl.project.configuration.requirements"]//following::md-checkbox[@name="survey" and @disabled="disabled"]
    Page Should Contain Element  xpath=//md-card[@ng-if="ctrl.project.configuration.requirements"]//following::md-checkbox[@name="instruments" and @disabled="disabled"]
    Page Should Contain Element  xpath=//md-card[@ng-if="ctrl.project.configuration.requirements"]//following::md-checkbox[@name="questions" and @disabled="disabled"]
    Page Should Contain Element  xpath=//md-card[@ng-if="ctrl.project.configuration.requirements"]//following::md-checkbox[@name="dataSet" and @disabled="disabled"]
    Page Should Contain Element  xpath=//md-card[@ng-if="ctrl.project.configuration.requirements"]//following::md-checkbox[@name="variables" and @disabled="disabled"]

Close The Toast Message for Upload Button in IE
    Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Diese Aktion wird ")]
    Element Should Contain  xpath=//md-toast//span[contains(.,"Diese Aktion wird ")]  Diese Aktion wird vom verwendeten Browser nicht unterstützt.
    Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]
