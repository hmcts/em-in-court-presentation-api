output "microserviceName" {
  value = "${var.product}-${var.component}"
}

output "vaultName" {
  value = "${local.vault_name}"
}

output "vaultUri" {
  value = "${data.azurerm_key_vault.key_vault.vault_uri}"
}

output "s2s_url" {
  value = "http://${var.s2s_url}-${local.local_env}.service.core-compute-${local.local_env}.internal"
}

output "dm_store_app_url" {
  value = "http://${var.dm_store_app_url}-${local.local_env}.service.core-compute-${local.local_env}.internal"
}

