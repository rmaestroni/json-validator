package com.github.rmaestroni.json_validator.json_util

import java.util.Map.Entry

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}

import scala.collection.JavaConverters._

class RemoveNullKeys extends (JsonNode => JsonNode) {
  private val mapper = new ObjectMapper()

  override def apply(node: JsonNode): JsonNode = clone(node)

  private def clone(root: JsonNode): JsonNode = root match {
    case node if node.isObject =>
      node
        .fields().asScala
        .foldLeft(mapper.createObjectNode())(copyFields)
    case node if node.isArray =>
      node
        .elements().asScala
        .map(element => clone(element))
        .foldLeft(mapper.createArrayNode())((ary, element) => ary.add(element))
    case node => node.deepCopy()
  }

  private def copyFields(receiver: ObjectNode, entry: Entry[String, JsonNode]): ObjectNode = {
    val node = entry.getValue
    if (!node.isNull) { receiver.set(entry.getKey, clone(node)) }
    receiver
  }
}

object RemoveNullKeys {
  def apply(node: JsonNode): JsonNode = new RemoveNullKeys()(node)
}
