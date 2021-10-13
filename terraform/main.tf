terraform {
  # use s3 as backend for storing the terraform state
  # currently not configured for concurrent state updates
  backend "s3" {
    profile = "mdm"
    bucket  = "metadatamanagement-private"
    key     = "terraform.tfstate"
    region  = "eu-central-1"
  }
}

# Specify the provider and access details
provider "aws" {
  profile = "mdm"
  region  = "eu-central-1"
  version = "~> 2.61"
}

data "aws_canonical_user_id" "current_user" {
}

# S3 bucket which holds the terraform state and sensitive terraform variables (sensitive_variables.tf)
resource "aws_s3_bucket" "metadatamanagement_private" {
  bucket = "metadatamanagement-private"
  acl    = "private"

  server_side_encryption_configuration {
    rule {
      apply_server_side_encryption_by_default {
        sse_algorithm = "AES256"
      }
    }
  }

  tags = {
    Name        = "metadatamanagement-private"
    Description = "Private storage for dev-related stuff"
  }

  versioning {
    enabled = true
  }
}

resource "aws_s3_bucket_object" "sensitive_variables" {
  bucket = aws_s3_bucket.metadatamanagement_private.id
  key    = "sensitive_variables.tf"
  source = "sensitive_variables.tf"
  etag = filemd5("sensitive_variables.tf")
}

# S3 bucket which holds publicly accessible files like dev mongodb dumps
resource "aws_s3_bucket" "metadatamanagement_public" {
  bucket = "metadatamanagement-public"
  acl    = "public-read"
  versioning {
    enabled    = false
    mfa_delete = false
  }
}
