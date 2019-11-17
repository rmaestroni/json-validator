package com.github.rmaestroni.json_validator.model

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.github.fge.jsonschema.core.report.ProcessingReport
import com.github.fge.jsonschema.main.JsonSchemaFactory

class SchemaValidator(val schemaDoc: JsonNode) {
  private val mapper = new ObjectMapper()
  private val factory = JsonSchemaFactory.byDefault
  private val schema = factory.getJsonSchema(schemaDoc)

  def validate(document: String): ProcessingReport = {
    try {
      val json: JsonNode = mapper.readTree(document)
      validate(json)
    } catch {
      case e: JsonParseException => throw new ParsingException(e)
    }
  }

  def validate(document: JsonNode): ProcessingReport = {
    schema.validate(document)
  }
}

object SchemaValidator {
  def apply(schemaDoc: JsonNode): SchemaValidator = new SchemaValidator(schemaDoc)
}

class ParsingException(val t: Throwable) extends Exception(t)
