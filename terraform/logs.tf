# logs.tf

# Set up CloudWatch group and log stream and retain logs for 30 days
resource "aws_cloudwatch_log_group" "log_group" {
  count             = length(var.stages)
  name              = "/ecs/metadatamanagement-${var.stages[count.index]}"
  retention_in_days = 30

  tags = {
    Name = "metadatamanagement-${var.stages[count.index]}-log-group"
  }
}

resource "aws_cloudwatch_log_group" "log_group_dataset_report" {
  count             = length(var.stages)
  name              = "/ecs/dataset-report-task-${var.stages[count.index]}"
  retention_in_days = 30

  tags = {
    Name = "dataset-report-task-${var.stages[count.index]}-log-group"
  }
}

resource "aws_cloudwatch_log_stream" "cb_log_stream" {
  count          = length(var.stages)
  name           = "metadatamanagement-${var.stages[count.index]}-log-stream"
  log_group_name = aws_cloudwatch_log_group.log_group[count.index].name
}

resource "aws_cloudwatch_log_stream" "cb_log_stream_dataset_report" {
  count          = length(var.stages)
  name           = "dataset-report-task-${var.stages[count.index]}-log-stream"
  log_group_name = aws_cloudwatch_log_group.log_group_dataset_report[count.index].name
}
