# Criar VPC
resource "aws_vpc" "main" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_support   = true
  enable_dns_hostnames = true
  tags                 = var.tags
}

# Criar gateway de internet para a VPC
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.main.id
  tags   = var.tags
}

# Criar sub-rede pública
resource "aws_subnet" "public" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.1.0/24"
  map_public_ip_on_launch = true
  availability_zone       = "us-east-1a"  # Escolha uma zona de disponibilidade apropriada
  tags                    = var.tags
}

# Criar sub-rede privada
resource "aws_subnet" "private" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.2.0/24"
  availability_zone = "us-east-1a"
  tags              = var.tags
}

# Criar rota para sub-rede pública
resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.main.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }
  tags = var.tags
}

# Associação da tabela de rota à sub-rede pública
resource "aws_route_table_association" "public_route_assoc" {
  subnet_id      = aws_subnet.public.id
  route_table_id = aws_route_table.public_route_table.id
}

# Gerar par de chaves
resource "tls_private_key" "ec2_key" {
  algorithm = "RSA"
  rsa_bits  = 2048
}

resource "aws_key_pair" "ec2_key" {
  key_name   = "${var.prefix}-ec2-key"
  public_key = tls_private_key.ec2_key.public_key_openssh
}

# Criar grupo de segurança
resource "aws_security_group" "ec2_security_group" {
  name        = "${var.prefix}-ec2-security-group"
  description = "Allow traffic for SSH, HTTP, Kafka, MySQL, Debezium"
  vpc_id      = aws_vpc.main.id

  # Regras de saída (egress)
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Regras de entrada (ingress)
  ingress {
    description = "Allow SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow HTTP"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow Zookeeper"
    from_port   = 2181
    to_port     = 2181
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow Kafka access via localhost"
    from_port   = 9092
    to_port     = 9092
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow Kafka external access"
    from_port   = 9094
    to_port     = 9094
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow MySQL"
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow Debezium Kafka Connector"
    from_port   = 8083
    to_port     = 8083
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow Example API"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Criar IP elástico
resource "aws_eip" "ec2_ip" {
  domain = "vpc"
}

# Lançar instância EC2 na sub-rede pública
resource "aws_instance" "ec2_instance" {
  ami           = "ami-0e86e20dae9224db8"  # ID da imagem do Ubuntu
  instance_type = "c5.xlarge"
  key_name      = aws_key_pair.ec2_key.key_name
  subnet_id     = aws_subnet.public.id

  vpc_security_group_ids = [aws_security_group.ec2_security_group.id]

  # Volume raiz (root_block_device)
  root_block_device {
    volume_size           = 40
    volume_type           = "gp3"
    delete_on_termination = true
  }

  tags = var.tags

  # Associação de IP elástico
  depends_on = [aws_eip.ec2_ip]
}

# Associação de IP elástico com a instância
resource "aws_eip_association" "ec2_ip_assoc" {
  instance_id   = aws_instance.ec2_instance.id
  allocation_id = aws_eip.ec2_ip.id
}

# Output do arquivo PEM
resource "local_file" "private_key" {
  filename        = "${path.module}/${var.prefix}-ec2-key.pem"
  content         = tls_private_key.ec2_key.private_key_pem
  file_permission = "0400"
}

# Outputs
output "public_ip" {
  value = aws_eip.ec2_ip.public_ip
}

output "instance_id" {
  value = aws_instance.ec2_instance.id
}

output "private_key_path" {
  value = local_file.private_key.filename
}
