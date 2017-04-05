package brookies.iut.com.brookies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;

public class EditProfileActivity extends AppCompatActivity {

    ImageButton icon_checkprofile;
    RadioButton radioButtonMale,radioButtonFemale;
    ImageView image_1,image_2,image_3;
    EditText firstName,lastName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        icon_checkprofile = (ImageButton) findViewById(R.id.icon_checkprofile);
        radioButtonMale = (RadioButton) findViewById(R.id.radioButtonMale);
        radioButtonFemale = (RadioButton) findViewById(R.id.radioButtonFemale);
        image_1 = (ImageView) findViewById(R.id.image_1);
        image_2 = (ImageView) findViewById(R.id.image_2);
        image_3 = (ImageView) findViewById(R.id.image_3);
        firstName = (EditText) findViewById(R.id.editText_firstname);
        lastName = (EditText) findViewById(R.id.editText_lastname);

        radioButtonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonMale.isChecked()){
                    radioButtonFemale.setChecked(false);
                }
                else {
                    radioButtonMale.setChecked(false);
                    radioButtonFemale.setChecked(true);
                }
            }
        });
        radioButtonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonFemale.isChecked()){
                    radioButtonMale.setChecked(false);
                }
                else {
                    radioButtonFemale.setChecked(false);
                    radioButtonMale.setChecked(true);
                }
            }
        });
    }
}
