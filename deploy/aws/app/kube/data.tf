locals {
  # This is a map of default tags passed to the provider.
  # This can be extended like adding cost-code or organization name.
  default_tags = {
    Environment = var.env
    Application = var.docker_image_name
    Owner       = var.owner
  }

  account_id = data.aws_caller_identity.current.account_id
}

data "aws_caller_identity" "current" {}