ECHO OFF



::start cmd /k "cd ..\..\.. & robot -P src\test\robotframework\libs -d target\test\robotframework\logs -v BROWSER:firefox .\src\test\robotframework\public_user\"
start cmd /k "cd ..\..\.. & robot -P src\test\robotframework\libs -d target\test\robotframework\logs -v BROWSER:chrome -t "Robotframework.Public User.Publicationmanagement.Search And View Publication.*" .\src\test\robotframework"
