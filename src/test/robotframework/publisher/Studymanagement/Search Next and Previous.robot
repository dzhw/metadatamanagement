*** Settings ***
Documentation     Navigating Next and Previous Search Results
Resource          ../../resources/search_resource.robot
Resource          ../../resources/click_element_resource.robot

*** Test Cases ***
Logged in Publisher Click On First Search Result
    Click on study tab
    Click on First Search Result
    Click on Next Content
    Click on Previous Content

*** Keywords ***
Click on First Search Result
    Click Element Through Tooltips    xpath=//a[@class='fdz-search-result'][1]

Click on Next Content
    Click Element Through Tooltips    xpath=//md-icon[@md-font-set='material-icons'][contains(.,'navigate_next')]

Click on Previous Content
    Click Element Through Tooltips    xpath=//md-icon[@md-font-set='material-icons'][contains(.,'navigate_before')]
