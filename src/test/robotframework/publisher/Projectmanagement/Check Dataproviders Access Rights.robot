*** Settings ***
Documentation     Publisher Create a new Project and Assign role and check the project as dataprovider
Metadata          Info on data    This test suite creates a temporary test project with the name "${PROJECT_NAME}${BROWSER}" which is automatically deleted afterwards
Force Tags        noslowpoke
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/project_management_resource.robot

*** Variables ***
${PROJECT_NAME}  temprobotassignroles
${TOAST_MSSG}  Die Aktion ist nicht möglich

*** Test Cases ***
Check Publisher Can Change Accordingly
   ${created}  Run Keyword and return status  Create Project  ${PROJECT_NAME}${BROWSER}
   Run Keyword If  ${created}==False  Fail  Could not create new project '${PROJECT_NAME}${BROWSER}'
   Assign a dataprovider  dataprovider
   Select Metadata Checkbox From The List
   Switch To Status Tab
   Ensure Survey Creation is Possible
   Go Back
   Sleep  1s   #to avoid failling edge test
   Ensure Create Button is Restricted   instruments
   Ensure Create Button is Restricted   dataSets
   Publisher Logout

Check Dataprovider Cannot Change Anything
   Login as dataprovider
   Select project by name  ${PROJECT_NAME}${BROWSER}
   Click on Cockpit Button
   Ensure Data Providers Ready Checkbox is Disabled
   Ensure Publisher Ready Checkbox is Disabled
   Ensure Project Release Button is Disabled
   Ensure Project Assign Role Button is Disabled
   Ensure Create Button is Restricted   dataPackages
   Ensure Upload Button is Restricted   questions
   Switch To Settings Tab
   Ensure Expected Metadata Fields are Disabled From The List
   Data Provider Logout

Check Project is Assigned to Dataprovider But Can Not Change Anything When Publisher is Ready
   Login as publisher
   Select project by name  ${PROJECT_NAME}${BROWSER}
   Click on Cockpit Button
   Click Publisher and Dataprovider Ready Checkbox From The List
   Change Project Roles
   Write Message and Assign
   Sleep  1s  #to ensure that the next locator is visible
   Assert Project is Assigned to Dataprovider
   Publisher Logout
   Login as dataprovider
   Select project by name  ${PROJECT_NAME}${BROWSER}
   Click on Cockpit Button
   Ensure Create Button is Restricted From The List
   Ensure Upload Button is Restricted From The List
   Data Provider Logout
   Login as publisher
   Delete project by name  ${PROJECT_NAME}${BROWSER}


*** Keywords ***
Assert Project is Assigned to Dataprovider
    Element Should Contain  xpath=//project-status-badge//span[contains(normalize-space(.),"Zugewiesen an Datengeber:innen")]  Zugewiesen an Datengeber:innen

Ensure Create Button is Restricted
    [Arguments]  ${metadataname}
    Click Element Through Tooltips  xpath=//md-card[@type="${metadataname}"]//button[contains(.,"Neu")]
    Close The Toast Message  ${TOAST_MSSG}

Ensure Upload Button is Restricted
    [Arguments]  ${metadataname}
    Click Element Through Tooltips  xpath=//md-card[@type="${metadataname}"]//button[contains(.,"Hochladen")]
    @{MD_BROWSERS}    Create List   edge  firefox   chrome  safari
    FOR   ${MD_BR}   IN  @{MD_BROWSERS}
        Run Keyword If  '${BROWSER}' == '${MD_BR}'  Close The Toast Message  ${TOAST_MSSG}
    END
    Run Keyword if  '${BROWSER}' == 'ie'  Close The Toast Message for Upload Button in IE   #in IE toast mesaage is different

Ensure Create Button is Restricted From The List
    @{MD_ITEMS}    Create List    dataPackages   surveys   instruments   dataSets
    FOR   ${MD_DT}   IN  @{MD_ITEMS}
        Click Element Through Tooltips  xpath=//md-card[@type="${MD_DT}"]//button[contains(.,"Neu")]
        Close The Toast Message  ${TOAST_MSSG}
    END

Ensure Upload Button is Restricted From The List
    @{MD_ITEMS}    Create List   questions   variables
    FOR   ${MD_DT}   IN  @{MD_ITEMS}
        Click Element Through Tooltips  xpath=//md-card[@type="${MD_DT}"]//button[contains(.,"Hochladen")]
        Run Keyword if  '${BROWSER}' == 'chrome'  Close The Toast Message  ${TOAST_MSSG}
        Run Keyword if  '${BROWSER}' == 'safari'  Close The Toast Message  ${TOAST_MSSG}
        Run Keyword if  '${BROWSER}' == 'firefox'  Close The Toast Message  ${TOAST_MSSG}
        Run Keyword if  '${BROWSER}' == 'edge'  Close The Toast Message  ${TOAST_MSSG}
        Run Keyword if  '${BROWSER}' == 'ie'  Close The Toast Message for Upload Button in IE   #in IE toast mesaage is different
    END

Ensure Expected Metadata Fields are Disabled From The List
    @{MD_ITEMS}    Create List    survey   instruments   questions   dataSet    variables
    FOR   ${MD_DT}   IN  @{MD_ITEMS}
        Page Should Contain Element  xpath=//md-card[@ng-if="ctrl.project.configuration.requirements"]//following::md-checkbox[@name="${MD_DT}" and @disabled="disabled"]
    END

Close The Toast Message for Upload Button in IE
    Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Diese Aktion wird ")]
    Element Should Contain  xpath=//md-toast//span[contains(.,"Diese Aktion wird ")]  Diese Aktion wird vom verwendeten Browser nicht unterstützt.
    Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]

Click Publisher and Dataprovider Ready Checkbox From The List
    @{MD_ITEMS}    Create List   surveys   instruments   questions   dataSets    variables   dataPackages  #data packages should be at last index to avoid outofbound error in IE
    FOR   ${MD_DT}   IN  @{MD_ITEMS}
        Click Publisher Ready Checkbox  ${MD_DT}
        Click Dataprovider Ready Checkbox  ${MD_DT}
    END

Select Metadata Checkbox From The List
    @{MD_ITEMS}    Create List   survey   instruments   questions   dataSet    variables
    FOR   ${MD_DT}   IN  @{MD_ITEMS}
        Click Element Through Tooltips  xpath=//md-checkbox[@name="${MD_DT}"]
    END
