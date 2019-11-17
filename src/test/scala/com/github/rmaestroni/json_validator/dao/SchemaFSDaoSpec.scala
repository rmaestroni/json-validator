package com.github.rmaestroni.json_validator.dao

import java.io.IOException
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{FileVisitResult, Files, Path, SimpleFileVisitor}

import com.fasterxml.jackson.databind.ObjectMapper
import org.scalatest.Outcome
import org.scalatest.fixture._

import scala.io.Source

class SchemaFSDaoSpec extends FunSpec {
  private val sampleSchemaDoc = {
    val mapper = new ObjectMapper()
    val input = Source.fromResource("fixtures/sample-schema.json").bufferedReader()
    mapper.readTree(input)
  }

  case class FixtureParam(dao: SchemaFSDao)

  override def withFixture(test: OneArgTest): Outcome = {
    val tempDir = Files.createTempDirectory("fs-dao-test")
    val dao = SchemaFSDao(tempDir)
    try test(FixtureParam(dao))
    finally tearDown(tempDir)
  }

  describe("save") {
    it("creates a schema") { fixtureParam =>
      val dao = fixtureParam.dao

      dao.save("schema-id", sampleSchemaDoc)

      val result = dao.find("schema-id")
      assert(result.isSuccess)

      val schema = result.get
      assertResult(sampleSchemaDoc)(schema.schemaDoc)
    }
  }

  describe("find") {
    describe("when not found") {
      it("returns a failure") { fixtureParam =>
        val dao = fixtureParam.dao
        val result = dao.find("does-not-exist")
        assert(result.isFailure)
      }
    }
  }

  describe("read") {
    describe("when not found") {
      it("returns a failure") { fixtureParam =>
        val dao = fixtureParam.dao
        val result = dao.read("does-not-exist")
        assert(result.isFailure)
      }
    }
  }

  private def tearDown(tempDir: Path): Unit = {
    // recursively delete temporary directory
    Files.walkFileTree(tempDir, new SimpleFileVisitor[Path]() {
      override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
        Files.delete(file)
        FileVisitResult.CONTINUE
      }

      override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = {
        Files.delete(dir)
        FileVisitResult.CONTINUE
      }
    })
  }
}
