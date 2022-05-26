package project.framework;

import java.util.Vector;

public class SearchEngine {
    private Vector<Service> searchResults;
    private static SearchEngine instance;

    SearchEngine() {
        clearSearchResults();
    }

    private void clearSearchResults() {searchResults = new Vector<>();}

    public static SearchEngine getInstance() {
        if (instance == null) instance = new SearchEngine();
        return instance;
    }

    public void performSearch(Vector<String> countries, Vector<Provider> providers, Vector<String> types, Vector<String> statuses) {
        clearSearchResults();
        for (Provider p : providers) {
            for (Service s : p.getServices()) {
                if (countries.contains(s.getCountryCode())
                        && statuses.contains(s.getCurrentStatus())) {
                    String[] t = s.getServiceTypes();
                    for (String type : t) {
                        if (types.contains(type)) {
                            searchResults.add(s);
                            break;
                        }
                    }
                }
            }
        }
    }

    public Vector<Service> getSearchResults() {
        return searchResults;
    }
}
