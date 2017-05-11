package brookies.iut.com.brookies.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by iem on 03/05/2017.
 */

public class APIClient {

    public static final String BASE_URL = "http://lucien.appsolute-preprod.fr/stripe/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

    public static APIInterface getApiInterface() {
        APIInterface apiService = APIClient.getClient().create(APIInterface.class);
        return apiService;
    }
}
