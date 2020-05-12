# outputs.tf

# output the load balancers dns name which needs to be aliased by
# dev.metadata.fdz.dzhw.eu
# test.metadata.fdz.dzhw.eu
# www.metadata.fdz.dzhw.eu
# metadata.fdz.dzhw.eu
output "alb_hostname" {
  value = aws_alb.load_balancer.dns_name
}