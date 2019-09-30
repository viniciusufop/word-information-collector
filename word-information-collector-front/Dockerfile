# The builder from node image
FROM node:alpine as builder

# build-time variables 
ARG env=prod

RUN apk update && apk add --no-cache make git

# Move our files into directory name "app"
WORKDIR /app

COPY package.json package-lock.json  /app/
RUN npm install @angular/cli@7.0.7 -g
RUN cd /app && npm install

COPY angular.json tsconfig.json tsconfig.app.json tsconfig.spec.json tslint.json  /app/
COPY src /app/src

# Build with $env variable from outside
RUN cd /app && npm run build:$env

# Build a small nginx image with static website
FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*
COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=builder /app/dist/word-information-collector-front /usr/share/nginx/html
RUN chown nginx:nginx /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
