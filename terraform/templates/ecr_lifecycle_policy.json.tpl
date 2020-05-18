{
    "rules": [{
        "rulePriority":1,
        "description": "remove untagged images",
        "selection": {
            "tagStatus": "untagged",
            "countType": "sinceImagePushed",
            "countUnit": "days",
            "countNumber": 1
        },
        "action": {
            "type":"expire"
        }
    }]
}