import java.io.{File, PrintWriter}
import scala.io.Source
import scala.sys.process._

object CapScriptEngine {

  // ---- tiny JSON parser: flat { "KEY": "value", ... } only ----
  private def parseFlatJson(raw: String): List[(String, String)] = {
    val s = raw.trim
    def fail(msg: String): Nothing = throw new RuntimeException(s"keywords.json: $msg")

    var i = 0
    def skipWs(): Unit = while (i < s.length && s(i).isWhitespace) i += 1

    def readString(): String = {
      skipWs()
      if (i >= s.length || s(i) != '"') fail("expected a string")
      i += 1
      val b = new StringBuilder
      while (i < s.length && s(i) != '"') {
        if (s(i) == '\\') {
          i += 1
          if (i >= s.length) fail("cut-off escape")
          s(i) match {
            case 'n'  => b += '\n'; i += 1
            case 't'  => b += '\t'; i += 1
            case 'r'  => b += '\r'; i += 1
            case 'b'  => b += '\b'; i += 1
            case 'f'  => b += '\f'; i += 1
            case '"'  => b += '"';  i += 1
            case '\\' => b += '\\'; i += 1
            case '/'  => b += '/';  i += 1
            case 'u'  =>
              if (i + 4 >= s.length) fail("bad \\u escape")
              b += Integer.parseInt(s.substring(i + 1, i + 5), 16).toChar
              i += 5
            case c    => fail(s"unknown escape \\$c")
          }
        } else { b += s(i); i += 1 }
      }
      if (i >= s.length) fail("unterminated string")
      i += 1
      b.toString
    }

    skipWs()
    if (i >= s.length || s(i) != '{') fail("root must be a { } object")
    i += 1
    skipWs()
    if (i < s.length && s(i) == '}') { i += 1; return Nil }

    val out = scala.collection.mutable.ListBuffer.empty[(String, String)]
    while (true) {
      val k = readString()
      skipWs()
      if (i >= s.length || s(i) != ':') fail("expected ':'")
      i += 1
      val v = readString()
      out += ((k, v))
      skipWs()
      if (i < s.length && s(i) == ',') i += 1
      else if (i < s.length && s(i) == '}') { i += 1; return out.toList }
      else fail("expected ',' or '}'")
    }
    Nil
  }

  // ---- translation ----
  private val StrLit = java.util.regex.Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\"")

  private def swapKeywords(chunk: String, dict: List[(String, String)]): String =
    dict.foldLeft(chunk) { case (acc, (meme, scalaKw)) =>
      acc.replaceAll(
        java.util.regex.Pattern.quote(meme),
        java.util.regex.Matcher.quoteReplacement(scalaKw)
      )
    }

  private def translate(capCode: String, dict: List[(String, String)]): String = {
    val sorted = dict.sortBy(-_._1.length) // longest first: "NO CAP" beats "CAP"
    val m = StrLit.matcher(capCode)
    val sb = new StringBuffer
    var last = 0
    while (m.find()) {
      sb.append(swapKeywords(capCode.substring(last, m.start()), sorted))
      sb.append(m.group()) // string literals stay untouched
      last = m.end()
    }
    sb.append(swapKeywords(capCode.substring(last), sorted))
    sb.toString
  }

  def main(args: Array[String]): Unit = {
    if (args.length < 1) {
      println("SKILL ISSUE: Please provide a .cap file! Example: caps script.cap")
      sys.exit(1)
    }
    val capFilePath = args(0)
    val dictPath    = if (args.length > 1) args(1) else "/content/keywords.json"

    val capCode = try {
      val src = Source.fromFile(capFilePath)
      val content = src.getLines().mkString("\n")
      src.close()
      content
    } catch {
      case _: Exception =>
        println(s"SKILL ISSUE: Could not read file $capFilePath")
        sys.exit(1)
    }

    val rawJson = try {
      val src = Source.fromFile(dictPath)
      val content = src.mkString
      src.close()
      content
    } catch {
      case _: Exception =>
        println(s"SKILL ISSUE: Could not read dictionary $dictPath")
        sys.exit(1)
    }

    val dict = try parseFlatJson(rawJson) catch {
      case e: Exception =>
        println(s"SKILL ISSUE (bad dictionary): ${e.getMessage}")
        sys.exit(1)
    }
    println(s"Loaded ${dict.size} brainrot keywords from $dictPath")

    val scalaCode = translate(capCode, dict)

    val tempFile = File.createTempFile("cap_output_", ".scala", new File("/tmp"))
    tempFile.deleteOnExit()
    val writer = new PrintWriter(tempFile)
    writer.write(scalaCode)
    writer.close()

    try s"scala3 ${tempFile.getAbsolutePath}".!!.trim
    catch {
      case e: Exception =>
        println(s"SKILL ISSUE (Compile Error):\n${e.getMessage}")
        return
    }

    try print("java -cp /opt/scala3/*:. MemeApp".!!.trim)
    catch {
      case e: Exception =>
        println(s"SKILL ISSUE (Runtime Error):\n${e.getMessage}")
    }
  }
}
