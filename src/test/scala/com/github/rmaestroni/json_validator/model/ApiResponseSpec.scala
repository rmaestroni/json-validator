package com.github.rmaestroni.json_validator.model

import java.util

import org.scalatest.FunSpec
import com.github.fge.jsonschema.core.report.{AbstractProcessingReport, LogLevel, ProcessingMessage, ProcessingReport}
import collection.JavaConverters._

class ApiResponseSpec extends FunSpec {
  describe("getSchemaNotFound") {
    it("returns a failure") {
      val resp = ApiResponse.getSchemaNotFound("some-id")
      assertResult("getSchema")(resp.action)
      assertResult("some-id")(resp.id)
      assertResult("error")(resp.status)
      assertResult("Not found")(resp.message.get)
    }
  }

  describe("uploadSchema") {
    describe("with a blank error message") {
      it("returns a success") {
        val resp = ApiResponse.uploadSchema("some-id")
        assertResult("uploadSchema")(resp.action)
        assertResult("some-id")(resp.id)
        assertResult("success")(resp.status)
        assertResult(None)(resp.message)
      }
    }

    describe("with an error message") {
      it("returns a failure") {
        val resp = ApiResponse.uploadSchema("some-id", "error-msg")
        assertResult("uploadSchema")(resp.action)
        assertResult("some-id")(resp.id)
        assertResult("error")(resp.status)
        assertResult("error-msg")(resp.message.get)
      }
    }
  }

  describe("validateDocument") {
    describe("with a blank error message") {
      it("returns a success") {
        val resp = ApiResponse.validateDocument("some-id")
        assertResult("validateDocument")(resp.action)
        assertResult("some-id")(resp.id)
        assertResult("success")(resp.status)
        assertResult(None)(resp.message)
      }
    }

    describe("with an error message") {
      it("returns a failure") {
        val resp = ApiResponse.validateDocument("some-id", "error-msg")
        assertResult("validateDocument")(resp.action)
        assertResult("some-id")(resp.id)
        assertResult("error")(resp.status)
        assertResult("error-msg")(resp.message.get)
      }
    }

    describe("with a ProcessingReport") {
      describe("when report has no messages") {
        val report = buildProcessingReport()

        it("returns a success") {
          val resp = ApiResponse.validateDocument("id", report)
          assertResult("validateDocument")(resp.action)
          assertResult("id")(resp.id)
          assertResult("success")(resp.status)
          assertResult(None)(resp.message)
        }
      }

      describe("when report has a message") {
        val report = buildProcessingReport(buildMessage("key", "value", "some-err-msg"))

        it("returns a failure and provides the report error") {
          val resp = ApiResponse.validateDocument("id", report)
          assertResult("validateDocument")(resp.action)
          assertResult("id")(resp.id)
          assertResult("error")(resp.status)
          assertResult("some-err-msg")(resp.message.get)
        }
      }

      describe("when report has multiple messages") {
        val report = buildProcessingReport(
          buildMessage("k1", "v1", "some-err-msg"),
          buildMessage("k2", "v2", "some-other-err-msg")
        )

        it("returns a failure and concatenates the report errors") {
          val resp = ApiResponse.validateDocument("id", report)
          assertResult("validateDocument")(resp.action)
          assertResult("id")(resp.id)
          assertResult("error")(resp.status)
          assertResult("some-err-msg\nsome-other-err-msg")(resp.message.get)
        }
      }
    }
  }

  private def buildProcessingReport(messages: ProcessingMessage*): ProcessingReport = {
    new AbstractProcessingReport() {
      override def log(level: LogLevel, message: ProcessingMessage): Unit = ???

      override def iterator(): util.Iterator[ProcessingMessage] = messages.iterator.asJava
    }
  }

  private def buildMessage(key: String, value: String, msg: String): ProcessingMessage = {
    val message = new ProcessingMessage()
    message.put(key, value)
    message.setMessage(msg)

    message
  }
}
