output "servicebus_namespace" {
  description = "Service bus namespace"
  value       = azurerm_servicebus_namespace.sb.name
}

output "servicebus_topic_name" {
  description = "Name of the topic"
  value       = azurerm_servicebus_topic.sb_topic.name
}

output "servicebus_subscription_name" {
  description = "Servicebus Subscription name"
  value       = azurerm_servicebus_subscription.sb_sub_1.name
}

output "servicebus_connectionstring" {
  value = azurerm_servicebus_namespace.sb.default_primary_connection_string
}

output "servicebus_subscription_filtered_name" {
  description = "Servicebus Subscription filtered name"
  value       = azurerm_servicebus_subscription.sb_sub_2.name
}

output "servicebus_subscription_id" {
  description = "Servicebus Subscription ID"
  value       = azurerm_servicebus_subscription.sb_sub_1.id
}

output "servicebus_subscription_filtered_id" {
  description = "Servicebus Subscription filtered ID"
  value       = azurerm_servicebus_subscription.sb_sub_2.id
}

output "function_publisher_id" {
  value = azurerm_function_app.function_publisher.id
}

output "function_listener_id" {
  value = azurerm_function_app.function_listener.id
}

output "function_publisher_name" {
  value = azurerm_function_app.function_publisher.name
}

output "function_listener_name" {
  value = azurerm_function_app.function_listener.name
}