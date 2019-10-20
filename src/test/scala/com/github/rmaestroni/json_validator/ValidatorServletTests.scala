package com.github.rmaestroni.json_validator

import org.scalatra.test.scalatest._

class ValidatorServletTests extends ScalatraFunSuite {

  addServlet(classOf[ValidatorServlet], "/*")

  test("GET / on ValidatorServlet should return status 200") {
    get("/") {
      status should equal (200)
    }
  }

}
