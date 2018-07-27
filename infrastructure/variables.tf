variable "product" {
  type = "string"
}

variable "shared_product_name" {
  default = "rpa"
}

variable "component" {
  type = "string"
}

variable "team_name" {
  default = "rpa"
}

variable "app_language" {
  default = "java"
}

variable "location" {
  type    = "string"
  default = "UK South"
}

variable "env" {
  type = "string"
}

variable "subscription" {
  type = "string"
}

variable "capacity" {
  default = "1"
}

variable "ilbIp"{}

variable "tenant_id" {}

variable "jenkins_AAD_objectId" {
  type                        = "string"
  description                 = "(Required) The Azure AD object ID of a user, service principal or security group in the Azure Active Directory tenant for the vault. The object ID must be unique for the list of access policies."
}

variable "common_tags" {
  type = "map"
}
////////////////////////////////////////////////
//Addtional Vars ///////////////////////////////
////////////////////////////////////////////////

variable "java_opts" {
  default = ""
}
////////////////////////////////////////////////
// Endpoints
////////////////////////////////////////////////
variable "s2s_url" {
  default = "rpe-service-auth-provider"
}

variable "dm_store_app_url" {
  default = "dm-store"
}

variable "postgresql_user" {
  default = "icp"
}

variable "database_name" {
  default = "incourtpres"
}

variable "s2s_names_whitelist" {
  default = "em_gw,jui_webapp,pui_webapp"
}

////////////////////////////////////////////////
// Logging
////////////////////////////////////////////////
variable "root_appender" {
  default = "JSON_CONSOLE"
}

variable "json_console_pretty_print" {
  default = "false"
}

variable "log_output" {
  default = "single"
}

variable "root_logging_level" {
  default = "INFO"
}

variable "show_sql" {
  default = "true"
}
