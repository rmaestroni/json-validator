package com.github.rmaestroni.json_validator.model

case class ApiResponse(
                        action: String,
                        id: String,
                        status: String,
                        message: Option[String]
                      )

object ApiResponse {
  def uploadSchema(id: String, errorMessage: String): ApiResponse = uploadSchema(id, Option(errorMessage))

  def uploadSchema(id: String, errorMessage: Option[String] = None): ApiResponse = {
    val action = "uploadSchema"
    val status = if (errorMessage.isEmpty) "success" else "error"
    ApiResponse(action, id, status, errorMessage)
  }
}
