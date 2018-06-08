*** Settings ***
Documentation     Tests if login is possible
Resource      ../resources/home_page_resource.robot
Resource      ../resources/search_resource.robot
Resource      ../resources/login_resource.robot
Default Tags  smoketest


*** Test Cases ***
Login as dataprovider
  Login as dataprovider
  Click Element Through Tooltips  xpath=//button[contains(.,'abmelden')]
