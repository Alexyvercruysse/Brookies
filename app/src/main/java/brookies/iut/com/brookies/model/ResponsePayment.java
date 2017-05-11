package brookies.iut.com.brookies.model;

/**
 * Created by iem on 03/05/2017.
 */

public class ResponsePayment {

    private boolean statut;

    public ResponsePayment(boolean statut) {
        this.statut = statut;
    }

    public boolean isStatut() {
        return statut;
    }
}
