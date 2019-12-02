package com.github.rmaestroni.json_validator.model

import com.github.fge.jsonschema.core.report.ProcessingReport
import collection.JavaConverters._

case class ApiResponse(
                        action: String,
                        id: String,
                        status: String,
                        message: Option[String]
                      )

object ApiResponse {
  def getSchemaNotFound(id: String): ApiResponse = {
    build(id, "getSchema", Option("Not found"))
  }

  def uploadSchema(id: String, errorMessage: String): ApiResponse = uploadSchema(id, Option(errorMessage))

  def uploadSchema(id: String, errorMessage: Option[String] = None): ApiResponse = {
    build(id, "uploadSchema", errorMessage)
  }

  def validateDocument(id: String, errorMessage: String): ApiResponse = validateDocument(id, Option(errorMessage))

  def validateDocument(id: String, errorMessage: Option[String] = None): ApiResponse = {
    build(id, "validateDocument", errorMessage)
  }

  def validateDocument(id: String, report: ProcessingReport): ApiResponse = {
    val msg = report
        .asScala
        .map(r => r.getMessage)
        .reduceOption((a, b) => s"$a\n$b")
    validateDocument(id, msg)
  }

  private def build(id: String, action: String, errorMessage: Option[String] = None): ApiResponse = {
    val status = if (errorMessage.isEmpty) "success" else "error"
    ApiResponse(action, id, status, errorMessage)
  }
}
