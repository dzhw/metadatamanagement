*** Settings ***
Documentation     Automatically test data provider/publisher ready/required icons
Metadata          Info on data    This test suite creates a temporary test project with the name "${PROJECT_NAME}${BROWSER}" which is automatically deleted afterwards
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/login_resource.robot
Resource          ../../resources/project_management_resource.robot

*** Variables ***
# project name has to start with the letter "a" to make sure it appears on the first page of project overview
${PROJECT_NAME}  atemprobotcheckicons

*** Test Cases ***
Check different icons of a project from projectoverview
    ${created}  Run Keyword and return status  Create Project  ${PROJECT_NAME}${BROWSER}
    Run Keyword If  ${created}==False  Fail  Could not create new project '${PROJECT_NAME}${BROWSER}'
    Click on Cockpit Button
    Assign a dataprovider  dataprovider
    Select Metadata Checkbox From The List
    Switch To Status Tab
    Click Publisher Ready Checkbox  dataPackages
    Click Dataprovider Ready Checkbox  dataPackages
    Click Publisher Ready Checkbox  surveys
    Click Publisher Ready Checkbox  instruments
    Click Dataprovider Ready Checkbox  dataSets
    Click on Project Overview Button
    Assert Clipboard Double Check Icon  # double check means both dataprovider and publisher or only publisher is ready
    Assert Clipboard Single Check Icon  # single check means only dataprovider is ready
    Assert Metadata Icons  # here it checks only the metadata icons are available without any provider being ready
    Delete project by name  ${PROJECT_NAME}${BROWSER}
    Get back to german home page

*** Keywords ***
Click on Project Overview Button
   Click Element Through Tooltips   xpath=//a//span[contains(.,"Projektübersicht")]

Select Metadata Checkbox From The List
   @{MD_ITEMS}    Create List   survey   instruments   questions   dataSet    variables    publications
   FOR   ${MD_DT}   IN  @{MD_ITEMS}
        Click Element Through Tooltips  xpath=//md-checkbox[@name="${MD_DT}"]
   END

Assert Clipboard Double Check Icon
   @{MD_ITEMS}    Create List    dataPackages   surveys   instruments
   FOR   ${MD_DT}   IN  @{MD_ITEMS}
        Page Should Contain Element   xpath=//tr//td[contains(.,"${PROJECT_NAME}${BROWSER}")]//parent::tr//td//metadata-status[@type="'${MD_DT}'"]//md-icon[contains(concat(' ', @md-svg-src, ' '), "clipboard-double-check.svg")]
   END

Assert Clipboard Single Check Icon
   Page Should Contain Element   xpath=//tr//td[contains(.,"${PROJECT_NAME}${BROWSER}")]//parent::tr//td//metadata-status[@type="'dataSets'"]//md-icon[contains(concat(' ', @md-svg-src, ' '), "clipboard-check.svg")]

Assert Metadata Icons
   @{MD_ITEMS}    Create List    questions   variables   publications
   FOR   ${MD_DT}   IN  @{MD_ITEMS}
        Page Should Contain Element   xpath=//tr//td[contains(.,"${PROJECT_NAME}${BROWSER}")]//parent::tr//td//metadata-status[@type="'${MD_DT}'"]//md-icon[contains(concat(' ', @md-svg-src, ' '), "clipboard.svg")]
   END
