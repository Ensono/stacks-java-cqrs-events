data "azurerm_client_config" "current" {}

# create a random string to be used to create
# similar but different resources
resource "random_string" "seed" {
  length  = var.seed_length
  upper   = false
  number  = false
  special = false
}
