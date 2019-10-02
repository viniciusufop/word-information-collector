# word-information-collector
## Descrição
Desevolvimento de um serviço (front e back) para efetuar a analise de uma determinada palavra e retonar os significados e sinonimos da mesma. Foi utilizado o site https://dicionariocriativo.com.br como repositório base para as pesquisa dos dados pertinentes as palavras.

## Objetivo
Aplicação desenvolvida para tratar um desafio proposto de analise de palavras

## Tecnologias Utilizadas

### Back-end

* Java
* Spring Boot
* Spring Data Mongo
* Lombok
* jsoup
* gradle

### Front end

* Html
* Css
* Bootstrap
* JavaScript
* TypeScript
* Angular 7
* cytoscapeJS
* ngx-cytoscape
* npm

### Ambiente
* docker
* docker compose
* mongo
* mongo express
* ngix

## Execução do projeto em DEV

Basta efetuar o clone do projeto no repositorio e executar o comando ```sudo docker-compose up -d --build```. 
O mesmo vai baixar as imagens e buildar os projetos, posteriormente subir os container.
* Recomendo utilizar as versões mais recentes de docker e docker-compose.

### URL disponibilizadas
Abaixo as URL de acesso as aplicação que estão executando, após subir os containers:
* Back: http://localhost:8080/swagger-ui.html
* Front: http://localhost
* Mongo Express: http://localhost:8081

## Falhas não solucionadas
* ~Foi identificado que ao comunicar com o site base para a pesquisa e utilizar palavras com caracteres especiais o sistema acaba convertendo o mesmo de encoding gerando um retorno sem informação para a palavra.~ Solucionado mudando a versão do JSoup.
