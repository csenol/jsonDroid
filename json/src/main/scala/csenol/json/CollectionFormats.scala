package csenol.json

import scala.collection.immutable.Vector

trait CollectionFormats {

  implicit def vectorFormat[T: JsonFormat] = new JsonFormat[Vector[T]] {

    override def wrapRead(in: JsonReader): Vector[T] = {
      in.beginArray
      val t = read(in)
      in.endArray
      t
    }

    override def wrapWrite(name: String, t: Vector[T], out: JsonWriter) = {
      out.name(name)
      out.beginArray()
      val y = write(t, out)
      out.endArray()
      y
    }

    def read(in: JsonReader): Vector[T] = {
      val tReader: JsonReaderT[T] = implicitly[JsonReaderT[T]]
      var buffer = Vector.empty[T]
      while (in.hasNext) {
        buffer = buffer :+ tReader.read(in)
      }
      buffer
    }

    def write(t: Vector[T], out: JsonWriter) = {
      var i = t.size - 1
      val tWriter = implicitly[JsonWriterT[T]]
      while (i >= 0) {
        tWriter.write(t(i), out)
        i -= 1
      }
      out
    }

  }

  import collection.{ immutable => imm }

  implicit def immIterableFormat[T: JsonFormat] = viaVector[imm.Iterable[T], T](list => imm.Iterable(list: _*))
  implicit def immSeqFormat[T: JsonFormat] = viaVector[imm.Seq[T], T](list => imm.Seq(list: _*))
  implicit def immIndexedSeqFormat[T: JsonFormat] = viaVector[imm.IndexedSeq[T], T](list => imm.IndexedSeq(list: _*))
  implicit def immLinearSeqFormat[T: JsonFormat] = viaVector[imm.LinearSeq[T], T](list => imm.LinearSeq(list: _*))
  implicit def immSetFormat[T: JsonFormat] = viaVector[imm.Set[T], T](list => imm.Set(list: _*))

  import collection._

  implicit def iterableFormat[T: JsonFormat] = viaVector[Iterable[T], T](list => Iterable(list: _*))
  implicit def seqFormat[T: JsonFormat] = viaVector[Seq[T], T](list => Seq(list: _*))
  implicit def indexedSeqFormat[T: JsonFormat] = viaVector[IndexedSeq[T], T](list => IndexedSeq(list: _*))
  implicit def linearSeqFormat[T: JsonFormat] = viaVector[LinearSeq[T], T](list => LinearSeq(list: _*))
  implicit def setFormat[T: JsonFormat] = viaVector[Set[T], T](list => Set(list: _*))

  def viaVector[I <: Iterable[T], T: JsonFormat](f: Vector[T] => I): JsonFormat[I] = new JsonFormat[I] {
    override def wrapRead(in: JsonReader): I = {
      in.beginArray
      val t = read(in)
      in.endArray
      t
    }
    override def wrapWrite(name: String, t: I, out: JsonWriter) = {
      out.name(name)
      out.beginArray()
      val y = write(t, out)
      out.endArray()
      y
    }
    def write(iterable: I, out: JsonWriter) = vectorFormat[T].write(iterable.toVector, out)
    def read(in: JsonReader) = f(vectorFormat[T].read(in))
  }

}