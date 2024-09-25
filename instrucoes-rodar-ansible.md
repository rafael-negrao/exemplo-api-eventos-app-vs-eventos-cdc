# Como montar a maquina laboratório utilizando ansible

- Criar estrutura base
  - java
  - maven
  - docker

```shell
export VM_TARGET="PREENCHER_NOME_MAQUINA.compute-1.amazonaws.com"
ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/instalacao-base.yml
```

- Fazer o clone / pull do projeto
- Rodar: mvn clean install

```shell
export VM_TARGET="PREENCHER_NOME_MAQUINA.compute-1.amazonaws.com"
ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/git_pull_maven_build.yml
```

- Criar variavel de ambiente na máquina para o IP Externo

```shell
export VM_TARGET="PREENCHER_NOME_MAQUINA.compute-1.amazonaws.com"
ansible-playbook -i ${VM_TARGET}, -u ubuntu --private-key ./terraform/laboratorio/laboratorio-cdc-ec2-key.pem ansible/set_env_variable.yml
```

