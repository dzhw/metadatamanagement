*** Settings ***
Documentation     Check Project Releasing and Unreleasing Rights for Publisher #Prerequisite is to have atleast one data package for the project
Metadata          Info on data    This test suite uses the project with the name "robotprojectrelease4${BROWSER}" which needs to be available in the testing environment.
Force Tags        noslowpoke
Suite Setup       Check test project release status
Suite Teardown    Unrelased The Project again to Sync with Intial Step
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/project_management_resource.robot

# TODO: refactor test (test case always failed because of long and unpredictable waiting time for project releases)
*** Test Cases ***
#Check Publishers Project Releasing and Unreleasing Funtionalities
#  Select project by name  robotprojectrelease4${BROWSER}
#  Click on Cockpit Button
#  Change Project Release Status
#  Write Version Name
#  Confirm Release
#  Click on OK Button
#  Sleep  2s  # to avoid failing in firefox
#  Assert Project Release Action Has Error Message
#  Close The Toast Message for Project Release Validation
#  Click on OK Button
#  #Click Publisher Ready Checkbox for Data Packages
#  Click Publisher Ready Checkbox  dataPackages
#  Change Project Release Status
#  Write Version Name
#  Confirm Release
#  Click on OK Button
# # Verify The Released Project is Available under The Data Package Tab
#  Verify The Unreleased Project is Still Available under The Data Package Tab with Shadow Copy
#  Edit Project Title and Check it does not appear under data package when unreleased
#  Check Edited Project appears under data package when released
#  Restore The Previous Project Version and Publish Again
#  Verify The Re-Released Previous Project is Available under The Data Package Tab
# # Unrelased The Project again to Sync with Intial Step

*** Keywords ***
Assert Project Release Action Has Error Message
  Wait Until Element Is Visible   xpath=//md-dialog-content//h2
  Element Should Contain   xpath=//md-dialog-content//h2   kann nicht freigegeben werden

Write Version Name
  Input Text  xpath=//input[@name="version"]   0.0.2
  # TODO if local hint: tick checkbox

Confirm Release
  #Confirm Local Release
  Click Element Through Tooltips  xpath=//md-checkbox[@name="releaseConfirmed"]

Close The Toast Message for Project Release Validation
  Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Die Post-Validierung wurde mit")]
  Element Should Contain  xpath=//md-toast//span[contains(.,"Die Post-Validierung wurde mit")]  Die Post-Validierung wurde mit
  Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]

Click Data Package Edit Button
  Click Element Through Tooltips  xpath=(//datapackage-search-result//md-card-actions//button[normalize-space()="Bearbeiten"])[1]

Click Data Package Save Button
  Click Element Through Tooltips  xpath=//button[@type="submit"]//md-icon[contains(. , "save")]

Click Restore Button
  Click Element Through Tooltips  xpath=//div[@ng-if="ctrl.dataPackage.id"]//button[@type="button"]//md-icon[contains(.,"history")]

Revise to second latest version
  Click Element Through Tooltips  xpath=//md-dialog//table//tbody//tr[1]//td
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
  Wait for Angular    10s
  Click Publisher Ready Checkbox  dataPackages
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
  Sleep  30s
  Publisher Logout
  Navigate to search
  Search for  Test Project Release Study ${BROWSER} DE
  Wait For Angular    30s
  Wait Until Page Contains Element  xpath=//md-card-header-text//span[contains(. ,"Test Project Release Study ${BROWSER} DE")]  10s

Check Edited Project appears under data package when released
  Login as publisher
  Select project by name  robotprojectrelease4${BROWSER}
  Click on Cockpit Button
  Click Publisher Ready Checkbox  dataPackages
  Change Project Release Status
  Write Version Name
  Confirm Release
  Click on OK Button
  Sleep  90s  #We need explicit sleep to ensure the project is available under the data package tab
  Publisher Logout   #explicit logout
  Navigate to search
  Search for  Test Project Release Study ${BROWSER} DE Edit_786
  Wait For Angular    30s
  Wait Until Page Contains Element  xpath=//md-card-header-text//span[contains(. ,"Test Project Release Study ${BROWSER} DE Edit_786")]  10s

Restore The Previous Project Version and Publish Again
  Login as publisher
  Select project by name  robotprojectrelease4${BROWSER}
  Click on Cockpit Button
  Change Project Release Status
  Click on OK Button
  Wait for Angular    10s
  Click Publisher Ready Checkbox  dataPackages
  Get back to german home page
  Click Data Package Edit Button
  Click Restore Button
  Revise to second latest version   #select the previous version
  Click Data Package Save Button
  Click on Cockpit Button
  Click Publisher Ready Checkbox  dataPackages
  Change Project Release Status  #release the previous version again
  Confirm Release
  Click on OK Button
  Sleep  90s

Verify The Re-Released Previous Project is Available under The Data Package Tab
  Wait For Angular  10s
  Publisher Logout
  Navigate to search
  Search for  Test Project Release Study ${BROWSER} DE
  Wait For Angular    30s
  Wait Until Page Contains Element  xpath=//md-card-header-text//span[contains(. ,"Test Project Release Study ${BROWSER} DE")]  10s

Unrelased The Project again to Sync with Intial Step
  Login as publisher
  Select project by name  robotprojectrelease4${BROWSER}
  Click on Cockpit Button
  Change Project Release Status  #unrelease the project to initial state
  Click on OK Button
  Wait for Angular    10s
  Click Publisher Ready Checkbox  dataPackages
  Sleep  5s
  Get back to home page and deselect project

Check test project release status
  # Test project needs to be unreleased to start test
  # Data Packages need to be not ready by publisher and dataprovider
  Select project by name  robotprojectrelease4${BROWSER}
  Click on Cockpit Button
  ${passed}  Run Keyword and Return status   Ensure Project Is Unreleased
  Run Keyword If  ${passed} == False   Fail   robotprojectrelease4${BROWSER} needs to be unreleased before starting this test
  Get back to home page and deselect project

