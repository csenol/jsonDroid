package csenol

import scala.util.Try

package object json {
  type JsonReader = com.google.gson.stream.JsonReader
  type JsonWriter = com.google.gson.stream.JsonWriter

  implicit class JsonWriterWrapper(val jwriter: JsonWriter) extends AnyVal {
    def write[T: JsonWriterT](name: String, t: T): JsonWriter = {
      val imp = implicitly[JsonWriterT[T]]
      imp.wrapWrite(name, t, jwriter)
    }
  }

  implicit class JsonReaderWrapper(val jreader: JsonReader) extends AnyVal {

    def getAs[T: JsonReaderT]: Option[T] = Try {
      implicitly[JsonReaderT[T]].wrapRead(jreader)
    }.toOption

    def hasNext = jreader.hasNext
    def nextName = jreader.nextName
    def beginObject = jreader.beginObject
    def endObject = jreader.endObject
  }

}

package json {

}
