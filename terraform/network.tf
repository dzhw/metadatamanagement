# network.tf

# declare the vpc defined by tecracer (import id: vpc-027f0043664598444)
resource "aws_vpc" "main" {
  cidr_block = "172.16.15.0/24"
  tags = {
    "Name" = "MDM"
  }
}

# declare the private subnets defined by tecracer
# private networks have internet access by leveraging a transit gateway (shared ressource)
# import id subnet-0a661737815565ad4
resource "aws_subnet" "private_1" {
  availability_zone = "eu-central-1a"
  cidr_block        = "172.16.15.0/26"
  vpc_id            = aws_vpc.main.id
  tags = {
    "Name" = "Private 1"
  }
}

# import id subnet-0379630a1642b6921
resource "aws_subnet" "private_2" {
  availability_zone = "eu-central-1b"
  cidr_block        = "172.16.15.64/26"
  vpc_id            = aws_vpc.main.id
  tags = {
    "Name" = "Private 2"
  }
}

# declare the public subnets defined by tecracer
# import id subnet-0acf843caf849a3cf
resource "aws_subnet" "public_1" {
  availability_zone       = "eu-central-1a"
  cidr_block              = "172.16.15.128/26"
  vpc_id                  = aws_vpc.main.id
  map_public_ip_on_launch = true
  tags = {
    "Name" = "Public 1"
  }
}

# import id subnet-00ea889c6527e3acc
resource "aws_subnet" "public_2" {
  availability_zone       = "eu-central-1b"
  cidr_block              = "172.16.15.192/26"
  vpc_id                  = aws_vpc.main.id
  map_public_ip_on_launch = true
  tags = {
    "Name" = "Public 2"
  }
}




