FROM openjdk:11 AS sbt-build

# install sbt
RUN curl -L 'https://github.com/sbt/sbt/releases/download/v1.3.3/sbt-1.3.3.tgz' | \
    tar xzf - -C /usr/local --strip-components=1 && sbt exit

WORKDIR /app

# fetch dependencies first in order to leverage build cache
COPY project project
COPY *.sbt ./
RUN sbt update

COPY src src

RUN sbt compile && \
  sbt test && \
  sbt universal:packageZipTarball
RUN mv "target/universal/json-validator-$(sbt -no-colors version | tail -1 | cut -d ' ' -f 2).tgz" app.tgz

# end of build stage

FROM openjdk:11-jre-slim

WORKDIR /app

ENV JAVA_OPTS="-Xmx512m"

COPY --from=sbt-build /app/app.tgz .
RUN tar xzpf app.tgz

COPY entrypoint.sh ./
RUN chmod u+x entrypoint.sh bin/json-validator

CMD ["./bin/json-validator"]
ENTRYPOINT ["./entrypoint.sh"]
