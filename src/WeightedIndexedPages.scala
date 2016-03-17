/**
  * Created by Michael Curtis on 3/12/2016.
  */

import PageSummary.{IndexedPages, PageSummary, Weighted}

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
}
