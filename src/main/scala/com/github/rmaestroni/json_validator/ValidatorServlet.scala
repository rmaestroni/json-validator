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
    schemaDao.read(params("id")) match {
      case Success(reader) => {
        response.setStatus(200)
        response.setCharacterEncoding("UTF-8")
        response.setContentType("application/json")

        reader.transferTo(response.getWriter)

        reader.close
        response.flushBuffer
      }
      case Failure(exception) => response.sendError(404)
    }
  }

  post("/schema/:id") {
    val id = params("id")
    postBody match {
      case Success(jsonNode) => {
        schemaDao.save(id, jsonNode) match {
          case Success(unit) => {
            status = 201
            ApiResponse.uploadSchema(id)
          }
          case Failure(exception) => {
            status = 500
            ApiResponse.uploadSchema(id, exception.getMessage)
          }
        }
      }
      case Failure(exception) => {
        // failed to parse JSON
        status = 422
        ApiResponse.uploadSchema(id, "Invalid JSON")
      }
    }
  }

  private def postBody: Try[JsonNode] = Try(parse(request.getReader)).map(asJsonNode(_))
}

import scala.util.{Failure, Success, Try}
