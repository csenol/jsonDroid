package csenol

import scala.util.Try

package object json {
  type JsonReader = com.google.gson.stream.JsonReader
  type JsonWriter = com.google.gson.stream.JsonWriter

  implicit class JsonWriterWrapper(val jwriter: JsonWriter) extends AnyVal {
    def write[T: JsonWriterT](name: String, t: T): JsonWriter = {
      val imp = implicitly[JsonWriterT[T]]
      imp.namedWrite(name, t, jwriter)
    }

    def write[T: JsonWriterT](t: T): JsonWriter = {
      val imp = implicitly[JsonWriterT[T]]
      imp.rootWrite(t, jwriter)
    }
  }

  implicit class JsonReaderWrapper(val jreader: JsonReader) extends AnyVal {

    def getAsTry[T: JsonReaderT]: Try[T] = Try {
      implicitly[JsonReaderT[T]].rootRead(jreader)
    }

    def getAs[T: JsonReaderT]: Option[T] = getAsTry[T].toOption

    def hasNext = jreader.hasNext
    def nextName = jreader.nextName
    def beginObject = jreader.beginObject
    def endObject = jreader.endObject
  }

}
