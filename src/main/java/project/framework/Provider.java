package project.framework;

import org.json.JSONArray;
import org.json.JSONObject;

public class Provider {
    private String countryCode, countryName, name, trustmark;
    private String[] serviceTypes;
    private Service[] services;

    /**
     *  Provider constructor; creates a Provider object.
     *
     * @param  jsonSource String with all provider information but in Json format
     *
     * @see    JSONObject
     * @see    Service
     */
    public Provider(String jsonSource) {
        //it serves as a placeholder for all the red parameters in the searching activity
        if (jsonSource.startsWith("num")) {
            name = jsonSource.substring(3);
            countryCode = "num";
            return;
        }

        //Normal creation flow of the constructor
        JSONObject data = new JSONObject(jsonSource);

        countryCode = data.getString("countryCode");
        countryName = CriteriaListFactory.getCountryList().get(countryCode);
        name = data.getString("name");
        trustmark = data.getString("trustmark");

        JSONArray jsonServiceTypes = data.getJSONArray("qServiceTypes");
        serviceTypes = new String[jsonServiceTypes.length()];
        for (int i=0; i < jsonServiceTypes.length(); i++) {
            serviceTypes[i] = jsonServiceTypes.getString(i);
        }

        JSONArray jsonServices = data.getJSONArray("services");
        services = new Service[jsonServices.length()];
        for (int i=0; i < jsonServices.length(); i++) {
            services[i] = new Service(jsonServices.getJSONObject(i).toString(), countryName, this.getName());
        }
    }

    //Get methods
    public String getCountryCode() { return countryCode; }
    public String getCountryName() { return countryName; }
    public Service[] getServices() { return services; }
    public String getName() { return name; }
    public String getTrustmark() { return trustmark; }
    public String[] getServiceTypes() { return serviceTypes; }
}
