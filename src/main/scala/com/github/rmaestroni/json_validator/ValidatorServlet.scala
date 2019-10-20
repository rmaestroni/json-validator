package com.github.rmaestroni.json_validator

import org.scalatra._

class ValidatorServlet extends ScalatraServlet {

  get("/") {
    views.html.hello()
  }

}
