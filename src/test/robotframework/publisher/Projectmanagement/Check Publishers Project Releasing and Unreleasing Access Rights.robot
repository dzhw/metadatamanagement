*** Settings ***
Documentation     Check Project Releasing and Unreleasing Rights for Publisher #Prerequisite is to have atleast one data package for the project
Force Tags        noslowpoke
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/project_management_resource.robot

*** Test Cases ***
Check Publishers Project Releasing and Unreleasing Funtionalities
  Select project by name  robotprojectrelease4${BROWSER}
  Click on Cockpit Button
  Change Project Release Status
  Click on OK Button
  Sleep  2s  # to avoid failing in firefox
  Assert Project Release Action Has Error Message
  Close The Toast Message for Project Release Validation
  Click on OK Button
  Click Publisher Ready Checkbox for Data Packages
  Change Project Release Status
  Write Version Name
  Click on OK Button
  Verify The Released Project is Available under The Data Package Tab
  Verify The Unreleased Project is Still Available under The Data Package Tab with Shadow Copy
  Edit Project Title and Check it does not appear under data package when unreleased
  Check Edited Project appears under data package when released
  Restore The Previous Project Version and Publish Again
  Verify The Re-Released Previous Project is Available under The Data Package Tab
  Unrelased The Project again to Sync with Intial Step

*** Keywords ***
Assert Project Release Action Has Error Message
  Wait Until Element Is Visible   xpath=//md-dialog-content//h2
  Element Should Contain   xpath=//md-dialog-content//h2   kann nicht freigegeben werden

Write Version Name
  Input Text  xpath=//input[@name="version"]   0.0.2

Close The Toast Message for Project Release Validation
  Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Die Post-Validierung wurde mit")]
  Element Should Contain  xpath=//md-toast//span[contains(.,"Die Post-Validierung wurde mit")]  Die Post-Validierung wurde mit
  Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]

Click Data Package Edit Button
  Click Element Through Tooltips  xpath=(//data-package-search-result//md-card-actions//button[normalize-space()="Bearbeiten"])[1]

Click Data Package Save Button
  Click Element Through Tooltips  xpath=//button[@type="submit"]//md-icon[contains(. , "save")]

Click Restore Button
  Click Element Through Tooltips  xpath=//div[@ng-if="ctrl.dataPackage.id"]//button[@type="button"]//md-icon[contains(.,"history")]

Revise to second latest version
  Click Element Through Tooltips  xpath=//md-dialog//table//tbody//tr[2]//td
  Sleep  1s

Get back to home page and deselect project
  Get back to german home page
  Click Element Through Tooltips    xpath=//md-sidenav//project-navbar-module//button[@aria-label="Clear Input"]

Verify The Released Project is Available under The Data Package Tab
  Sleep  60s  #We need explicit sleep to ensure the project is available under the data package tab
  Publisher Logout   #explicit logout
  Navigate to search
  Search for  stu-robotprojectrelease4${BROWSER}$
  Wait Until Page Contains Element  xpath=//md-card-header-text//span[contains(.,"Test Project Release Study ${BROWSER}")]  5s

Verify The Unreleased Project is Still Available under The Data Package Tab with Shadow Copy
  Login as publisher
  Select project by name  robotprojectrelease4${BROWSER}
  Click on Cockpit Button
  Change Project Release Status
  Click on OK Button
  Wait Until Angular Ready    10s
  Click Publisher Ready Checkbox for Data Packages
  Sleep  5s   #We need explicit sleep to ensure the project is not available under the data package tab
  Publisher Logout
  Navigate to search
  Search for  stu-robotprojectrelease4${BROWSER}$
  Wait Until Page Contains Element  xpath=//md-card-header-text//span[contains(.,"Test Project Release Study ${BROWSER}")]  5s

Edit Project Title and Check it does not appear under data package when unreleased
  Login as publisher  #we need explicit login to be synced with suite teardown
  Select project by name  robotprojectrelease4${BROWSER}
  Click Data Package Edit Button
  Input Text    name=titleDe    Test Project Release Study ${BROWSER} DE Edit_786
  Input Text    name=titleEn    Test Project Release Study ${BROWSER} EN Edit_786
  Click Data Package Save Button
  Get back to german home page
  Sleep  10s
  Publisher Logout
  Navigate to search
  Search for  Test Project Release Study ${BROWSER} DE
  Wait Until Page Contains Element  xpath=//md-card-header-text//span[contains(. ,"Test Project Release Study ${BROWSER} DE")]  10s

Check Edited Project appears under data package when released
  Login as publisher
  Select project by name  robotprojectrelease4${BROWSER}
  Click on Cockpit Button
  Click Publisher Ready Checkbox for Data Packages
  Change Project Release Status
  Write Version Name
  Click on OK Button
  Sleep  90s  #We need explicit sleep to ensure the project is available under the data package tab
  Publisher Logout   #explicit logout
  Navigate to search
  Search for  Test Project Release Study ${BROWSER} DE Edit_786
  Wait Until Page Contains Element  xpath=//md-card-header-text//span[contains(. ,"Test Project Release Study ${BROWSER} DE Edit_786")]  10s

Restore The Previous Project Version and Publish Again
  Login as publisher
  Select project by name  robotprojectrelease4${BROWSER}
  Click on Cockpit Button
  Change Project Release Status
  Click on OK Button
  Wait Until Angular Ready    10s
  Click Publisher Ready Checkbox for Data Packages
  Get back to german home page
  Click Data Package Edit Button
  Click Restore Button
  Revise to second latest version   #select the previous version
  Click Data Package Save Button
  Click on Cockpit Button
  Click Publisher Ready Checkbox for Data Packages
  Change Project Release Status  #release the previous version again
  Click on OK Button
  Sleep  90s

Verify The Re-Released Previous Project is Available under The Data Package Tab
  Publisher Logout
  Navigate to search
  Search for  Test Project Release Study ${BROWSER} DE
  Wait Until Page Contains Element  xpath=//md-card-header-text//span[contains(. ,"Test Project Release Study ${BROWSER} DE")]  10s

Unrelased The Project again to Sync with Intial Step
  Login as publisher
  Select project by name  robotprojectrelease4${BROWSER}
  Click on Cockpit Button
  Change Project Release Status  #unrelease the project to initial state
  Click on OK Button
  Wait Until Angular Ready    10s
  Click Publisher Ready Checkbox for Data Packages
  Sleep  5s
  Get back to home page and deselect project
