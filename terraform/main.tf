terraform {
  # use s3 as backend for storing the terraform state 
  # currently not configured for concurrent state updates
  backend "s3" {
    profile = "mdm"
    bucket = "metadatamanagement-private"
    key = "terraform.tfstate"
    region = "eu-central-1"
  }
}