*** Settings ***
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening the survey page of the first wave for bachelor graduates
Resource          ../resources/search_resource.robot
Resource          ../resources/home_page_resource.robot
Forced Tags  smoketest

*** Test Cases ***
Looking for Absolventenpanel 2005s first wave survey in german
  Click on surveys tab
  Search for  Absolventenpanel 2005 Erste Welle
  Click on search result by id  sur-gra2005-sy3$
  Page Should Contain  n = 1.622
  [Teardown]  Get back to home page

Looking for Graduate Panel 2005s first wave survey in english
  [Tags] noedge
  [Setup]   Change language to english
  Click on surveys tab
  Search for  DZHW Graduate Panel 2005 First Wave
  Click on search result by id  sur-gra2005-sy3$
  Page Should Contain  n = 1,622
  [Teardown]  Get back to german home page
