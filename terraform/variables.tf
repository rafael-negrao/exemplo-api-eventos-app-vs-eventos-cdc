variable "prefix" {
  type        = string
  description = "Prefix"
}

variable "tags" {
  default     = {}
  type        = map(string)
  description = "Optional tags to add to created resources"
}

variable "region" {
  default     = "us-west-1"
  type        = string
  description = "AWS region to deploy to"
}
