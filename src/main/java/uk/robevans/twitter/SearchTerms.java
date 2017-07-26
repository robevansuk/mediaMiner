package uk.robevans.twitter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ren7881 on 25/07/2017.
 */
@Component
@ConfigurationProperties(prefix="twitter")
public class SearchTerms {
    private List<String> searchTerms = new ArrayList<>();

    public List<String> getSearchTerms() {
        return searchTerms;
    }

    public List<String> individualSearchTerms(){
        List<String> individualSearchTerms = new ArrayList<>();
        for (String searchTerm : searchTerms) {
            String[] terms = new String[]{searchTerm};
            if (searchTerm.contains(",")){
                terms = searchTerm.split(",");
            }

            for (String term : terms){
                individualSearchTerms.add(term.trim());
            }
        }
        return individualSearchTerms;
    }
}