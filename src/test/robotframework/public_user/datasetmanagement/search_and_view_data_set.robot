*** Settings ***
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening a data set page
Resource    ../resources/search_resource.robot
Resource    ../resources/home_page_resource.robot


*** Test Cases ***
Looking for Absolventenpanel 2005s individual data bachelor in german
  Click on data set tab
  Search for  Absolventenpanel 2005 Personendatensatz Bachelor
  Click on search result by id  dat-gra2005-ds3$
  Page Should Contain  Kann nach Ab­schluss eines Da­ten­nut­zungs­ver­trags her­un­ter­ge­la­den wer­den
  [Teardown]  Get back to german home page

Looking for Graduate Panel 2005s individual data bachelor in english
  [Setup]   Change language to english
  Click on data set tab
  Search for  Graduate Panel 2005 Questionnaire First Wave
  Click on search result by id  dat-gra2005-ds3$
  Page Should Contain  can be down­loaded after the con­clu­sion of a data use agree­ment
  [Teardown]  Get back to german home page
