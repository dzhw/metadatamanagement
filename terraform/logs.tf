# logs.tf

# Set up CloudWatch group and log stream and retain logs for 30 days
resource "aws_cloudwatch_log_group" "log_group" {
  count             = 3
  name              = "/ecs/metadatamanagement-${var.stages[count.index]}"
  retention_in_days = 30

  tags = {
    Name = "metadatamanagement-${var.stages[count.index]}-log-group"
  }
}

resource "aws_cloudwatch_log_stream" "cb_log_stream" {
  count          = 3
  name           = "metadatamanagement-${var.stages[count.index]}-log-stream"
  log_group_name = aws_cloudwatch_log_group.log_group[count.index].name
}
