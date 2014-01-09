package csenol.json

import annotation.implicitNotFound

@implicitNotFound(msg = "Cannot find JsonReaderT or JsonFormat type class for ${T}")
trait JsonReaderT[T] {

  def read(in: JsonReader): T

  private[json] def wrapRead(in: JsonReader): T = {
    in.beginObject
    val t = read(in)
    in.endObject
    t
  }
}

@implicitNotFound(msg = "Cannot find JsonWriterT or JsonFormat type class for ${T}")
trait JsonWriterT[T] {

  def write(t: T, out: JsonWriter): JsonWriter

  private[json] def wrapWrite(name: String, t: T, out: JsonWriter): JsonWriter = {
    out.name(name)
    out.beginObject
    val o = write(t, out)
    out.endObject
    o
  }

  private[json] def wrapWrite(t: T, out: JsonWriter): JsonWriter = {
    out.beginObject
    val o = write(t, out)
    out.endObject
    o
  }
}

trait JsonFormat[T] extends JsonReaderT[T] with JsonWriterT[T]
