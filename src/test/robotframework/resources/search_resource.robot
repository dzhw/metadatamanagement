*** Settings ***
Documentation     Resource used by all search and view test cases
Library     ExtendedSelenium2Library
Resource    click_element_resource.robot
Variables   ../common_variables.yaml

*** Keywords ***
Search for
  [Arguments]    ${query}
  Input Text     id=query    ${query}

Click on search result by id
  [Arguments]    ${id}
  Wait Until Keyword Succeeds  5s  0.5s  Click Element Through Tooltips   xpath=//a//span[text()='${id}']

Click on questions tab
  Wait Until Keyword Succeeds  5s  0.5s  Click Element Through Tooltips  xpath=//md-pagination-wrapper/md-tab-item[4]

Click on variable tab
  Wait Until Keyword Succeeds  5s  0.5s  Click Element  xpath=//md-pagination-wrapper/md-tab-item[6]

Click on surveys tab
  Wait Until Keyword Succeeds  5s  0.5s  Click Element Through Tooltips  xpath=//md-pagination-wrapper/md-tab-item[2]

Click on publications tab
  Wait Until Keyword Succeeds  5s  0.5s  Click Element  xpath=//md-pagination-wrapper/md-tab-item[7]

Click on instruments tab
  Wait Until Keyword Succeeds  5s  0.5s  Click Element  xpath=//md-pagination-wrapper/md-tab-item[3]

Click on data set tab
  Wait Until Keyword Succeeds  5s  0.5s  Click Element  xpath=//md-pagination-wrapper/md-tab-item[5]
