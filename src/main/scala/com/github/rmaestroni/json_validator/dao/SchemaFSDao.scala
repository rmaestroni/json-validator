package com.github.rmaestroni.json_validator.dao
import java.io.Reader
import java.nio.file.{FileSystems, Files, Path}

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.github.rmaestroni.json_validator.model.SchemaValidator

import scala.util.Try

class SchemaFSDao(val containerDir: Path) extends SchemaDao {
  private val mapper = new ObjectMapper()

  override def find(id: String): Try[SchemaValidator] = {
    read(id)
      .map(mapper.readTree)
      .map(SchemaValidator(_))
  }

  override def read(id: String): Try[Reader] = {
    Try(Files.newBufferedReader(schemaFilePath(id)))
  }

  override def save(id: String, json: JsonNode): Try[Unit] = {
    Try(Files.newBufferedWriter(schemaFilePath(id)))
      .map(mapper.writeValue(_, json))
  }

  private def schemaFilePath(id: String) = containerDir.resolve(id)
}

object SchemaFSDao {
  def apply(basePath: Path): SchemaFSDao = new SchemaFSDao(basePath)
  def apply(basePath: String): SchemaFSDao = this(FileSystems.getDefault.getPath(basePath))
}
