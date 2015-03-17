# Smoke Kafka

## Introduction

Here is the pratical and repeatable example to run kafka example. No more blogs or code snippets and we're making it easier with docker.

## Setup

This client relies on ZooKeeper and Kafka server so we setup these containers.

```
docker run -d -p 2181:2181 tobegit3hub/standalone-zookeeper
```

TODO: Build the standalone-kafka image

## Usage

```
mvn exec:java -Dexec.mainClass="cn.chendihao.SmokeKafka"
```