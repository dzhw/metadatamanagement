*** Settings ***
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening the questionnaire
Resource    ../resources/search_resource.robot
Resource    ../resources/home_page_resource.robot
Force Tags  smoketest

*** Test Cases ***
Looking for Absolventenpanel 2005s Fragebogen Erste Welle in german
  Click on instruments tab
  Search for  Absolventenpanel 2005 Fragebogen Erste Welle
  Click on search result by id  ins-gra2005-ins1$
  Page Should Contain  gra2005_W1_Questionnaire_de.pdf
  [Teardown]  Get back to home page

Looking for Graduate Panel 2005s questionnaire first wave in english
  [Tags]  noslowpoke
  [Setup]   Change language to english
  Click on instruments tab
  Search for  Graduate Panel 2005 Questionnaire First Wave
  Click on search result by id  ins-gra2005-ins1$
  Page Should Contain  gra2005_W1_Questionnaire_en.pdf
  [Teardown]  Get back to german home page
