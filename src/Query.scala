/**
  * Created by Michael Curtis on 3/16/2016.
  */
package Query
import PageSummary._
import Math._
class Query(queryTerms: Iterable[String]) {

}
class WeightedQuery(querTerms: Iterable[String]) extends Query(querTerms) with Weighted[String]{
  override val items: Iterable[String] = querTerms
  override val weightingFn = (input: String) => {0.0}
  //TODO Make sure this overrides correctly
  //Weights the middle most terms the lowest and the tail ends the highest.
  override def weights: Iterable[Double] = {
    return for((item, index) <- items.zipWithIndex)yield abs(items.size - index).toDouble
  }
}