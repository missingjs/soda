FROM missingjs/soda-jdk:11
RUN apt-get install -y zip unzip wget curl
WORKDIR /opt
RUN wget https://groovy.jfrog.io/artifactory/dist-release-local/groovy-zips/apache-groovy-binary-4.0.2.zip
RUN unzip apache-groovy-binary-4.0.2.zip

ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
ENV GROOVY_HOME=/opt/groovy-4.0.2
ENV PATH="${GROOVY_HOME}/bin:${PATH}"

WORKDIR /
