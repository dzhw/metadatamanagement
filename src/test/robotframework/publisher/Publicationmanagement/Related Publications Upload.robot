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
   Press Key  xpath=//input[@type='file' and @ngf-select='uploadRelatedPublications($file)'][1]   ${CURDIR}/publicationdata/relatedPublications.xlsx

Close The Toast Message for upload
   Wait Until Page Contains Element  xpath=//md-toast//span[contains(., "Upload von 199 Publikationen")]  timeout=240s
   Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]

Assert New Publication Entry with Excel Upload
   Page Should Contain Element   xpath=//related-publication-search-result[contains(.,"No Place Like Home?")]
