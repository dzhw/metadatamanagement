*** Settings ***
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening a variable page
Resource    ../resources/search_resource.robot
Resource    ../resources/home_page_resource.robot


*** Test Cases ***
Looking for Absolventenpanel 2005s individual data bachelor in german
  Click on variable tab
  Search for  Absolventenpanel 2005 Personendatensatz Bachelor studienberatung
  Click on search result by id  var-gra2005-ds3-astu11z$
  Page Should Contain  ordinal
  [Teardown]  Get back to german home page

Looking for Graduate Panel 2005s individual data bachelor in english
  [Setup]   Change language to english
  Click on variable tab
  Search for  graduate panel individual data bachelor study guidance
  Click on search result by id  var-gra2005-ds3-astu11z$
  Page Should Contain  ordinal
  [Teardown]  Get back to german home page
