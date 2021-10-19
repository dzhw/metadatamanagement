# elastic_container_service.tf

resource "aws_ecs_cluster" "cluster" {
  count = length(var.stages)
  name  = var.stages[count.index]
  
  capacity_providers = ["FARGATE", "FARGATE_SPOT"]
  default_capacity_provider_strategy {
    capacity_provider = "FARGATE_SPOT"
    weight            = 1
    base              = 0
  }
}

data "template_file" "web_container" {
  count    = length(var.stages)
  template = file("./templates/web_container.json.tpl")

  vars = {
    stage             = var.stages[count.index]
    port              = var.container_port
    cpu               = var.container_cpu
    memory            = var.container_memory
    dara_endpoint     = var.dara_credentials[count.index].endpoint
    dara_username     = var.dara_credentials[count.index].username
    dara_password     = var.dara_credentials[count.index].password
    elasticsearch_uri = var.elasticsearch_uris[count.index]
    rabbitmq_uri      = var.rabbitmq_uris[count.index]
    mongodb_uri       = var.mongodb_uris[count.index]
    mongodb_ca_cert   = var.mongodb_ca_certs[count.index]
    email_hostname    = var.email_credentials[count.index].hostname
    email_password    = var.email_credentials[count.index].password
    email_username    = var.email_credentials[count.index].username
    site_token        = var.seo4ajax_tokens[count.index].site_token
  }
}

resource "aws_ecs_task_definition" "metadatamanagement_web" {
  count              = length(var.stages)
  family             = "metadatamanagement-${var.stages[count.index]}-web"
  execution_role_arn = aws_iam_role.mdm_task_execution_role.arn
  # grant this container the right to start other fargate tasks (e.g. report-task)
  task_role_arn            = aws_iam_role.mdm_task_execution_role.arn
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = var.container_cpu
  memory                   = var.container_memory
  container_definitions    = data.template_file.web_container[count.index].rendered
  volume {
    name = "tmp"
  }
}

data "template_file" "report_task_container" {
  count    = length(var.stages)
  template = file("./templates/report_task_container.json.tpl")

  vars = {
    stage        = var.stages[count.index]
    cpu          = var.container_cpu
    memory       = var.container_memory
    mdm_endpoint = var.task_api_credentials[count.index].endpoint
    mdm_username = var.task_api_credentials[count.index].username
    mdm_password = var.task_api_credentials[count.index].password
  }
}

resource "aws_ecs_task_definition" "report_task" {
  count                    = length(var.stages)
  family                   = "report-task-${var.stages[count.index]}"
  execution_role_arn       = aws_iam_role.mdm_task_execution_role.arn
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = var.container_cpu
  memory                   = var.container_memory
  container_definitions    = data.template_file.report_task_container[count.index].rendered
  volume {
    name = "tmp"
  }
}

data "template_file" "worker_container" {
  count    = length(var.stages)
  template = file("./templates/worker_container.json.tpl")

  vars = {
    stage             = var.stages[count.index]
    port              = var.container_port
    cpu               = var.container_cpu
    memory            = var.container_memory
    dara_endpoint     = var.dara_credentials[count.index].endpoint
    dara_username     = var.dara_credentials[count.index].username
    dara_password     = var.dara_credentials[count.index].password
    elasticsearch_uri = var.elasticsearch_uris[count.index]
    rabbitmq_uri      = var.rabbitmq_uris[count.index]
    mongodb_uri       = var.mongodb_uris[count.index]
    mongodb_ca_cert   = var.mongodb_ca_certs[count.index]
    email_hostname    = var.email_credentials[count.index].hostname
    email_password    = var.email_credentials[count.index].password
    email_username    = var.email_credentials[count.index].username
    site_token        = var.seo4ajax_tokens[count.index].site_token
  }
}

resource "aws_ecs_task_definition" "metadatamanagement_worker" {
  count              = length(var.stages)
  family             = "metadatamanagement-${var.stages[count.index]}-worker"
  execution_role_arn = aws_iam_role.mdm_task_execution_role.arn
  # grant this container the right to start other fargate tasks (e.g. report-task)
  task_role_arn            = aws_iam_role.mdm_task_execution_role.arn
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = var.container_cpu
  memory                   = var.container_memory
  container_definitions    = data.template_file.worker_container[count.index].rendered
  volume {
    name = "tmp"
  }
}

resource "aws_ecs_service" "metadatamanagement" {
  count   = length(var.stages)
  name    = "metadatamanagement"
  cluster = aws_ecs_cluster.cluster[count.index].id
  task_definition = aws_ecs_task_definition.metadatamanagement_web[count.index].arn
  desired_count   = var.container_count

  network_configuration {
    security_groups  = [aws_security_group.ecs_web_tasks.id]
    subnets          = [aws_subnet.private_1.id, aws_subnet.private_2.id]
    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = aws_alb_target_group.mdm[count.index].id
    container_name   = "metadatamanagement-${var.stages[count.index]}-web"
    container_port   = var.container_port
  }

  # use spot instances on dev and test stage
  capacity_provider_strategy {
    capacity_provider = "FARGATE${count.index != 2 ? "_SPOT" : ""}"
    weight            = 1
    base              = 0
  }

  depends_on = [aws_alb_listener.front_end]
}

resource "aws_ecs_service" "metadatamanagement_worker" {
  count   = length(var.stages)
  name    = "metadatamanagement-worker"
  cluster = aws_ecs_cluster.cluster[count.index].id
  # todo use test and prod task definitions
  task_definition = aws_ecs_task_definition.metadatamanagement_worker[count.index].arn
  desired_count   = 1

  network_configuration {
    security_groups  = [aws_security_group.ecs_web_tasks.id]
    subnets          = [aws_subnet.private_1.id, aws_subnet.private_2.id]
    assign_public_ip = false
  }

  # use spot instances on dev and test stage
  capacity_provider_strategy {
    capacity_provider = "FARGATE${count.index != 2 ? "_SPOT" : ""}"
    weight            = 1
    base              = 0
  }
}
