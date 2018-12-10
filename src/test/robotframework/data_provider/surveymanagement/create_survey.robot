*** Settings ***
Documentation     Data driven test of survey creation.
Suite Setup       Go To Survey Create Page
Suite Teardown    Close Survey Editor And Log Out
Force Tags        noslowpoke
Test Template     Survey Page With Empty Or Invalid Options Should Fail
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/login_resource.robot

*** Test Cases ***    GerTitle       EngTitle     Wave        FielPeriodStart    FieldPeriodEnd    GerSurvMeth    EngSurvMeth    GerPopTitle    EngPopTitle    GerPopDesc      EngPopDesc            GerSample    EngSample    NetSampleSize     ResponseRate
Empty German Title    ${Empty}       Something    1           01.05.2018         31.05.2018        Versuch        Trial          Alle           All            Na eben alle    Of course everyone    Alle         All          1400              10

Empty English Title
                      Irgendetwas    ${Empty}     1           01.05.2018         31.05.2018        Versuch        Trial          Alle           All            Na eben alle    Of course everyone    Alle         All          1400              10

Empty Wave            Irgendetwas    Something    ${Empty}    01.05.2018         31.05.2018        Versuch        Trial          Alle           All            Na eben alle    Of course everyone    Alle         All          1400              10

Empty German SurveyMethod
                      Irgendetwas    Something    1           01.05.2018         01.06.2018        ${Empty}       Trial          Alle           All            Na eben alle    Of course everyone    Alle         All          1400              10

Empty English SurveyMethod
                      Irgendetwas    Something    1           01.05.2018         01.06.2018        Versuch        ${Empty}       Alle           All            Na eben alle    Of course everyone    Alle         All          1400              10

Empty PopTitle        Irgendetwas    Something    1           01.05.2018         01.06.2018        Versuch        Trial          ${Empty}       ${Empty}       Na eben alle    Of course everyone    Alle         All          1400              10

Empty PopDescription
                      Irgendetwas    Something    1           01.05.2018         01.06.2018        Versuch        Trial          Alle           All            ${Empty}        ${Empty}              Alle         All          1400              10

Empty Sample          Irgendetwas    Something    1           01.05.2018         01.06.2018        Versuch        Trial          Alle           All            Na eben alle    Of course everyone    ${Empty}     ${Empty}     1400              10

Empty SampleSize      Irgendetwas    Something    1           01.05.2018         01.06.2018        Versuch        Trial          Alle           All            Na eben alle    Of course everyone    Alle         All          ${Empty}          10

Invalid SampleSize First
                      Irgendetwas    Something    1           01.05.2018         01.06.2018        Versuch        Trial          Alle           All            Na eben alle    Of course everyone    Alle         All          -5                10

Invalid SampleSize Second
                      Irgendetwas    Something    1           01.05.2018         01.06.2018        Versuch        Trial          Alle           All            Na eben alle    Of course everyone    Alle         All          Testsamplesize    10

Invalid ResponseRate
                      Irgendetwas    Something    1           01.05.2018         01.06.2018        Versuch        Trial          Alle           All            Na eben alle    Of course everyone    Alle         All          Testsamplesize    E

*** Keywords ***
Survey Page With Empty Or Invalid Options Should Fail
    [Arguments]    ${GTitle}    ${ETitle}    ${Wave}    ${FieldPeriodStart}    ${FieldPeriodEnd}    ${GSurveyMethod}
    ...    ${ESurveyMethod}    ${GPopTitle}    ${EPopTitle}    ${GPopDesc}    ${EPopDesc}    ${GSample}
    ...    ${ESample}    ${NetSampleSize}    ${ResponseRate}
    Pass Execution If    '${BROWSER}' == 'ie'    Survey Creation not possible in IE
    Clear Element Text    name=titleDe
    Input Text    name=titleDe    ${GTitle}
    Clear Element Text    name=titleEn
    Input Text    name=titleEn    ${ETitle}
    Clear Element Text    name=wave
    Input Text    name=wave    ${Wave}
    Clear Element Text    xpath=//md-datepicker[@name='fieldPeriodStart']//input
    Input Text    xpath=//md-datepicker[@name='fieldPeriodStart']//input    ${FieldPeriodStart}
    Clear Element Text    xpath=//md-datepicker[@name='fieldPeriodEnd']//input
    Input Text    xpath=//md-datepicker[@name='fieldPeriodEnd']//input    ${FieldPeriodEnd}
    Clear Element Text    name=surveyMethodDe
    Input Text    name=surveyMethodDe    ${GSurveyMethod}
    Clear Element Text    name=surveyMethodEn
    Input Text    name=surveyMethodEn    ${ESurveyMethod}
    Choose Quantitative Daten As Data Type
    Clear Element Text    name=populationTitleDe
    Input Text    name=populationTitleDe    ${GPopTitle}
    Clear Element Text    name=populationTitleEn
    Input Text    name=populationTitleEn    ${EPopTitle}
    Clear Element Text    name=populationDescriptionDe
    Input Text    name=populationDescriptionDe    ${GPopDesc}
    Clear Element Text    name=populationDescriptionEn
    Input Text    name=populationDescriptionEn    ${EPopDesc}
    Clear Element Text    name=sampleDe
    Input Text    name=sampleDe    ${GSample}
    Clear Element Text    name=sampleEn
    Input Text    name=sampleEn    ${ESample}
    Clear Element Text    name=sampleSize
    Input Text    name=sampleSize    ${NetSampleSize}
    Clear Element Text    name=responseRate
    Input Text    name=responseRate    ${ResponseRate}
    Save Changes
    Page Should Contain    Die Erhebung wurde nicht gespeichert, weil es noch ungültige Felder gibt!

Go To Survey Create Page
    Pass Execution If    '${BROWSER}' == 'ie'    Survey Creation not possible in IE
    Login as dataprovider
    Select project by name    robotproject
    Wait Until Angular Ready    6s
    Click on surveys tab
    Click Element Through Tooltips    xpath=//ui-view/descendant::button[md-icon[text()='add']]

Close Survey Editor And Log Out
    Pass Execution If    '${BROWSER}' == 'ie'    Survey Creation not possible in IE
    Click Element Through Tooltips    xpath=//md-icon[text()='close']
    Get back to german home page
    Click Element Through Tooltips    xpath=//button[text()='Ja']
    #Probleme mit allen anderen Optionen, merkwürdiges Resultat wenn zuschnell ausgeloggt wird.
    Sleep    1s
    Click Element Through Tooltips    xpath=//button[@id='logout']

Choose Quantitative Daten As Data Type
    Click Element Through Tooltips    xpath=//md-select[@name = 'dataType']
    Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., 'Quantitative Daten')]
