# provider.tf

# Specify the provider and access details
provider "aws" {
  profile = "mdm"
  region  = "eu-central-1"
  version = "~> 2.61"
}
