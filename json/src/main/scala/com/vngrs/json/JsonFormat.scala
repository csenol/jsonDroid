package com.vngrs.json

import annotation.implicitNotFound

@implicitNotFound(msg = "Cannot find JsonReaderT or JsonFormat type class for ${T}")
trait JsonReaderT[T] {

  def read(in: JsonReader): T

  private[json] def rootRead(in: JsonReader): T = {
    in.beginObject
    val t = read(in)
    in.endObject
    t
  }
}

@implicitNotFound(msg = "Cannot find JsonWriterT or JsonFormat type class for ${T}")
trait JsonWriterT[T] {

  def write(t: T, out: JsonWriter): JsonWriter

  private[json] def namedWrite(name: String, t: T, out: JsonWriter): JsonWriter = {
    out.name(name)
    rootWrite(t, out)
  }

  private[json] def rootWrite(t: T, out: JsonWriter): JsonWriter = {
    out.beginObject
    val o = write(t, out)
    out.endObject
    o
  }
}

trait JsonFormat[T] extends JsonReaderT[T] with JsonWriterT[T]
