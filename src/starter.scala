//	def getLinks( html : String , baseURL : String) : List[String] = {
//		// See http://www.mkyong.com/regular-expressions/how-to-extract-html-links-with-regular-expression/ for explanation of regex
//		val aTagRegex = """(?i)<a([^>]+)>(.+?)</a>""".r
//		val urlRegex = """\s*(?i)href\s*=\s*(\"([^"]*\")|'[^']*'|([^'">\s]+))""".r
//
//		val opts = for ( a <- aTagRegex findAllMatchIn html ) yield urlRegex.findFirstMatchIn(a.toString)
//
//		val hrefs = opts collect { case Some(x) => x group 1 }
//
//		// remove leading and trailing quotes, if any
//		val cleaned = hrefs map { _.stripPrefix("\"").stripPrefix("\'").stripSuffix("\"").stripPrefix("\'") } filter { ! _.startsWith("javascript") }
//
//		// Use Java's URL class to parse the URL
//		//   and get the full URL string (including implied context)
//		val contextURL = new java.net.URL(baseURL)
//
//		def getURL(x: String) = {
//          var result = ""
//          try {
//            result = new java.net.URL(contextURL, x).toString()
//          }
//          catch {
//            case e: java.net.MalformedURLException => Unit
//          }
//          result
//        }
//
//		(cleaned map { getURL(_) } ).filter(_.length > 0).toList
//	}
//
//
//	def printBest(query : List[String], pages : List[PageSummary]) = {
//		val scores = for(x <- pages) yield (x.url, x.fracMatching(query))
//		for (x <- scores.sortBy(_._2).takeRight(5).reverse) println(x._1 + ": " + x._2.toString)
//	}