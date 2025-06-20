[
    {
      "dnsSearchDomains": null,
      "logConfiguration": {
        "logDriver": "awslogs",
        "secretOptions": null,
        "options": {
          "awslogs-group": "/ecs/metadatamanagement-${stage}",
          "awslogs-region": "eu-central-1",
          "awslogs-stream-prefix": "ecs"
        }
      },
      "entryPoint": [],
      "portMappings": [
        {
          "hostPort": ${port},
          "protocol": "tcp",
          "containerPort": ${port}
        }
      ],
      "command": [],
      "linuxParameters": null,
      "cpu": ${cpu},
      "environment": [
        {
          "name": "cf_instance_index",
          "value": "0"
        },
        {
          "name": "JAVA_TOOL_OPTIONS",
          "value": "-Xss256k -XX:+UseG1GC -XX:+UseStringDeduplication"
        },
        {
          "name": "SPRING_PROFILES_ACTIVE",
          "value": "${stage}"
        },
        {
          "name": "vcap_services_dara_pid_credentials_endpoint",
          "value": "${dara_pid_endpoint}"
        },
        {
          "name": "vcap_services_dara_pid_credentials_password",
          "value": "${dara_pid_password}"
        },
        {
          "name": "vcap_services_dara_pid_credentials_username",
          "value": "${dara_pid_username}"
        },
        {
          "name": "vcap_services_datacite_credentials_endpoint",
          "value": "${datacite_endpoint}"
        },
        {
          "name": "vcap_services_datacite_credentials_password",
          "value": "${datacite_password}"
        },
        {
          "name": "vcap_services_datacite_credentials_username",
          "value": "${datacite_username}"
        },
        {
          "name": "vcap_services_elastic_credentials_sslUri",
          "value": "${elasticsearch_uri}"
        },
        {
          "name": "vcap_services_email_credentials_hostname",
          "value": "${email_hostname}"
        },
        {
          "name": "vcap_services_email_credentials_password",
          "value": "${email_password}"
        },
        {
          "name": "vcap_services_email_credentials_username",
          "value": "${email_username}"
        },
        {
          "name": "vcap_services_mongodb_credentials_uri",
          "value": "${mongodb_uri}"
        },
        {
          "name": "vcap_services_mongodb_cacert",
          "value": "${mongodb_ca_cert}"
        },
        {
          "name": "vcap_services_rabbitmq_credentials_uri",
          "value": "${rabbitmq_uri}"
        },
        {
          "name": "vcap_services_seo4ajax_sitetoken",
          "value": "${site_token}"
        },
        {
          "name": "vcap_services_tweet_credentials_consumerkey",
          "value": "${tweet_consumer_key}"
        },
        {
          "name": "vcap_services_tweet_credentials_consumersecret",
          "value": "${tweet_consumer_secret}"
        },
        {
          "name": "vcap_services_tweet_credentials_oauthtoken",
          "value": "${tweet_oauth_token}"
        },
        {
          "name": "vcap_services_tweet_credentials_oauthtokensecret",
          "value": "${tweet_oauth_token_secret}"
        }
      ],
      "resourceRequirements": null,
      "ulimits": null,
      "dnsServers": null,
      "mountPoints": [
        {
          "readOnly": null,
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
      "image": "347729458675.dkr.ecr.eu-central-1.amazonaws.com/dzhw/metadatamanagement:latest-${stage}",
      "startTimeout": null,
      "firelensConfiguration": null,
      "dependsOn": null,
      "disableNetworking": null,
      "interactive": null,
      "healthCheck": null,
      "essential": true,
      "links": [],
      "hostname": null,
      "extraHosts": null,
      "pseudoTerminal": null,
      "user": null,
      "readonlyRootFilesystem": null,
      "dockerLabels": null,
      "systemControls": null,
      "privileged": null,
      "name": "metadatamanagement-${stage}-worker"
    }
  ]
