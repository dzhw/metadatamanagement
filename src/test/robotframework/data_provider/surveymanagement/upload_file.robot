*** Settings ***
Documentation     Tests the upload on the survey edit site.
Resource    ../resources/home_page_resource.robot
Resource    ../resources/search_resource.robot
Resource    ../resources/login_resource.robot
Default Tags  chromeonly


*** Test Cases ***
Fileupload in survey Editor
  Login as dataprovider
  Select project by name  fileuploadproject
  Click on surveys tab
  Click on search result by id    sur-fileuploadproject-sy1$
  Click Element Through Tooltips          xpath=//ui-view//a/md-icon[text()='mode_edit']
  Choose File  xpath=//input[@type="file"][@ngf-select="ctrl.saveResponseRateImageDe($file)"][1]    ${CURDIR}/images/1_responserate_de.svg
# Sleep is needed as the save button is too soon available and the upload wouldn't be done.
  Sleep  2s
  Click Element Through Tooltips  xpath=//md-card//button[contains(.,"save")]
  Click Element Through Tooltips  xpath=//md-toolbar//a[span[text()="1"]]
  Page Should Contain Element  xpath=//md-content//a[@ng-href="/public/files/surveys/sur-fileuploadproject-sy1$/1_responserate_de"]
  Click Element Through Tooltips          xpath=//ui-view//a/md-icon[text()='mode_edit']
  Click Element Through Tooltips  xpath=//md-card//button[contains(.,"delete_forever")]
#Same Reason as above
  Sleep  2s
  Click Element Through Tooltips  xpath=//md-card//button[contains(.,"save")]
  [Teardown]  Get back to home page
