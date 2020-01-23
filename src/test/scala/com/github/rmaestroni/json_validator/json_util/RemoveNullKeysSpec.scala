package com.github.rmaestroni.json_validator.json_util

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.scalatest.FunSpec

class RemoveNullKeysSpec extends FunSpec {
  private val mapper = new ObjectMapper()

  describe("apply") {
    it("copies the object") {
      val json = parseJson("{\"foo\":\"bar\", \"ary\":[1, \"baz\", 2]}")
      val copy = RemoveNullKeys(json)
      assertResult("bar")(copy.get("foo").asText())

      val ary = copy.get("ary")
      assert(ary.isArray)
      assertResult(1)(ary.get(0).asInt())
      assertResult("baz")(ary.get(1).asText())
      assertResult(2)(ary.get(2).asInt())
    }

    describe("when given a json array") {
      it("copies the array") {
        val json = parseJson("[[0, 1, {\"a\": [10]}], \"baz\", 2]")
        val copy = RemoveNullKeys(json)
        assert(copy.isArray)

        assertResult("baz")(copy.get(1).asText())
        assertResult(2)(copy.get(2).asInt())

        val subAry = copy.get(0)
        assert(subAry.isArray)
        assertResult(0)(subAry.get(0).asInt())
        assert(subAry.get(2).isObject)
      }
    }

    describe("with some null keys") {
      it("returns a copy with nulls removed") {
        val json = parseJson("{\"foo\":\"bar\", \"baz\": null, \"ary\":[null, \"baz\", 2]}")
        val copy = RemoveNullKeys(json)

        assert(!copy.has("baz"))

        val subAry = json.get("ary")
        assert(subAry.get(0).isNull)
      }
    }
  }

  private def parseJson(json: String): JsonNode = mapper.readTree(json)
}
