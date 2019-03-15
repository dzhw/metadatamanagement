*** Settings ***
Documentation     Upload related publications excel file which replace all old publications and rewrite with new one
Resource          ../../resources/search_resource.robot
Resource          ../../resources/project_management_resource.robot

*** Test Cases ***
Upload excel file for related publications
   [Tags]   chromeonly
   Click on publications tab
   Upload Excel file
   Click on OK Button
   Close The Toast Message for upload
   Assert New Publication Entry with Excel Upload

*** Keywords ***
Upload Excel file
    press key  xpath=//input[@type='file' and @ngf-select='uploadRelatedPublications($file)'][1]   ${CURDIR}/publicationdata/relatedPublications.xls

Close The Toast Message for upload
     Wait Until Element Is Visible   xpath=//md-toast//span[contains(., "Upload von 48 Publikationen")]  # to ensure enough time for uploading and next test
     Click Element Through Tooltips   xpath=//md-toast//span[contains(., "Upload von 48 Publikationen")]
     Element Should Contain  xpath=//md-toast//span[contains(.,"Upload von 48 Publikationen")]  Upload von 48 Publikationen
     Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]

Assert New Publication Entry with Excel Upload
     Element Should Contain    xpath=//a//span[text()='pub-HossainPub2019$']   pub-HossainPub2019$
