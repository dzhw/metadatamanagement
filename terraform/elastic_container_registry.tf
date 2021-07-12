# elastic_container_registry.tf

# setup a container repository for the web and worker container
# different stages will be distinguished by tag (latest-dev, latest-test, latest-prod)
resource "aws_ecr_repository" "metadatamanagement" {
  name                 = "dzhw/metadatamanagement"
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }
}

# setup a container repository for the report_task
# different stages will be distinguished by tag (latest-dev, latest-test, latest-prod)
resource "aws_ecr_repository" "report_task" {
  name                 = "dzhw/report-task"
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }
}

data "template_file" "delete_untagged_policy" {
  template = file("./templates/ecr_lifecycle_policy.json.tpl")
}

resource "aws_ecr_lifecycle_policy" "delete_untagged_report_task_images" {
  repository = aws_ecr_repository.report_task.name
  policy     = data.template_file.delete_untagged_policy.rendered
}

resource "aws_ecr_lifecycle_policy" "delete_untagged_metadatamanagement_images" {
  repository = aws_ecr_repository.metadatamanagement.name
  policy     = data.template_file.delete_untagged_policy.rendered
}
