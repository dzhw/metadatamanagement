*** Settings ***
Documentation     Tests the user experience of searching & finding a specific question of the Graduate Panel 2005
Resource    ../resources/search_resource.robot
Resource    ../resources/home_page_resource.robot
Force Tags  Long

*** Test Cases ***
Looking for Absolventenpanel 2005s Fragebogen Erste Welle in german
  Click on questions tab
  Search for  Wel­che Er­fah­run­gen haben Sie (bis­her) in Ihrer Aus­bil­dungs- bzw. Prak­ti­kums­pha­se ge­macht?
  Click on search result by id  que-gra2005-ins1-3.3$
  Page Should Contain  Bilder zur Frage
  [Teardown]  Get back to home page

Looking for Graduate Panel 2005s questionnaire first wave in english
  [Tags] noedge
  [Setup]   Change language to english
  Click on questions tab
  Search for  What experiences have you had (so far) during your training/internship?
  Click on search result by id  que-gra2005-ins1-3.3$
  Page Should Contain  Images of this Question
  [Teardown]  Get back to german home page
