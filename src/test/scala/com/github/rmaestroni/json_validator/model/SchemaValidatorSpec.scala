package com.github.rmaestroni.json_validator.model

import com.fasterxml.jackson.databind.ObjectMapper
import org.scalatest.FunSpec

import scala.io.Source

class SchemaValidatorSpec extends FunSpec {
  private val validator = {
    val mapper = new ObjectMapper()
    val in = Source.fromResource("fixtures/sample-schema.json").bufferedReader()
    SchemaValidator(mapper.readTree(in))
  }

  describe("validate") {
    describe("with malformed json") {
      it("throws ParsingException") {
        assertThrows[ParsingException] {
          validator.validate("some-malformed-json")
        }
      }
    }

    describe("when json doc does not comply with schema") {
      it("reports a failure") {
        val report = validator.validate("{}")
        assert(!report.isSuccess)
      }
    }

    describe("when json doc complies with schema") {
      it("reports a success") {
        val report = validator.validate(""" { "source": "foo", "destination": "bar" } """)
        assert(report.isSuccess)
      }
    }

    describe("when json doc contains null keys") {
      it("removes the keys before validating") {
        val reportOk = validator.validate(""" { "source": "foo", "destination": "bar", "some-null-key": null } """)
        assert(reportOk.isSuccess)

        val reportFailure = validator.validate(""" { "source": "foo", "destination": null } """)
        assert(!reportFailure.isSuccess)
      }
    }
  }
}
