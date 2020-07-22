# roles.tf

# default task execution role policy
data "aws_iam_policy_document" "ecs_task_assume_role_policy" {
  version = "2012-10-17"
  statement {
    sid     = ""
    effect  = "Allow"
    actions = ["sts:AssumeRole"]

    principals {
      type        = "Service"
      identifiers = ["ecs-tasks.amazonaws.com"]
    }
  }
}

# Role for running ECS tasks, is capable of starting other tasks
resource "aws_iam_role" "mdm_task_execution_role" {
  name               = "mdmTaskExecutionRole"
  assume_role_policy = data.aws_iam_policy_document.ecs_task_assume_role_policy.json
  description        = "Allows ECS tasks to call AWS services on your behalf."
  tags = {
    app = "metadatamanagement"
  }
}
