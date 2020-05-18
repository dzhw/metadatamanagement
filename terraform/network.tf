# network.tf
# networking has been setup by tecracer
# the VPC spans two AZs each having a private and a public subnet
# cidr blocks are aligned with DZHW local network
resource "aws_vpc" "main" {
  cidr_block = "172.16.15.0/24"
  tags = {
    "Name" = "MDM"
  }
}

resource "aws_subnet" "private_1" {
  availability_zone = "eu-central-1a"
  cidr_block        = "172.16.15.0/26"
  vpc_id            = aws_vpc.main.id
  tags = {
    "Name" = "Private 1"
  }
}

resource "aws_subnet" "private_2" {
  availability_zone = "eu-central-1b"
  cidr_block        = "172.16.15.64/26"
  vpc_id            = aws_vpc.main.id
  tags = {
    "Name" = "Private 2"
  }
}

resource "aws_subnet" "public_1" {
  availability_zone       = "eu-central-1a"
  cidr_block              = "172.16.15.128/26"
  vpc_id                  = aws_vpc.main.id
  map_public_ip_on_launch = true
  tags = {
    "Name" = "Public 1"
  }
}

resource "aws_subnet" "public_2" {
  availability_zone       = "eu-central-1b"
  cidr_block              = "172.16.15.192/26"
  vpc_id                  = aws_vpc.main.id
  map_public_ip_on_launch = true
  tags = {
    "Name" = "Public 2"
  }
}

resource "aws_internet_gateway" "internet_gateway" {
  vpc_id = aws_vpc.main.id
  tags = {
    "Name" = "IGW MDM"
  }
}

resource "aws_ec2_transit_gateway" "shared_transit_gateway" {
  default_route_table_association = "disable"
  default_route_table_propagation = "disable"
  description                     = "Connection of VPN and VPCs"
}

resource "aws_default_network_acl" "default_acl" {
  default_network_acl_id = aws_vpc.main.default_network_acl_id
  subnet_ids             = [aws_subnet.public_1.id, aws_subnet.public_2.id, aws_subnet.private_1.id, aws_subnet.private_2.id]

  ingress {
    action     = "allow"
    cidr_block = "0.0.0.0/0"
    from_port  = 0
    icmp_code  = 0
    icmp_type  = 0
    protocol   = "-1"
    rule_no    = 100
    to_port    = 0
  }

  egress {
    protocol   = -1
    rule_no    = 100
    action     = "allow"
    cidr_block = "0.0.0.0/0"
    from_port  = 0
    to_port    = 0
  }
}

resource "aws_vpc_dhcp_options" "dhcp_options" {
  domain_name = "eu-central-1.compute.internal"
  domain_name_servers = [
    "AmazonProvidedDNS"
  ]
  tags = {}
}

resource "aws_route_table" "main_route_table" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "Main Routing Table"
  }
}

resource "aws_route_table" "private_route_table" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    transit_gateway_id = aws_ec2_transit_gateway.shared_transit_gateway.id
  }

  tags = {
    Name = "Private Routing Table"
  }
}

resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.main.id
  
  route {
    cidr_block = "10.0.0.0/8"
    transit_gateway_id = aws_ec2_transit_gateway.shared_transit_gateway.id
  }

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.internet_gateway.id
  }

  tags = {
    Name = "Public Routing Table"
  }
}

resource "aws_route_table_association" "private_1" {
  subnet_id      = aws_subnet.private_1.id
  route_table_id = aws_route_table.private_route_table.id
}

resource "aws_route_table_association" "private_2" {
  subnet_id      = aws_subnet.private_2.id
  route_table_id = aws_route_table.private_route_table.id
}

resource "aws_route_table_association" "public_1" {
  subnet_id      = aws_subnet.public_1.id
  route_table_id = aws_route_table.public_route_table.id
}

resource "aws_route_table_association" "public_2" {
  subnet_id      = aws_subnet.public_2.id
  route_table_id = aws_route_table.public_route_table.id
}



