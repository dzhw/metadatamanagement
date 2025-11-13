# load_balancer.tf

# use a shared loadbalancer for all stages
resource "aws_alb" "load_balancer" {
  name            = "metadatamanagement"
  subnets         = [aws_subnet.public_1.id, aws_subnet.public_2.id]
  security_groups = [aws_security_group.load_balancer.id]
  ip_address_type = "ipv4"
  # load-balancer must not be deleted because there are DNS-aliases pointing to its name
  enable_deletion_protection = true
  enable_http2               = true
}

# use different target groups per stage
resource "aws_alb_target_group" "mdm" {
  count       = length(var.stages)
  name        = var.stages[count.index]
  port        = var.container_port
  protocol    = "HTTP"
  vpc_id      = aws_vpc.main.id
  slow_start  = 60
  target_type = "ip"

  health_check {
    healthy_threshold   = 3
    interval            = 120
    protocol            = "HTTP"
    matcher             = "200"
    timeout             = 5
    path                = var.health_check_path
    unhealthy_threshold = 2
  }
}

# As fallback redirect all https traffic from the ALB to the prod target group
resource "aws_alb_listener" "front_end" {
  load_balancer_arn = aws_alb.load_balancer.id
  port              = 443
  protocol          = "HTTPS"
  certificate_arn   = "arn:aws:acm:eu-central-1:347729458675:certificate/23a17421-8267-4154-937a-ffdcb40ba6e1"

  default_action {
    target_group_arn = aws_alb_target_group.mdm[2].id
    type             = "forward"
  }
}

# seperate the traffic to the different stages by host header
resource "aws_alb_listener_rule" "static" {
  count        = length(var.stages)
  listener_arn = aws_alb_listener.front_end.arn
  priority     = 100 - count.index

  action {
    type             = "forward"
    target_group_arn = aws_alb_target_group.mdm[count.index].id
  }

  condition {
    host_header {
      values = [var.hostnames[count.index]]
    }
  }
}

# Redirect all http traffic to https
resource "aws_alb_listener" "redirect" {
  load_balancer_arn = aws_alb.load_balancer.id
  port              = 80
  protocol          = "HTTP"

  default_action {
    type = "redirect"
    redirect {
      port        = "443"
      protocol    = "HTTPS"
      status_code = "HTTP_301"
    }
  }
}
