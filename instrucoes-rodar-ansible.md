# Como montar a maquina laboratório utilizando ansible

- Criar estrutura base
  - java
  - maven
  - docker

```shell
# Extrair o IP público usando grep e awk
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')

# Verificar se foi extraído corretamente
echo "IP público: $VM_TARGET"

ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/01.instalacao-base.yml
```

- Fazer o clone / pull do projeto
- Rodar: mvn clean install

```shell
# Extrair o IP público usando grep e awk
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')

# Verificar se foi extraído corretamente
echo "IP público: $VM_TARGET"

ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/02.git_pull_maven_build.yml
```

- Criar variavel de ambiente na máquina para o IP Externo

```shell
# Extrair o IP público usando grep e awk
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')

# Verificar se foi extraído corretamente
echo "IP público: $VM_TARGET"

ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/03.set_env_variable.yml -e "target_ip=${VM_TARGET}"
```

- Iniciar o docker compose

```shell
# Extrair o IP público usando grep e awk
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')

# Verificar se foi extraído corretamente
echo "IP público: $VM_TARGET"
ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/04.start_docker_compose.yml
```

- Configurar o MySQL

```shell
# Extrair o IP público usando grep e awk
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')

# Verificar se foi extraído corretamente
echo "IP público: $VM_TARGET"

ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/05.configure_mysql.yml
```

- Configuarar o conector Debezium

```shell
# Extrair o IP público usando grep e awk
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')

# Verificar se foi extraído corretamente
echo "IP público: $VM_TARGET"

ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/06.configure_kafka_connect.yml
```

- Parar o docker compose

```shell
# Extrair o IP público usando grep e awk
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')

# Verificar se foi extraído corretamente
echo "IP público: $VM_TARGET"
ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/stop_docker_compose.yml
```
