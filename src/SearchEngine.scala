/**
  * Created by Michael Curtis on 2/10/2016.
  */
import org.apache.http._
import org.apache.http.client.entity._
import org.apache.http.client.methods._
import org.apache.http.impl.client._
import org.apache.http.client.utils._
import org.apache.http.message._
import org.apache.http.params._
import PageSummary._
import scala.collection.mutable.ListBuffer
import scala.util.Random
import scala.util.matching.Regex

object SearchEngine extends App{
  def parseArgs(args: Array[String]): Map[String, List[String]] = {

    def nameValuePair(paramName: String) = {
      def values(commaSeperatedValues: String) = commaSeperatedValues.split(",").toList

      val index = args.indexWhere(_ == paramName)
      (paramName, if(index == -1) Nil else values(args(index + 1)))
    }

    Map(nameValuePair("-d"), nameValuePair("-h"))
  }
  def splitByEqual(nameValue:String): Array[String] = nameValue.split('=')
  def headers = for(nameValue <- params("-h")) yield {
    def tokens = splitByEqual(nameValue)
    new BasicHeader(tokens(0), tokens(1))
  }
  def getLinks( html : String , baseURL : String) : List[String] = {
    // See http://www.mkyong.com/regular-expressions/how-to-extract-html-links-with-regular-expression/ for explanation of regex
    val aTagRegex = """(?i)<a([^>]+)>(.+?)</a>""".r
    val urlRegex = """\s*(?i)href\s*=\s*(\"([^"]*\")|'[^']*'|([^'">\s]+))""".r

    val opts = for ( a <- aTagRegex findAllMatchIn html ) yield urlRegex.findFirstMatchIn(a.toString)

    val hrefs = opts collect { case Some(x) => x group 1 }

    // remove leading and trailing quotes, if any
    val cleaned = hrefs map { _.stripPrefix("\"").stripPrefix("\'").stripSuffix("\"").stripPrefix("\'") } filter { ! _.startsWith("javascript") }

    // Use Java's URL class to parse the URL
    //   and get the full URL string (including implied context)
    val contextURL = new java.net.URL(baseURL)

    def getURL(x: String) = {
      var result = ""
      try {
        result = new java.net.URL(contextURL, x).toString()
      }
      catch {
        case e: java.net.MalformedURLException => Unit
      }
      result
    }

    (cleaned map { getURL(_) } ).filter(_.length > 0).toList
  }
  def fetch(url:String): String  = {
    val httpget = new HttpGet(s"${url}")
    headers.foreach { httpget.addHeader(_) }
    try {
      val responseBody = new DefaultHttpClient().execute(httpget, new BasicResponseHandler())
      return responseBody;
    }
    catch{
      case _: org.apache.http.client.HttpResponseException => val responseBody = ""
        return responseBody
    }
  }
  def crawlAndIndex(url: String, pages: Int, mode: String = "read", weight: Boolean = true): IndexedPages = {
    //var x = List[PageSummary]()

    var x = if(mode == "augment") {
      new IndexedPages with Augmentable[PageSummary]
    }
    else {
      new IndexedPages
    }

    val links = getLinks(fetch(url), url).distinct
    val z = new PageSummary(url, getTerms(fetch(url), testFilter))
    if(pages > 1){
      if(links.size > 0){
        var index =  0
        while(x.pages.size < pages && index < links.size) {
          x.pages = ListBuffer[PageSummary](z) ++ crawlAndIndex(links(index), pages - 1).pages
          index += 1
        }
      }
      else{
        x.pages = ListBuffer[PageSummary](z)
      }
      return x
    }
    else{
      x.pages = ListBuffer[PageSummary](z)
      return x
    }
  }
  def formEntity = {
    def toJavaList(scalaList: List[BasicNameValuePair]) = {
      java.util.Arrays.asList(scalaList.toArray:_*)
    }

    def formParams = for(nameValue <- params("-d")) yield {
      def tokens = splitByEqual(nameValue)
      new BasicNameValuePair(tokens(0), tokens(1))
    }

    def formEntity = new UrlEncodedFormEntity(toJavaList(formParams), "UTF-8")
    formEntity
  }
  def getTerms(html: String, f: String => Boolean): List[String] = {
    return (for(c <- (new Regex("(_|\\w)*")).findAllIn(html).mkString(",").split(",") if c != ""; if f(c)) yield c)(collection.breakOut)
  }
  def testFilter(input: String): Boolean = {
    if(input.length > 0){
      return true
    }
    return false
  }
  def handlePostRequest = {
    val httppost = new HttpPost(url)
    headers.foreach { httppost.addHeader(_) }
    httppost.setEntity(formEntity)
    val responseBody = new DefaultHttpClient().execute(httppost, new BasicResponseHandler())
    println(responseBody)
  }
  def handleGetRequest = {
    val query = params("-d").mkString("&")
    val httpget = new HttpGet(s"${url}?${query}")
    headers.foreach { httpget.addHeader(_) }
    val responseBody = new DefaultHttpClient().execute(httpget, new BasicResponseHandler())
    println(responseBody)
  }
  def handleDeleteRequest = {
    val httpDelete = new HttpDelete(url)
    val httpResponse = new DefaultHttpClient().execute(httpDelete)
    println(httpResponse.getStatusLine())
  }
  def handleOptionsRequest = {
    val httpOptions = new HttpOptions(url)
    headers.foreach { httpOptions.addHeader(_) }
    val httpResponse = new DefaultHttpClient().execute(httpOptions)
    println(httpOptions.getAllowedMethods(httpResponse))
  }

  require(args.size >= 2, "at minimum you should specify action(post, get, delete, options) and url")
  val command = args.head
  val params = parseArgs(args)
  val url = args.last

  command match {
    case "post"    => handlePostRequest
    case "get"     => handleGetRequest
    case "delete"  => handleDeleteRequest
    case "options" => handleOptionsRequest
    case "fetch" => val x = getTerms(fetch(url), testFilter)
      for(c <- x){
        println(c)
      }
    case "crawl" => val x = crawlAndIndex(url, 10)
      for(c <- x){
        println("New Page")
        for(b <- c.terms){
          print(b + " ")
        }
      }
  }


}