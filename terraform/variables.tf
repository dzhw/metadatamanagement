# variables.tf

variable "stages" {
  description = "The stages to be setup (dev, test AND prod)"
  default = ["dev","test","prod"]
}
variable "hostnames" {
  description = "The hostnames to be setup (dev, test AND prod)"
  default = ["dev.metadata.fdz.dzhw.eu","test.metadata.fdz.dzhw.eu","metadata.fdz.dzhw.eu"]
}

variable "container_port" {
  description = "The port on which the web containers will listen"
  default = 8080
}

variable "container_memory" {
  description = "The memory for the web, worker and task container (in MB)"
  default = 2048
}

variable "container_cpu" {
  default = 1024
}

variable "container_count" {
  description = "The number of web containers to start"
  default = 3
}

variable "health_check_path" {
  description = "Path for determining whether a web container is healthy"
  default = "/management/info"
}
