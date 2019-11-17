package com.github.rmaestroni.json_validator.dao

import java.io.Reader

import com.fasterxml.jackson.databind.JsonNode
import com.github.rmaestroni.json_validator.model.SchemaValidator

import scala.util.Try

trait SchemaDao {
  def find(id: String): Try[SchemaValidator]
  def read(id: String): Try[Reader]
  def save(id: String, schema: JsonNode): Try[Unit]
}
