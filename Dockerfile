FROM maven:3.5.0-jdk-8-alpine

WORKDIR /usr/ecommerce

ADD ./application/pom.xml /usr/ecommerce/pom.xml

RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

ADD ./application/src /usr/ecommerce/src
RUN ["mvn", "package"]

EXPOSE 4567

CMD ["/usr/bin/java", "-jar", "target/ecommerce-jar-with-dependencies.jar"]