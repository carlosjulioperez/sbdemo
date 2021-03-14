#!/usr/bin/env bash

mkdir microservices
cd microservices

spring init \
--boot-version=2.4.3.RELEASE \
--build=gradle \
--java-version=1.8 \
--packaging=jar \
--name=product-service \
--package-name=ec.carper.microservices.core.product \
--groupId=ec.carper.microservices.core.product \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
product-service

spring init \
--boot-version=2.4.3.RELEASE \
--build=gradle \
--java-version=1.8 \
--packaging=jar \
--name=price-service \
--package-name=ec.carper.microservices.core.price \
--groupId=ec.carper.microservices.core.price \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
price-service

spring init \
--boot-version=2.4.3.RELEASE \
--build=gradle \
--java-version=1.8 \
--packaging=jar \
--name=product-composite-service \
--package-name=ec.carper.microservices.composite.product \
--groupId=ec.carper.microservices.composite.product \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
product-composite-service

cd ..
