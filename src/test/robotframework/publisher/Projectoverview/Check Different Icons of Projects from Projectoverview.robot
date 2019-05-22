*** Settings ***
Documentation     Automatically test data provider/publisher ready/required icons
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/login_resource.robot
Resource          ../../resources/project_management_resource.robot

*** Test Cases ***
Check different icons of a project from projectoverview
    Create Project  testroboticons${BROWSER}
    Click on Cockpit Button
    Assign a dataprovider  dataprovider
    Select Metadata Checkbox From The List
    Switch To Status Tab
    Click Publisher Ready Checkbox for Studies
    Click Dataprovider Ready Checkbox for Studies
    Click Publisher Ready Checkbox for Surveys
    Click Publisher Ready Checkbox for Instruments
    Click Dataprovider Ready Checkbox for Datasets
    Click on Project Overview Button
    Click on Next Button
    Assert Clipboard Double Check Icon  # doble check means both dataprovider ando publisher or only publisher is ready
    Assert Clipboard Single Check Icon  # single check means only dataprovider is ready
    Assert Metadata Icons  # here it checks only the metadata icons are available without any provider being ready
    Delete project by name  testroboticons${BROWSER}
    Get back to german home page

*** Keywords ***
Click on Project Overview Button
   Click Element Through Tooltips   xpath=//a//span[contains(.,"Projektübersicht")]

Click on Next Button
   :FOR  ${INDEX}  IN RANGE  0   4
   \   Click Element Through Tooltips   xpath=//ul//li[@class="pagination-next ng-scope"]//a[contains(., "Weiter")]
   \   Sleep  1s

Select Metadata Checkbox From The List
   @{MD_ITEMS}    Create List   survey   instruments   questions   dataSet    variables    publications
   :FOR   ${MD_DT}   IN  @{MD_ITEMS}
   \   Click Element Through Tooltips  xpath=//md-checkbox[@name="${MD_DT}"]

Assert Clipboard Double Check Icon
   @{MD_ITEMS}    Create List    studies   surveys   instruments
   :FOR   ${MD_DT}   IN  @{MD_ITEMS}
   \    Page Should Contain Element   xpath=//tr[contains(.,"testroboticons${BROWSER}")]//metadata-status[@type="'${MD_DT}'"]//md-icon[@md-svg-src="assets/images/icons/a103f3a2.clipboard-double-check.svg"]

Assert Clipboard Single Check Icon
   Page Should Contain Element   xpath=//tr[contains(.,"testroboticons${BROWSER}")]//metadata-status[@type="'dataSets'"]//md-icon[@md-svg-src="assets/images/icons/c691f57d.clipboard-check.svg"]

Assert Metadata Icons
   @{MD_ITEMS}    Create List    questions   variables   publications
   :FOR   ${MD_DT}   IN  @{MD_ITEMS}
   \    Page Should Contain Element   xpath=//tr[contains(.,"testroboticons${BROWSER}")]//metadata-status[@type="'${MD_DT}'"]//md-icon[@md-svg-src="assets/images/icons/bbaf48e1.clipboard.svg"]


