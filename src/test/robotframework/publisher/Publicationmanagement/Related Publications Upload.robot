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
   Assert New Publication Entry with Excel Upload

*** Keywords ***
Upload Excel file
    press key  xpath=//input[@type='file' and @ngf-select='uploadRelatedPublications($file)'][1]   ${CURDIR}/publicationdata/relatedPublications.xls

Assert New Publication Entry with Excel Upload
     Wait Until Element Is Visible   xpath=//a//span[text()='pub-HossainPub2019$']  # to ensure enough time for uploading and next test
     Element Should Contain    xpath=//a//span[text()='pub-HossainPub2019$']   pub-HossainPub2019$
