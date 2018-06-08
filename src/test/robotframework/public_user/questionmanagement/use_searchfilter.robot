*** Settings ***
Documentation     Tests the user experience of using a survey filter for finding a connected question
Resource    ../resources/search_resource.robot
Resource    ../resources/home_page_resource.robot
Default Tags  Long

*** Test Cases ***
Use of Searchfilters to find a question
  Click on questions tab
  Activate Filter by name  Erhebung
  Choose Filter Option by id  sur-gra2005-sy4$
  Click on search result by id  que-gra2005-ins2-1.1$
  Page Should Contain  Verbundene Objekte
  [Teardown]  Get back to home page
