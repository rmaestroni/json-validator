package com.github.rmaestroni.json_validator

import com.fasterxml.jackson.databind.JsonNode
import com.github.rmaestroni.json_validator.dao.{SchemaDao, SchemaFSDao}
import com.github.rmaestroni.json_validator.model.ApiResponse
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

import scala.util.{Failure, Success, Try}

class ValidatorServlet extends ScalatraServlet with JacksonJsonSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats
  protected val schemaDao: SchemaDao = SchemaFSDao("/tmp/snowplow")

  before() { contentType = formats("json") }

  get("/schema/:id") {
    val id = params("id")

    schemaDao.read(id) match {
      case Success(reader) =>
        response.setStatus(200)
        response.setCharacterEncoding("UTF-8")
        response.setContentType("application/json")

        reader.transferTo(response.getWriter)

        reader.close()
        response.flushBuffer()
      case Failure(_) =>
        status = 404
        ApiResponse.getSchemaNotFound(id)
    }
  }

  post("/schema/:id") {
    val id = params("id")
    postBody match {
      case Success(jsonNode) =>
        schemaDao.save(id, jsonNode) match {
          case Success(_) =>
            status = 201
            ApiResponse.uploadSchema(id)
          case Failure(exception) =>
            status = 500
            ApiResponse.uploadSchema(id, exception.getMessage)
        }
      case Failure(_) =>
        // failed to parse JSON
        status = 422
        ApiResponse.uploadSchema(id, "Invalid JSON")
    }
  }

  post("/validate/:schemaId") {
    val schemaId = params("schemaId")
    schemaDao.find(schemaId) match {
      case Success(validator) =>
        postBody match {
          case Success(jsonNode) =>
            val report = validator.validate(jsonNode)
            status = if (report.isSuccess) 200 else 422
            ApiResponse.validateDocument(schemaId, report)
          case Failure(_) =>
            // failed to parse JSON
            status = 422
            ApiResponse.validateDocument(schemaId, "Invalid JSON")
        }
      case Failure(_) =>
        status = 404
        ApiResponse.validateDocument(schemaId, "Schema not found")
    }
  }

  private def postBody: Try[JsonNode] = Try(parse(request.getReader)).map(asJsonNode)
}
