*** Settings ***
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening its study page
Resource    ../resources/search_resource.robot
Resource    ../resources/home_page_resource.robot
Force Tags  smoketest  noslowpoke

*** Test Cases ***
Looking for Absolventenpanel 2005 in german
  Search for  Absolventenpanel 2005
  Click on search result by id  stu-gra2005$
  Page Should Contain  Wirt­schafts- und Fi­nanz­kri­se
  [Teardown]  Get back to german home page

Looking for Graduate Panel 2005 in english
  [Setup]   Change language to english
  Search for  Graduate Panel 2005
  Click on search result by id  stu-gra2005$
  Page Should Contain  eco­nomic and fi­nan­cial cri­sis
  [Teardown]  Get back to german home page
