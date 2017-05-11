package brookies.iut.com.brookies.model;

/**
 * Created by iem on 11/05/2017.
 */

public class ParamsPayment {

    private String stripeToken;
    private String stripeAmount;

    public ParamsPayment(String stripeToken, String stripeAmount) {
        this.stripeToken = stripeToken;
        this.stripeAmount = stripeAmount;
    }

    public String getStripeToken() {
        return stripeToken;
    }

    public String getStripeAmount() {
        return stripeAmount;
    }
}
