*** Settings ***
Documentation     Check Project Releasing and Unreleagsing Rights for Publisher #Prerequisite is to have atleast one study for the project
Force Tags        noslowpoke
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/project_management_resource.robot

*** Test Cases ***
Check The Publisher Ready Checkbox is Not Marked and Release is Not Possible
  Run Keyword If  '${BROWSER}' == 'edge'  Select project by name  robotprojectrelease4edge
  Run Keyword If  '${BROWSER}' == 'ie'  Select project by name  robotprojectrelease4ie
  Run Keyword If  '${BROWSER}' == 'firefox'  Select project by name  robotprojectrelease4firefox
  Run Keyword If  '${BROWSER}' == 'chrome'  Select project by name  robotprojectrelease4chrome
  Click on Cockpit Button
  Change Project Release Status
  Sleep  1s  # to avoid failing in Firefox
  Assert Project Release Action Has Error Message
  Close The Toast Message for Project Release Validation
  Click on OK Button

Check The Publisher Ready Checkbox is Marked but Release Without Saving is Not Possible
  Click Publisher Ready Checkbox for Studies
  Change Project Release Status
  Sleep  1s  # to avoid failing in Firefox
  Assert Project Release Action Has Error Message
  Close The Toast Message for Project Release Validation
  Click on OK Button

Check The Publisher Ready Checkbox is Marked and When Saved Then Release is Possible
  Save Changes
  Change Project Release Status
  Close The Toast Message for Project Release Validation
  Write Version Name
  Click on OK Button

Verify The Released Project is Available under The Study Tab
  Sleep   60s  #We need explicit sleep for 60s to ensure the project is available in the study tab
  Publisher Logout
  Click on study tab
  Run Keyword If  '${BROWSER}' == 'edge'  Element Should Contain  xpath=//md-card-header-text//span[contains(.,"stu-robotprojectrelease4edge$")]  stu-robotprojectrelease4edge$
  Run Keyword If  '${BROWSER}' == 'ie'  Element Should Contain  xpath=//md-card-header-text//span[contains(.,"stu-robotprojectrelease4ie$")]  stu-robotprojectrelease4ie$
  Run Keyword If  '${BROWSER}' == 'firefox'  Element Should Contain  xpath=//md-card-header-text//span[contains(.,"stu-robotprojectrelease4firefox$")]  stu-robotprojectrelease4firefox$
  Run Keyword If  '${BROWSER}' == 'chrome'  Element Should Contain  xpath=//md-card-header-text//span[contains(.,"stu-robotprojectrelease4chrome$")]  stu-robotprojectrelease4chrome$

Verify The Unreleased Project is Unavailable under The Study Tab
  Login as publisher
  Run Keyword If  '${BROWSER}' == 'edge'  Select project by name  robotprojectrelease4edge
  Run Keyword If  '${BROWSER}' == 'ie'  Select project by name  robotprojectrelease4ie
  Run Keyword If  '${BROWSER}' == 'firefox'  Select project by name  robotprojectrelease4firefox
  Run Keyword If  '${BROWSER}' == 'chrome'  Select project by name  robotprojectrelease4chrome
  Click on Cockpit Button
  Change Project Release Status
  Click on OK Button
  Sleep  3s  #to ensure enough time for the next checkbox to be ready  
  Click Publisher Ready Checkbox for Studies   #deselect the check box here
  Save Changes
  Sleep  60s   #We need explicit sleep for 60s to ensure the project is not available in the study tab
  Publisher Logout
  Click on study tab
  Run Keyword If  '${BROWSER}' == 'edge'   Page Should Not Contain  stu-robotprojectrelease4edge$
  Run Keyword If  '${BROWSER}' == 'ie'   Page Should Not Contain  stu-robotprojectrelease4ie$
  Run Keyword If  '${BROWSER}' == 'firefox'   Page Should Not Contain  stu-robotprojectrelease4firefox$
  Run Keyword If  '${BROWSER}' == 'chrome'   Page Should Not Contain  stu-robotprojectrelease4chrome$
  Login as publisher  #we need explicit login to be synced with suite teardown


*** Keywords ***
Assert Project Release Action Has Error Message
  Element Should Contain   xpath=//md-dialog-content//div//p[@class="ng-binding"]   kann nicht freigegeben werden

Write Version Name
 # Click Element Through Tooltips  xpath=//input[@ng-model="release.version"]
  Input Text  xpath=//input[@ng-model="release.version"]  0.0.2

Close The Toast Message for Project Release Validation
  Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Die Post-Validierung wurde mit")]
  Element Should Contain  xpath=//md-toast//span[contains(.,"Die Post-Validierung wurde mit")]  Die Post-Validierung wurde mit
  Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]
