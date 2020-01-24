# JSON validator

## Build and run

```sh
docker-compose up
```

Alternatively
```sh
docker build -t json-validator . && \
  docker run -e PORT=8080 -e SCHEMA_DIR=/schemas \
    -v "$(pwd)/tmp/schemas:/schemas" \
    -p 8080:8080 \
    json-validator
```

Both commands will run the service saving the schemas to the local directory
`./tmp/schemas`.

Without Docker, use `PORT=8080 SCHEMA_DIR=tmp/schemas sbt` then `jetty:start`.

## Endpoints

```
POST    /schema/SCHEMAID        - Upload a JSON Schema with unique `SCHEMAID`
GET     /schema/SCHEMAID        - Download a JSON Schema with unique `SCHEMAID`

POST    /validate/SCHEMAID      - Validate a JSON document against the JSON Schema identified by `SCHEMAID`
```
