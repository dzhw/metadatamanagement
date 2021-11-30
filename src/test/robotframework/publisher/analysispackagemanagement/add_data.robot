*** Settings ***
Documentation     Publisher Keyword to fill out the form
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Keywords ***
Move element
    [Arguments]    ${focusElement}    ${elementName}    ${button}
    set focus to element              //${focusElement}\[@name='${elementName}']
    Click Element Through Tooltips    ${button}

Open Create Page
    Click Element Through Tooltips    //ui-view/descendant::button[md-icon[text()='add']]

Create new analysis package
    Click Element Through Tooltips    //md-checkbox[@name = 'analysisPackage']
    Click Element Through Tooltips    //md-tab-item[contains(.,' Status ')]
    Click Element Through Tooltips    //button[contains(.,'Neu')]

Fill out details
    Input Text    name=titleDe                Test Analysepaket
    Input Text    name=titleEn                Test Analysis Package
    Input Text    name=annotationsDe          Dieses Analysepaket wurde von Robot automatisch erstellt.
    Input Text    name=annotationsEn          This analysis package was created automatically by Robot.

Fill out description
    Input Text    name=descriptionDe          Dieses Analysepaket wurde automatisch erstellt und überprüft die Eingabe valider Eigenschaften.
    Input Text    name=descriptionEn          This analysis package was created automatically and test the input of valid attributes.

Fill out authors
    input text      name=authorsFirstName_0       Heinz
    input text      name=authorsMiddleName_0      Schelm
    input text      name=authorsLastName_0        Erhardt
    Click Element Through Tooltips                //button[contains(@aria-label,'Klicken, um eine neue Autor:in diesem Analysepaket hinzuzufügen.')]
    input text      name=authorsFirstName_1       Gilda
    input text      name=authorsMiddleName_1      nope
    input text      name=authorsLastName_1        Zanetti
    Move element    input   authorsFirstName_1    //button[@id = 'move-authors-up-button']
    Move element    input   authorsFirstName_0    //button[@id = 'move-authors-down-button']

Fill out data curation
    input text      name=dataCuratorsFirstName_0     Allan
    input text      name=dataCuratorsMiddleName_0    Stewart
    input text      name=dataCuratorsLastName_0      Konigsberg
    Click Element Through Tooltips                   //button[contains(@aria-label,'Klicken, um eine neue Datenkurator:in diesem Analysepaket hinzuzufügen.')]
    input text      name=dataCuratorsFirstName_1     Mia
    input text      name=dataCuratorsMiddleName_1    nope
    input text      name=dataCuratorsLastName_1      Farrow
    Move element    input   dataCuratorsFirstName_1    //button[@id = 'move-dataCurators-up-button']
    Move element    input   dataCuratorsFirstName_0    //button[@id = 'move-dataCurators-down-button']

Add institutions
    Click Element Through Tooltips             //button[contains(@aria-label,'Klicken, um eine weitere Institution diesem Analysepaket hinzuzufügen.')]
    Input Text      name=institutionDe_0       DZHW ${BROWSER}
    Input Text      name=institutionEn_0       DZHW ${BROWSER}
    Click Element Through Tooltips             //button[contains(@aria-label,'Klicken, um eine weitere Institution diesem Analysepaket hinzuzufügen.')]
    Input Text      name=institutionDe_1       DZHW 2 ${BROWSER}
    Input Text      name=institutionEn_1       DZHW 2 ${BROWSER}
    Move element    input   institutionDe_1    //button[@id = 'move-institution-up-button']
    Move element    input   institutionDe_0    //button[@id = 'move-institution-down-button']

Add sponsors
    Click Element Through Tooltips         //button[contains(@aria-label,'Klicken, um eine weitere Geldgeber:in diesem Analysepaket hinzuzufügen.')]
    Input Text      name=sponsorDe_0       DZHW ${BROWSER}
    Input Text      name=sponsorEn_0       DZHW ${BROWSER}
    Click Element Through Tooltips         //button[contains(@aria-label,'Klicken, um eine weitere Geldgeber:in diesem Analysepaket hinzuzufügen.')]
    Input Text      name=sponsorDe_1       DZHW 2 ${BROWSER}
    Input Text      name=sponsorEn_1       DZHW 2 ${BROWSER}
    Move element    input   sponsorDe_1    //button[@id = 'move-sponsor-up-button']
    Move element    input   sponsorDe_0    //button[@id = 'move-sponsor-down-button']

Add license
    Input Text    name=license    This is free and unencumbered software released into the public domain...

Add tags
    Input Text                    //md-autocomplete[@md-search-text="tagSearchTextDe"]//input   Deutsche Tags Schlüsselwörter
    Run Keyword And Ignore Error  Click Element Through Tooltips                                //md-virtual-repeat-container//span[text()='Deutsche Tags Schlüsselwörter']
    Input Text                    //md-autocomplete[@md-search-text="tagSearchTextEn"]//input   English Days Keyword
    Run Keyword And Ignore Error  Click Element Through Tooltips                                //md-virtual-repeat-container//span[text()='English Days Keyword']

Add scripts
    Input Text                    name=scriptsTitleDe_0             DZHW ${BROWSER}
    Input Text                    name=scriptsTitleEn_0             DZHW ${BROWSER}
    set focus to element          scriptsUsedLanguage_0
    Run Keyword And Ignore Error  Click Element Through Tooltips    //md-virtual-repeat-container//span[text()='Abchasisch']
    set focus to element          scriptsSoftwarePackage_0
    Run Keyword And Ignore Error  Click Element Through Tooltips    //md-virtual-repeat-container//span[text()='R']
    input text                    scriptsSoftwarePackageVersion_0   1.0.0
