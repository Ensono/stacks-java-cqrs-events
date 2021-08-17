

# Required variables

variable "resource_group_name" {
  type        = string
  description = "Name of hte resource grup holding the resources"
}

variable "resource_group_location" {
  type        = string
  description = "Region in Azure that the resources should be deployed to"
}

variable "name_company" {
  type        = string
  description = "Name of the company"
}

variable "name_project" {
  type        = string
  description = "Name of the project"
}

variable "name_domain" {
  type = string
}

variable "stage" {
  type = string
}

variable "attributes" {
  default = []
}

variable "tags" {
  type    = map(string)
  default = {}
}

variable "cosmosdb_database_name" {
  type = string
}

variable "cosmosdb_collection_name" {
  type = string
}

variable "cosmosdb_connection_string" {
  type = string
}

variable "cosmosdb_lease_collection_name" {
  type    = string
  default = "Leases"
}

# Optional variables
# These have default values that can be overriden as required
variable "function-publisher-name" {
  type    = string
  default = "function-publisher"
}

variable "function-listener-name" {
  type    = string
  default = "function-listener"
}

variable "sb_name" {
  type        = string
  default     = "sb-menu"
  description = "Name of the service bus to create"
}

variable "sb_topic_name" {
  type        = string
  default     = "sbt-menu-events"
  description = "Name of the topic to create"
}

variable "sb_subscription_filtered_name" {
  type        = string
  default     = "sbs-menu-events-filtered"
  description = "Name of the Service Bus subscription, filtered, to create"
}

variable "sb_subscription_name" {
  type        = string
  default     = "sbs-menu-events"
  description = "Name of the Service Bus subscription to create"
}

variable "sb_max_delivery_count" {
  type    = number
  default = 1
}

variable "seed_length" {
  type    = number
  default = 6
}

variable "servicebus_sku" {
  type    = string
  default = "Standard"
}

variable "location_name_map" {
  type = map(string)

  default = {
    northeurope   = "eun"
    westeurope    = "euw"
    uksouth       = "uks"
    ukwest        = "ukw"
    eastus        = "use"
    eastus2       = "use2"
    westus        = "usw"
    eastasia      = "ase"
    southeastasia = "asse"
  }
}
