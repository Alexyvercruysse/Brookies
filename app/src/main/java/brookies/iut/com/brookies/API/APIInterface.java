package brookies.iut.com.brookies.API;

import com.google.gson.JsonObject;

import brookies.iut.com.brookies.model.ParamsPayment;
import brookies.iut.com.brookies.model.ResponsePayment;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by iem on 03/05/2017.
 */

public interface APIInterface {

    @POST("charge_user.php")
    Call<ResponsePayment> getPaymentValidation(@Body ParamsPayment paramsPayment);

}
