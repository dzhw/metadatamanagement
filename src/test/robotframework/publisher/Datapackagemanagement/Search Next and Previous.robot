*** Settings ***
Documentation     Navigating Next and Previous Search Results
Resource          ../../resources/search_resource.robot
Resource          ../../resources/click_element_resource.robot

*** Test Cases ***
Logged in Publisher Click On First Search Result
    Click on data package tab
    Click on first search result
    Click on Next Content
    Click on Previous Content

*** Keywords ***
Click on Next Content
    Click Element Through Tooltips    xpath=//md-icon[@md-font-set='material-icons'][contains(.,'navigate_next')]

Click on Previous Content
    Click Element Through Tooltips    xpath=//md-icon[@md-font-set='material-icons'][contains(.,'navigate_before')]
