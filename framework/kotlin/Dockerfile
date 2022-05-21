FROM missingjs/soda-jdk:11

ARG KOTLIN_VERSION=1.6.21

ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

WORKDIR /opt
RUN apt-get update \
    && apt-get install -y zip unzip curl python3 \
    && curl -OL https://github.com/JetBrains/kotlin/releases/download/v${KOTLIN_VERSION}/kotlin-compiler-${KOTLIN_VERSION}.zip \
    && unzip kotlin-compiler-${KOTLIN_VERSION}.zip \
    && rm kotlin-compiler-${KOTLIN_VERSION}.zip \
    && mv kotlinc kotlinc-${KOTLIN_VERSION}

ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64 \
    KOTLIN_HOME=/opt/kotlinc-${KOTLIN_VERSION}

ENV PATH="${KOTLIN_HOME}/bin:${PATH}"

WORKDIR /

