##
## Fluxo para compilar o projeto em uma imagem docker
##

# Inicia com uma imagem base do Java JDK 8
FROM openjdk:8-jdk-alpine AS BUILDER

# define variavel que identifica um diretorio
ENV APP_HOME=/usr/app/

# acessa o diretorio definido
WORKDIR $APP_HOME

# copia os arquivos necessario para execucao do gradle
COPY build.gradle settings.gradle gradlew $APP_HOME

# copia a pasta do gradle
COPY gradle $APP_HOME/gradle

# executa um build com condicao alternativa 0 (vai baixar as dependencias e retornar um erro)
RUN ./gradlew build || return 0

# copia o codigo fonte da aplicacao
COPY src src

# compila o projeto e gerar o .jar
RUN ./gradlew build

##
## Fluxo para executar o projeto em uma imagem docker
##

# Inicia com uma imagem base do Java JRE 8
FROM openjdk:8-jre-alpine


# define o diretorio final da aplicacao
ENV DIRECTORY=/usr/local/app

# cria o diretorio
RUN mkdir -p $DIRECTORY

# acessa o diretorio definido
WORKDIR $DIRECTORY

# copia a aplicacao para o diretorio final
COPY --from=BUILDER /usr/app/build/libs/word-information-collector-back-0.0.1-SNAPSHOT.jar word-information-collector.jar

COPY run.sh run.sh
RUN chmod +x run.sh