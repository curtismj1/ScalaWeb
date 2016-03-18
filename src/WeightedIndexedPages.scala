/**
  * Created by Michael Curtis on 3/12/2016.
  */
package WeightedIndexedPages
import PageSummary.{IndexedPages, PageSummary, Weighted}
import Query._
import SearchResults.SearchResults
class WeightedIndexedPages extends IndexedPages with Weighted[PageSummary]{
  //This is called the "grover weighting function"
  val weightingFn = (page: PageSummary) => {
    //because who doesn't love random constants in their code?
    var rank: Double = 100 - page.url.length
    for(i <- page.terms){
      if(i.toLowerCase() == "god" || i.toLowerCase() == "jesus" || i.toLowerCase() == "holy spirit"){
        rank += 1
      }
    }
    rank
  }
  //Note: I ran into the problem where results is a val and therefore sealed, so you
  //can't update it from within search. Therefore, I did pattern matching within the
  //results function to account for both the WeightedIndexedPages as well as the weighted query.
  //In hind sight, I think the "results" function should only pattern match and call an appropriate
  //sealed helper function, or at least modify a collection that we have access to if changes need
  //to be made.
  override def search(q: Query): SearchResults = {
    return super.search(q)
  }
}
