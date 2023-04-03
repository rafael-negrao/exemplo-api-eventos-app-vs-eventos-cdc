# Instruções para configuração do ambiente


## Instalação do Java 8

```shell
sudo apt update
sudo apt install openjdk-8-jdk
```

## Instalação do Maven

```shell
sudo apt install maven
```

## Instalação do Docker

### Instalando as dependências
```
sudo apt install apt-transport-https curl gnupg-agent ca-certificates software-properties-common -y
```

### Instalando o Docker

Adicionando a chave GPGK
```
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```

Adicionando o repositório
```
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"
```

Instalando o Docker
```
sudo apt install docker-ce docker-ce-cli containerd.io -y
```

Adicionando o usuário corrente ao grupo do docker
```
sudo usermod -aG docker $USER
```

Verificando se o docker foi instalado
```
docker version
```

Verificando o status do serviço do docker
```
sudo systemctl status docker
```

Usar este comando para iniciar o serviço do docker
```
sudo systemctl start docker
```

Usar este comando para o docker inicializar de forma automática
```
sudo systemctl enable docker
```
Usar este comando para reiniciar o serviço do docker
```
sudo systemctl restart docker
```

#### Testando o Docker
Rodar este comando para vefiricar se docker esta funcionado de forma correta
```
docker run hello-world
```

Para mostrar a imagens baixadas
```
docker images
```

Para mostar as imagens que foram executadas
```
docker ps -a
```

### Instalando o docker-compose

Fazer download do docker-compose
```
mkdir -p ~/.docker/cli-plugins/
curl -SL https://github.com/docker/compose/releases/download/v2.3.3/docker-compose-linux-x86_64 -o ~/.docker/cli-plugins/docker-compose
```

Alterando o modo de execução do docker-compose
```
chmod +x ~/.docker/cli-plugins/docker-compose
```

Testando se tudo deu certo
```
docker compose version
```
