variable "docker_image_name" {
  type = string
  default = "stacks-team"
}
variable "create_docker_repositories" {
  type    = bool
  default = false
}
variable "tags" {
  description = "Meta data for labelling the infrastructure"
  type        = map(string)
  default     = {}
}

variable "env" {
  description = "The name of the environment."
  default     = "nonprod"
  type        = string
}

variable "owner" {
  description = "Responsible parties"
  type        = string
}

variable "region" {
  description = "AWS Region for this infrastruture"
  type        = string
  default     = "eu-west-2"
}