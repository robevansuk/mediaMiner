package uk.robevans.twitter

import org.junit.Test
import spock.lang.Specification

/**
 * Created by robevans.uk on 27/07/2017.
 */
class TwitterStreamTest extends Specification {

    TwitterStream testObject

    @Test
    "should return individual search terms as a single string with no spaces" (){
        given:
        List<String> terms = Arrays.asList("foo,bar", "buzz")
        SearchTerms searchTerms = new SearchTerms(terms)

        when:
        testObject = new TwitterStream(searchTerms)

        then:
        testObject.getSearchTerms().size() == 2
        testObject.getIndividualSearchTerms().size() == 3
    }

    @Test
    "should set up positive/negative counters for individual search terms"(){
        given:
        List<String> terms = Arrays.asList("foo,bar", "buzz")
        SearchTerms searchTerms = new SearchTerms(terms)

        when:
        testObject = new TwitterStream(searchTerms)

        then:
        testObject.getPositiveCounts().size() == 2
        testObject.getNegativeCounts().size() == 2
    }

    @Test
    "should set up index counters for individual search terms"(){
        given:
        List<String> terms = Arrays.asList("foo,bar", "buzz")
        SearchTerms searchTerms = new SearchTerms(terms)

        when:
        testObject = new TwitterStream(searchTerms)

        then: "we should see 3 keys pointing to only 2 indexes"
        testObject.getIndexForSearchTerm().keySet().size() == 3
        testObject.getIndexForSearchTerm().get("foo") == 0
        testObject.getIndexForSearchTerm().get("bar") == 0
        testObject.getIndexForSearchTerm().get("buzz") == 1
    }
}