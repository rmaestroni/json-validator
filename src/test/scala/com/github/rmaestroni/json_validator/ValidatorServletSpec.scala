package com.github.rmaestroni.json_validator

import java.io.{FileNotFoundException, Reader}

import com.fasterxml.jackson.databind.JsonNode
import com.github.rmaestroni.json_validator.dao.{SchemaDao, SchemaFSDao}
import com.github.rmaestroni.json_validator.model.SchemaValidator
import org.junit.{Ignore, Test}
import org.scalatra.test.scalatest._

import scala.util.{Failure, Try}

class ValidatorServletSpec extends ScalatraSpec {

  describe("GET /schema/:id") {
    describe("when not found") {
      addServlet(stubServlet(SchemaFSDao("/tmp/foo")), "/*")

      it("returns 404") {
        get("/schema/xyz") {
          assertResult(404)(status)
        }
      }
    }
  }

  private def stubServlet(dao: SchemaDao): ValidatorServlet = {
    new ValidatorServlet {
      override val schemaDao = dao
    }
  }
}
