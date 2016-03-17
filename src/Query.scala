/**
  * Created by Michael Curtis on 3/16/2016.
  */
package Query
import PageSummary._
import Math._
class Query(val queryTerms: Iterable[String]) {

}
class WeightedQuery(queryTerms: Iterable[String]) extends Query(queryTerms) with Weighted[String]{
  override val items: Iterable[String] = queryTerms
  override val weightingFn = (input: String) => {0.0}
  //TODO Make sure this overrides correctly
  //Weights the middle most terms the lowest and the tail ends the highest.
  override def weights: Iterable[Double] = {
    return for((item, index) <- items.zipWithIndex)yield abs(items.size - index).toDouble
  }
}