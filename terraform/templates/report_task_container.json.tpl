 [
    {
      "dnsSearchDomains": null,
      "logConfiguration": {
        "logDriver": "awslogs",
        "secretOptions": null,
        "options": {
          "awslogs-group": "/ecs/report-task-${stage}",
          "awslogs-region": "eu-central-1",
          "awslogs-stream-prefix": "ecs"
        }
      },
      "entryPoint": null,
      "portMappings": [],
      "command": null,
      "linuxParameters": null,
      "cpu": ${cpu},
      "environment": [
        {
          "name": "JDK_JAVA_OPTIONS",
          "value": "-Xmx256m -Xms256m"
        },
        {
          "name": "SPRING_PROFILES_ACTIVE",
          "value": "cloud"
        },
        {
          "name": "vcap.services.mdm.credentials.endpoint",
          "value": "${mdm_endpoint}"
        },
        {
          "name": "vcap.services.mdm.credentials.password",
          "value": "${mdm_password}"
        },
        {
          "name": "vcap.services.mdm.credentials.username",
          "value": "${mdm_username}"
        }
      ],
      "resourceRequirements": null,
      "ulimits": null,
      "dnsServers": null,
      "mountPoints": [
        {
          "readOnly": false,
          "containerPath": "/tmp",
          "sourceVolume": "tmp"
        }
      ],
      "workingDirectory": null,
      "secrets": null,
      "dockerSecurityOptions": null,
      "memory": ${memory},
      "memoryReservation": ${memory},
      "volumesFrom": [],
      "stopTimeout": null,
      "image": "347729458675.dkr.ecr.eu-central-1.amazonaws.com/dzhw/report-task:latest-${stage}",
      "startTimeout": null,
      "firelensConfiguration": null,
      "dependsOn": null,
      "disableNetworking": null,
      "interactive": null,
      "healthCheck": null,
      "essential": true,
      "links": null,
      "hostname": null,
      "extraHosts": null,
      "pseudoTerminal": null,
      "user": null,
      "readonlyRootFilesystem": null,
      "dockerLabels": null,
      "systemControls": null,
      "privileged": null,
      "name": "report-task"
    }
  ]
