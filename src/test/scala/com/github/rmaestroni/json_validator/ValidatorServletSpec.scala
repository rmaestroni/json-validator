package com.github.rmaestroni.json_validator

import com.github.rmaestroni.json_validator.dao.{SchemaDao, SchemaFSDao}
import org.scalatra.test.scalatest._

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
      override val schemaDao: SchemaDao = dao
    }
  }
}
