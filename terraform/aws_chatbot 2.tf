# aws_chatbot
# setup cloudwatch alarms which send the alarm state of the stage to our slack channel
resource "aws_sns_topic" "mdm_devops_slack_channel" {
  name         = "mdmSlackChannel"
  display_name = "MDM Slack Channel"
}

resource "aws_sns_topic_subscription" "aws_chatbot_subscription" {
  topic_arn                       = aws_sns_topic.mdm_devops_slack_channel.arn
  protocol                        = "https"
  endpoint                        = "https://global.sns-api.chatbot.amazonaws.com"
  confirmation_timeout_in_minutes = 1
  endpoint_auto_confirms          = false
}

resource "aws_cloudwatch_metric_alarm" "alert_unhealthy_containers" {
  count               = length(var.stages)
  alarm_name          = "alert_unhealthy_containers_${var.stages[count.index]}"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = 1
  datapoints_to_alarm = 1
  metric_name         = "UnHealthyHostCount"
  namespace           = "AWS/ApplicationELB"
  treat_missing_data  = "breaching"
  dimensions = {
    "LoadBalancer" = aws_alb.load_balancer.arn_suffix
    "TargetGroup"  = aws_alb_target_group.mdm[count.index].arn_suffix
  }
  period                    = 300
  statistic                 = "Average"
  threshold                 = 0
  alarm_description         = "Send an alert if there are unhealthy web containers running."
  insufficient_data_actions = []
  ok_actions                = [aws_sns_topic.mdm_devops_slack_channel.arn]
  alarm_actions             = [aws_sns_topic.mdm_devops_slack_channel.arn]
}

resource "aws_cloudwatch_metric_alarm" "alert_number_of_healthy_containers_too_low" {
  count               = length(var.stages)
  alarm_name          = "alert_number_of_healthy_containers_too_low_${var.stages[count.index]}"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = 1
  datapoints_to_alarm = 1
  metric_name         = "HealthyHostCount"
  namespace           = "AWS/ApplicationELB"
  treat_missing_data  = "breaching"
  dimensions = {
    "LoadBalancer" = aws_alb.load_balancer.arn_suffix
    "TargetGroup"  = aws_alb_target_group.mdm[count.index].arn_suffix
  }
  period                    = 120
  statistic                 = "Average"
  threshold                 = 3
  alarm_description         = "Send an alert if there are less than 3 web containers running."
  insufficient_data_actions = []
  ok_actions                = [aws_sns_topic.mdm_devops_slack_channel.arn]
  alarm_actions             = [aws_sns_topic.mdm_devops_slack_channel.arn]
}
