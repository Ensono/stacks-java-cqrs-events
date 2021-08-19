
# create a random strng to be used for the name of the storage accounts
resource "random_string" "sa_name_publisher" {
  length  = 16
  upper   = false
  special = false
}

resource "random_string" "sa_name_listener" {
  length  = 16
  upper   = false
  special = false
}

# Create a new storage accounts to store TF state for future deployments
resource "azurerm_storage_account" "sa_publisher" {
  name                = random_string.sa_name_publisher.result
  resource_group_name = var.resource_group_name
  location            = var.resource_group_location

  account_tier             = "Standard"
  account_replication_type = "LRS"
}

resource "azurerm_storage_account" "sa_listener" {
  name                = random_string.sa_name_listener.result
  resource_group_name = var.resource_group_name
  location            = var.resource_group_location

  account_tier             = "Standard"
  account_replication_type = "LRS"
}

# The app plans for the functions
resource "azurerm_app_service_plan" "app_sp_publisher" {
  name                = "service-plan-${var.function-publisher-name}"
  resource_group_name = var.resource_group_name
  location            = var.resource_group_location
  kind                = "linux"
  reserved            = true

  sku {
    tier = "ElasticPremium"
    size = "EP1"
  }
}

resource "azurerm_app_service_plan" "app_sp_listener" {
  name                = "service-plan-${var.function-listener-name}"
  resource_group_name = var.resource_group_name
  location            = var.resource_group_location
  kind                = "linux"
  reserved            = true

  sku {
    tier = "ElasticPremium"
    size = "EP1"
  }
}

# The function apps
resource "azurerm_function_app" "function_publisher" {
  name                = "${var.function-publisher-name}-${random_string.seed.result}"
  resource_group_name = var.resource_group_name
  location            = var.resource_group_location
  depends_on = [
    azurerm_servicebus_namespace.sb
  ]

  app_service_plan_id        = azurerm_app_service_plan.app_sp_publisher.id
  storage_account_name       = azurerm_storage_account.sa_publisher.name
  storage_account_access_key = azurerm_storage_account.sa_publisher.primary_access_key

  app_settings = {
    COSMOSDB_COLLECTION_NAME       = var.cosmosdb_collection_name
    COSMOSDB_CONNECTIONSTRING      = var.cosmosdb_connection_string
    COSMOSDB_DATABASE_NAME         = var.cosmosdb_database_name
    COSMOSDB_LEASE_COLLECTION_NAME = var.cosmosdb_lease_collection_name
    SERVICEBUS_CONNECTIONSTRING    = azurerm_servicebus_namespace.sb.default_primary_connection_string
    SUBSCRIPTION_NAME              = azurerm_servicebus_subscription.sb_sub_1.name
    TOPIC_NAME                     = azurerm_servicebus_topic.sb_topic.name
  }
}

resource "azurerm_function_app" "function_listener" {
  name                = "${var.function-listener-name}-${random_string.seed.result}"
  resource_group_name = var.resource_group_name
  location            = var.resource_group_location
  depends_on = [
    azurerm_servicebus_namespace.sb
  ]

  app_service_plan_id        = azurerm_app_service_plan.app_sp_listener.id
  storage_account_name       = azurerm_storage_account.sa_listener.name
  storage_account_access_key = azurerm_storage_account.sa_listener.primary_access_key

  app_settings = {
    SERVICEBUS_CONNECTIONSTRING = azurerm_servicebus_namespace.sb.default_primary_connection_string
    SUBSCRIPTION_NAME           = azurerm_servicebus_subscription.sb_sub_2.name
    TOPIC_NAME                  = azurerm_servicebus_topic.sb_topic.name
  }
}

