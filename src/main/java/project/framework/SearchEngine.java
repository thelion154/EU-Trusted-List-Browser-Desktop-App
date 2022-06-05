package project.framework;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class SearchEngine {
    private Map<Provider, Vector<Service>> searchResults;
    private static SearchEngine instance;

    private SearchEngine() { clearSearchResults(); }
    private void clearSearchResults() {
        searchResults = new HashMap<>();
    }

    private void addService(Provider p, Service s) {
        if (searchResults.get(p) == null) {
            Vector<Service> tmp = new Vector<>();
            tmp.add(s);
            searchResults.put(p, tmp);
        }
        else searchResults.get(p).add(s);
    }

    public static SearchEngine getInstance() {
        if (instance == null) instance = new SearchEngine();
        return instance;
    }

    public void performSearch(SearchCriteria criteria) {
        clearSearchResults();
        for (Provider p : criteria.getProviders()) {
            Service[] ser = p.getServices();
            for (Service s : ser) {
                if (criteria.getCountries().contains(s.getCountryCode()) &&
                        criteria.getStatuses().contains(s.getCurrentStatus())) {
                    Vector<String> types = criteria.getTypes();
                    for (String type : s.getServiceTypes()) {
                        if (types.contains(type)) {
                            addService(p, s);
                            break;
                        }
                    }
                }
            }
        }
    }

    public Map<Provider, Vector<Service>> getSearchResults() { return searchResults; }
}
