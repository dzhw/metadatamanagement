*** Settings ***
Documentation     Automatically test data provider/publisher ready/required icons
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/login_resource.robot
Resource          ../../resources/project_management_resource.robot

*** Test Cases ***
Check different icons of a project from projectoverview
    Create Project  atestroboticons${BROWSER}
    Click on Cockpit Button
    Assign a dataprovider  dataprovider
    Select Metadata Checkbox From The List
    Switch To Status Tab
    Click Publisher Ready Checkbox for Data Packages
    Click Dataprovider Ready Checkbox for Data Packages
    Click Publisher Ready Checkbox for Surveys
    Click Publisher Ready Checkbox for Instruments
    Click Dataprovider Ready Checkbox for Datasets
    Click on Project Overview Button
    Assert Clipboard Double Check Icon  # doble check means both dataprovider ando publisher or only publisher is ready
    Assert Clipboard Single Check Icon  # single check means only dataprovider is ready
    Assert Metadata Icons  # here it checks only the metadata icons are available without any provider being ready
    Delete project by name  atestroboticons${BROWSER}
    Get back to german home page

*** Keywords ***
Click on Project Overview Button
   Click Element Through Tooltips   xpath=//a//span[contains(.,"Projektübersicht")]

Select Metadata Checkbox From The List
   @{MD_ITEMS}    Create List   survey   instruments   questions   dataSet    variables    publications
   :FOR   ${MD_DT}   IN  @{MD_ITEMS}
   \   Click Element Through Tooltips  xpath=//md-checkbox[@name="${MD_DT}"]

Assert Clipboard Double Check Icon
   @{MD_ITEMS}    Create List    dataPackages   surveys   instruments
   :FOR   ${MD_DT}   IN  @{MD_ITEMS}
   \    Page Should Contain Element   xpath=//tr[contains(.,"atestroboticons${BROWSER}")]//metadata-status[@type="'${MD_DT}'"]//md-icon[contains(@md-svg-src, ".clipboard-double-check.svg")]

Assert Clipboard Single Check Icon
   Page Should Contain Element   xpath=//tr[contains(.,"atestroboticons${BROWSER}")]//metadata-status[@type="'dataSets'"]//md-icon[contains(@md-svg-src, ".clipboard-check.svg")]

Assert Metadata Icons
   @{MD_ITEMS}    Create List    questions   variables   publications
   :FOR   ${MD_DT}   IN  @{MD_ITEMS}
   \    Page Should Contain Element   xpath=//tr[contains(.,"atestroboticons${BROWSER}")]//metadata-status[@type="'${MD_DT}'"]//md-icon[contains(@md-svg-src, ".clipboard.svg")]
