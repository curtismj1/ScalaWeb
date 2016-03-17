/**
  * Created by Michael Curtis on 3/16/2016.
  */

package SearchResults
import PageSummary._
import Query._
import scala.collection.mutable.ListBuffer
import scala.math
class SearchResults(query: Query, pages: Iterable[PageSummary]) {

  def results: Iterable[(Double, String)] = {
    var weights = Iterable[Double]()
    //THIS IS BAD AND I SHOULD FEEL BAD BUT IT IS FUNNY AND APPARENTLY OK
    query match{
      case i: WeightedQuery => {
        weights = i.weights
      }
      case _ => weights = List.fill(query.queryTerms.size)(1)
    }

      var b = ListBuffer[(Double, String)]()
      var n = 0
      var size = pages.size.toDouble
      for((word, weight) <- query.queryTerms.zip(weights)){
        for(page <- pages){
          if(page.terms.contains(word)){
            n += 1
          }
        }
        b += ((weight * math.log(size/(1.0 + n.toDouble)), word))
      }
    b.sortWith(_._1 > _._1)
  }
  def printTop(n: Int) = {
    val res = results
      for((r, index) <- res.zipWithIndex if index < n){
        println("Score for: " + r._2 + " = " + r._1)
      }
  }
}
