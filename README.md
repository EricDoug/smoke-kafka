# Smoke Kafka

## Introduction

We implement a painless kafka client for developers.

It's pratical and repeatable because of docker. Everybody can run it within seconds.

## Setup Kafka

The client relies on ZooKeeper and Kafka and we should setup [standalone-kafka](https://github.com/tobegit3hub/standalone-kafka) with one command.

```
sudo docker run -d --net=host tobegit3hub/standalone-kafka
```

## Usage

Run with docker container.

```
sudo docker run -i -t --net=host tobegit3hub/smoke-kafka
```

Or compile source code with maven.

```
mvn clean package
sh ./target/smoke-kafka-1.0-SNAPSHOT/bin/smoke-kafka.sh
```