package project.framework;

import java.util.Vector;

public class SearchEngine {
    Vector<Service> searchResults;

    SearchEngine() {
        clearSearchResults();
    }

    private void clearSearchResults() {searchResults = new Vector<>();}

    public Vector<Service> performSearch(Vector<String> countries, Vector<Provider> providers, Vector<String> types, Vector<String> statuses) {
        clearSearchResults();
        for (Provider p : providers) {
            for (Service s : p.getServices()) {
                if (countries.contains(s.getCountryCode()) && types.contains(s.getType()) && statuses.contains(s.getCurrentStatus())) {
                    searchResults.add(s);
                }
            }
        }
        return searchResults;
    }

    public Service.ServiceInfo getServiceInfo(int serviceIndex) {
        return searchResults.get(serviceIndex).getServiceInfo();
    }
}
