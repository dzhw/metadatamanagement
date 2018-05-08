*** Settings ***
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening a publication page
Resource    ../resources/search_resource.robot
Resource    ../resources/home_page_resource.robot

*** Test Cases ***
Looking for Bildungsherkunft und Promotionen
  Click on publications tab
  Search for  Bildungsherkunft und Promotionen
  Click on search result by id  pub-Jaksztat.2014$
  Page Should Contain  http://www.zfs-online.org/index.php/zfs/article/view/3175/2712
  [Teardown]  Get back to home page

Looking for Graduate Panel 2005s individual data bachelor in english
  [Setup]   Change language to english
  Click on publications tab
  Search for  Bildungsherkunft und Promotionen
  Click on search result by id  pub-Jaksztat.2014$
  Page Should Contain  http://www.zfs-online.org/index.php/zfs/article/view/3175/2712
  [Teardown]  Get back to german home page

*** Keywords ***
Click on publications tab
  Wait Until Keyword Succeeds  5s  1s  Click Element  xpath=//md-pagination-wrapper/md-tab-item[7]
