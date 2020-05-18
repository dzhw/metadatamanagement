# security_groups.tf

# Loadbalancer Security Group
resource "aws_security_group" "load_balancer" {
  name        = "load-balancer-security-group"
  description = "controls access to the load balancer, http will be redirected to https"
  vpc_id      = aws_vpc.main.id

  ingress {
    protocol    = "tcp"
    from_port   = 80
    to_port     = 80
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    protocol    = "tcp"
    from_port   = 443
    to_port     = 443
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Task Container Security Group
resource "aws_security_group" "ecs_web_tasks" {
  name        = "web-container-security-group"
  description = "Allow inbound access from the load balancer"
  vpc_id      = aws_vpc.main.id

  ingress {
    protocol    = "tcp"
    from_port   = var.container_port
    to_port     = var.container_port
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_default_security_group" "default" {
  vpc_id = aws_vpc.main.id

  ingress {
    cidr_blocks = [aws_vpc.main.cidr_block]
    protocol    = "-1"
    self        = false
    from_port   = 0
    to_port     = 0
  }

  egress {
    from_port   = 0
    to_port     = 0
    self        = false
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
