/**
  * Created by Michael Curtis on 3/12/2016.
  */

import PageSummary.{IndexedPages, PageSummary, Weighted}

class WeightedIndexedPages extends IndexedPages with Weighted[PageSummary]{
  val items = indexedPages
  def weightingFn(page: PageSummary): Double = {
    return 0.0
  }
}
