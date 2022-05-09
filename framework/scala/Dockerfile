FROM missingjs/soda-jdk:11

ARG SCALA_VERSION=2.13.8

RUN apt-get update \
    && apt-get install -y curl ca-certificates gnupg unzip zip

# install scala
RUN mkdir /build
WORKDIR /build
RUN curl -OL https://downloads.lightbend.com/scala/$SCALA_VERSION/scala-${SCALA_VERSION}.deb \
    && dpkg -i scala-${SCALA_VERSION}.deb \
    && rm scala-${SCALA_VERSION}.deb

# install sbt
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list \
    && echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list \
    && curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add \
    && apt-get update \
    && apt-get install -y sbt

