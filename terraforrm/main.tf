# Terraform configuration for AWS S3 bucket setup only

provider "aws" {
  region = "us-east-1"
}

# --------------------
# Random ID for unique S3 bucket name
# --------------------
resource "random_id" "bucket_id" {
  byte_length = 4
}

# --------------------
# S3 Bucket
# --------------------
resource "aws_s3_bucket" "cloudapp_bucket" {
  bucket = "cloudapp-bucket-${random_id.bucket_id.hex}"
  acl    = "private"
  force_destroy = true

  tags = {
    Name = "cloudapp-s3"
  }
}
