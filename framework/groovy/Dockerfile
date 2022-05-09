FROM missingjs/soda-jdk:11

ARG GROOVY_VERSION=4.0.2
ARG bin_package=apache-groovy-binary-${GROOVY_VERSION}.zip

WORKDIR /opt
RUN apt-get update \
    && apt-get install -y zip unzip curl \
    && curl -OL https://groovy.jfrog.io/artifactory/dist-release-local/groovy-zips/$bin_package \
    && unzip $bin_package \
    && rm $bin_package

ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
ENV GROOVY_HOME=/opt/groovy-${GROOVY_VERSION}
ENV PATH="${GROOVY_HOME}/bin:${PATH}"

WORKDIR /
