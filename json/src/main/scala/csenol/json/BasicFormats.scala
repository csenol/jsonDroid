package csenol.json

trait BasicFormats {

  trait NoWrapped[T] extends JsonFormat[T] {
    abstract override def wrapRead(in: JsonReader) = read(in)
    abstract override def wrapWrite(name: String, t: T, out: JsonWriter) = {
      out.name(name)
      write(t, out)
    }
    abstract override def wrapWrite(t: T, out: JsonWriter) = {
      write(t, out)
    }
  }

  implicit object stringFormat extends JsonFormat[String] with NoWrapped[String] {
    def read(in: JsonReader): String = in.nextString
    def write(t: String, out: JsonWriter) = out.value(t)
  }

  implicit object intFormat extends JsonFormat[Int] with NoWrapped[Int] {
    def read(in: JsonReader): Int = in.nextInt
    def write(t: Int, out: JsonWriter) = out.value(t)
  }

  implicit object longFormat extends JsonFormat[Long] with NoWrapped[Long] {
    def read(in: JsonReader) = in.nextLong
    def write(t: Long, out: JsonWriter) = out.value(t)
  }

  implicit object doubleFormat extends JsonFormat[Double] with NoWrapped[Double] {
    def read(in: JsonReader): Double = in.nextDouble
    def write(t: Double, out: JsonWriter) = out.value(t)
  }

  implicit object boolenFormat extends JsonFormat[Boolean] with NoWrapped[Boolean] {
    def read(in: JsonReader): Boolean = in.nextBoolean
    def write(t: Boolean, out: JsonWriter) = out.value(t)
  }

}
