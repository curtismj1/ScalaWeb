/**
  * Created by Michael Curtis on 2/11/2016.
  */
package PageSummary
import scala.collection.mutable.ListBuffer

trait Weighted[A] {
  val items: Iterable[A]
  def weightingFn: A => Double
  def weights: Iterable[Double] = {
    return for(item <- items) yield weightingFn(item)
  }
  //TODO Ask about whether or not this function is correct. Seems off
  def sumIf(p: A => Boolean): Double = {
    var d = 0.0
    val temp = weights.toArray
    for((item, index: Int) <- items.zipWithIndex ){
      if(p(item)){
          d += temp(index)
      }
    }
    return d
  }
  def totalWeight(): Double = {
    var returnMe = 0.0
    for (weight <- weights){
      returnMe += weight
    }
    return returnMe
  }
}

class PageSummary(incURL: String, incTerms: List[String]) {
  val url = incURL
  val terms = incTerms
  def fracMatching(term: String): Float = {
    var i = 0.0f
    for(t <- terms){
      if(term == t){
        i += 1.0f
      }
    }
    return i / terms.size
  }
  def fracMatching(incTerms: List[String]): Float = {
    var i = 0.0f
    for(t1 <- incTerms){
      for(t <- terms){
        if(t == t1){
          i += 1.0f
        }
      }
    }
    return i / terms.size
  }
}
//TODO Ask about how IndexedPages is supposed to work
class IndexedPages() extends Iterable[PageSummary]{
  var indexedPages = new ListBuffer[PageSummary]
  override def iterator = indexedPages.iterator
  //TODO test this and make sure that "snowflake" doesn't return positive for "snow"
  def numContaining(word: String): Double = {
    var count = 0.0
    for(page <- indexedPages){
      if(page.terms.contains(word)){
        count += 1.0
      }
    }
    return count
  }
}