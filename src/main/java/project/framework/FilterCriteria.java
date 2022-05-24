package project.framework;

import java.util.Vector;

public class FilterCriteria {
    private Vector<String> countries;
    private Vector<Provider> providers;
    private Vector<String> types;
    private Vector<String> statuses;

    public FilterCriteria(Vector<String> c, Vector<Provider> p, Vector<String> t, Vector<String> s) {
        countries = c;
        providers = p;
        types = t;
        statuses = s;
    }

    public Vector<String> getCountries() {
        return countries;
    }

    public Vector<Provider> getProviders() {
        return providers;
    }

    public Vector<String> getTypes() {
        return types;
    }

    public Vector<String> getStatuses() {
        return statuses;
    }
}