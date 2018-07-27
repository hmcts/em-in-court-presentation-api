// used for db migrations
output "microserviceName" {
  value = "${local.app_full_name}"
}

// used for db migrations
output "vaultName" {
  value = "${module.local_key_vault.key_vault_name}"
}

// used for grabing shared secrects (shown in the jenkins file)
output "vaultUri" {
  value = "${data.azurerm_key_vault.shared_key_vault.vault_uri}"
}

