
# 🚀 Guia de Configuração do Laboratório CDC com Ansible

Este guia apresenta o processo completo para configurar e executar o ambiente de laboratório para **Change Data Capture (CDC)** usando **Ansible**, **Docker** e **AWS EC2**.

---

## 📋 Pré-requisitos

- ✅ Terraform já executado (infraestrutura AWS provisionada)
- ✅ Ansible instalado na máquina local
- ✅ Acesso SSH configurado para a instância EC2

---

## 🔧 Configuração Automática do IP

Todas as instruções utilizam um comando padrão para extrair automaticamente o IP público da instância EC2:

```bash
# Comando padrão para extrair IP público
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')
echo "🌐 IP público: $VM_TARGET"
```
---

## 📝 Sequência de Execução

### **Passo 1: 🏗️ Instalação da Estrutura Base**
*Instala Java, Maven e Docker na instância*

```bash
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')
echo "🌐 IP público: $VM_TARGET"

ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/01.instalacao-base.yml
```
**📦 O que este passo instala:**
- ☕ OpenJDK 11
- 📦 Maven 3.6+
- 🐳 Docker CE + Docker Compose

---

### **Passo 2: 📥 Clone e Build do Projeto**
*Faz o clone do projeto e executa mvn clean install*
```bash
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')
echo "🌐 IP público: $VM_TARGET"

ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/02.git_pull_maven_build.yml
```
**📋 O que este passo faz:**
- 🔄 Clone/pull do repositório Git
- 🔨 Executa `mvn clean install`
- 📦 Gera o JAR da aplicação

---

### **Passo 3: 🌐 Configuração da Variável de Ambiente**
*Define o IP público como variável de ambiente para o Kafka*
```bash
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')
echo "🌐 IP público: $VM_TARGET"

ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/03.set_env_variable.yml -e "target_ip=${VM_TARGET}"
```
**⚙️ O que este passo configura:**
- 🔧 Variável `HOSTNAME_COMMAND` para acesso externo ao Kafka
- 📝 Persiste configuração no perfil do usuário

---

### **Passo 4: 🐳 Inicialização dos Serviços Docker**
*Sobe todos os containers do ambiente (Zookeeper, Kafka, MySQL, Debezium)*
```bash
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')
echo "🌐 IP público: $VM_TARGET"

ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/04.start_docker_compose.yml
```
**🚀 Serviços iniciados:**
- 🐘 **Zookeeper** (porta 2181)
- 🌊 **Kafka** (portas 9092, 9094)
- 🗄️ **MySQL** (porta 3306)
- 🔗 **Kafka Connect/Debezium** (porta 8083)

---

### **Passo 5: 🗄️ Configuração do MySQL**
*Configura autenticação nativa do MySQL para CDC*
```bash
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')
echo "🌐 IP público: $VM_TARGET"

ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/05.configure_mysql.yml
```
**🔐 Configurações aplicadas:**
- ✅ Autenticação nativa para usuário `root`
- ✅ Flush de privilégios
- ✅ Teste de conectividade

---

### **Passo 6: 🔗 Configuração do Conector Debezium**
*Cria e configura o conector MySQL para Change Data Capture*
```bash
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')
echo "🌐 IP público: $VM_TARGET"

ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/06.configure_kafka_connect.yml -e "target_ip=${VM_TARGET}"
```
**📊 O que este passo configura:**
- 🔌 Conector `exemplodb-connector`
- 📡 Monitoramento da database `exemplodb`
- 🎯 Tópicos Kafka para CDC
- ✅ Verificação do status do conector

---

## 🛠️ Comandos de Gerenciamento

### **📋 Listar Conectores Ativos**
```bash
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')
ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/06.01.manage_kafka_connectors.yml -e "target_ip=${VM_TARGET}" -e "action=list"
```
### **📊 Verificar Status do Conector**
```bash
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')
ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/06.01.manage_kafka_connectors.yml -e "target_ip=${VM_TARGET}" -e "action=status"
```
### **🔄 Reiniciar Conector**
```bash
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')
ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/06.01.manage_kafka_connectors.yml -e "target_ip=${VM_TARGET}" -e "action=restart"
```
### **🗑️ Deletar Conector**
```bash
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')
ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/06.01.manage_kafka_connectors.yml -e "target_ip=${VM_TARGET}" -e "action=delete"
```
---

## 🛑 Finalização do Ambiente

### **Parar Todos os Serviços**
```bash
export VM_TARGET=$(grep -A 5 '"public_ip":' terraform/terraform.tfstate | grep -v null | head -1 | awk -F'"' '{print $4}')
echo "🌐 IP público: $VM_TARGET"

ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/07.stop_docker_compose.yml
```

**🧹 O que este comando faz:**
- ⏹️ Para todos os containers
- 🗑️ Remove volumes não utilizados
- 🧽 Limpa recursos Docker órfãos

---

## 🌐 Portas e Serviços

| 🔧 **Serviço**  | 🚪 **Porta** | 📝 **Descrição**        |
|-----------------|--------------|-------------------------|
| SSH             | 22           | Acesso remoto           |
| HTTP            | 80           | Servidor web            |
| Zookeeper       | 2181         | Coordenação distribuída |
| Kafka (local)   | 9092         | Broker Kafka (interno)  |
| Kafka (externo) | 9094         | Broker Kafka (externo)  |
| MySQL           | 3306         | Base de dados           |
| Kafka Connect   | 8083         | API Debezium            |
| Exemplo API     | 8080         | Aplicação Spring Boot   |

---

## 🎯 URLs de Acesso

Após a configuração completa, os serviços estarão disponíveis em:

- **🌊 Kafka Connect API:** `http://${VM_TARGET}:8083`
- **🗄️ MySQL:** `${VM_TARGET}:3306`
- **☕ Exemplo API:** `http://${VM_TARGET}:8080`
- **📖 Swagger UI:** `http://${VM_TARGET}:8080/swagger-ui.html`

---

## ✅ Verificações de Saúde

Para verificar se todos os serviços estão funcionando:
```bash
# Verificar containers em execução
ssh -i ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ubuntu@${VM_TARGET} "docker ps"

# Testar Kafka Connect
curl http://${VM_TARGET}:8083/

# Testar API da aplicação
curl http://${VM_TARGET}:8080/actuator/health
```
---

## 🚨 Solução de Problemas

**🔍 Verificar logs dos containers:**
```bash
ssh -i ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ubuntu@${VM_TARGET} "docker logs kafka"
ssh -i ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ubuntu@${VM_TARGET} "docker logs mysql"
ssh -i ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ubuntu@${VM_TARGET} "docker logs connect"
```
**🔄 Reiniciar serviços específicos:**
```bash
ssh -i ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ubuntu@${VM_TARGET} "docker restart kafka"
```


