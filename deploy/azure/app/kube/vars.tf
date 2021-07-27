############################################
# AUTHENTICATION
############################################
# RELYING PURELY ON ENVIRONMENT VARIABLES as the user can control these from their own environment
############################################
# NAMING
############################################

variable "name_company" {
  type = string
}

variable "name_project" {
  type = string
}

variable "name_domain" {
  type = string
}

variable "name_component" {
  type = string
}

variable "name_role" {
  type = string
}

variable "name_environment" {
  type = string
}

variable "attributes" {
  type = list(string)
}

variable "tags" {
  type = map(string)
}

# Each region must have corresponding a shortend name for resource naming purposes
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

############################################
# AZURE INFORMATION
############################################

variable "resource_group_location" {
  type = string
}

variable "app_gateway_frontend_ip_name" {
  type = string
}

variable "dns_record" {
  type = string
}

variable "dns_zone_name" {
  type = string
}

variable "dns_zone_resource_group" {
  type = string
}

variable "core_resource_group" {
  type = string
}

variable "internal_dns_zone_name" {
  type = string
}


###########################
# CONDITIONAL SETTINGS
##########################
variable "create_cosmosdb" {
  description = "Whether to create a cosmosdb or not for this application"
  type        = bool
}

variable "create_cache" {
  type        = bool
  description = "Whether to create a RedisCache"
}

variable "create_dns_record" {
  type = bool
}

variable "create_cdn_endpoint" {
  type = bool
}
###########################
# CosmosDB SETTINGS
##########################
variable "cosmosdb_sql_container" {
  type        = string
  description = "Specify the SQLContainer name in CosmosDB"
  default     = "Menu"
}

variable "cosmosdb_sql_container_partition_key" {
  type        = string
  default     = "/id"
  description = "Specify partition key"
}

variable "cosmosdb_kind" {
  type        = string
  default     = "GlobalDocumentDB"
  description = "Specify the CosmosDB kind"
}
variable "cosmosdb_offer_type" {
  type        = string
  default     = "Standard"
  description = "Specify the offer type"
}
