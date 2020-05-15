# simple_email_service.tf
# Currently there are a couple of email adresses configured in amazon ses.
# However we need to setup DKIM with the IT-Support
resource "aws_ses_domain_identity" "system_email_senders" {
  count = length(var.system_email_senders)
  domain = var.system_email_senders[count.index]
}

resource "aws_ses_domain_dkim" "dzhw" {
  domain = var.system_email_senders[0]
}