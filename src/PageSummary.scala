/**
  * Created by Michael Curtis on 2/11/2016.
  */
package PageSummary

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import Query._
import SearchResults._
trait Weighted[A] {
  val items: Iterable[A]
  val weightingFn: A => Double
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
  def search(q: Query) = {

  }
}

trait Augmentable[A] {
  val items: scala.collection.mutable.Seq[A] with
    scala.collection.generic.Growable[A]

  def add(newItem: A): Boolean = {
    if(items.contains(newItem)){
      return false
    }
    else{
      items += newItem
      return true
    }
  }
}

//TODO Ask about how IndexedPages is supposed to work
class IndexedPages() extends Iterable[PageSummary]{
  var pages = new ListBuffer[PageSummary]
  val items = pages
  override def iterator = pages.iterator
  //TODO test this and make sure that "snowflake" doesn't return positive for "snow"
  def numContaining(word: String): Double = {
    var count = 0.0
    for(page <- pages){
      if(page.terms.contains(word)){
        count += 1.0
      }
    }
    return count
  }
  def search(q: Query) : SearchResults = {
    var s = new SearchResults(q, pages)
    var res = s.results

    s
  }
}