package brookies.iut.com.brookies;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import brookies.iut.com.brookies.model.Picture;
import brookies.iut.com.brookies.model.User;

public class EditProfileActivity extends AppCompatActivity {

    private ImageButton icon_checkprofile;
    private RadioButton radioButtonMale,radioButtonFemale;
    private CheckBox checkBoxMale,checkBoxFemale;
    private ImageView image_1,image_2,image_3;
    private EditText firstName,lastName,birthdate;
    private Bitmap image1Bitmap,image2Bitmap,image3Bitmap;
    private FrameLayout progressBarHolder;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "EditProfileActivity";
    private String userId;
    private DatabaseReference mDatabase;
    private  static final int PICK_PHOTO_FOR_1 = 1001;
    private  static final int PICK_PHOTO_FOR_2 = 1002;
    private  static final int PICK_PHOTO_FOR_3 = 1003;
    private User user;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        progressBarHolder.setVisibility(View.VISIBLE);
        userId = getIntent().getStringExtra("userId");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    userId = user.getUid();
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        mDatabase.child("user/"+userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                lastName.setText(user.getLastname());
                firstName.setText(user.getFirstname());
                if (user.getSexe() != null && user.getSexe().equals("")) {
                    radioButtonFemale.setChecked(user.getSexe() == "female" ? true : false);
                    radioButtonMale.setChecked(user.getSexe() == "male" ? true : false);
                }
                if (user.getLikeMen() != null || user.getLikeWomen() != null){
                    checkBoxFemale.setChecked(user.getLikeWomen());
                    checkBoxMale.setChecked(user.getLikeMen());
                }
                if (user.getBirthdate() != null && !user.getBirthdate().equals("")){
                    birthdate.setText(user.getBirthdate());
                }
                Picasso.with(getApplication()).load(user.getPictures().get(0).getUrl()).into(image_1);
                if (user.getPictures().size() >= 2) {
                    Picasso.with(getApplication()).load(user.getPictures().get(1).getUrl()).into(image_2);
                }
                if (user.getPictures().size() >= 3) {
                    Picasso.with(getApplication()).load(user.getPictures().get(2).getUrl()).into(image_3);
                }
                progressBarHolder.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        icon_checkprofile = (ImageButton) findViewById(R.id.icon_checkprofile);

        icon_checkprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstName.getText().toString().equals("") ||
                        lastName.getText().toString().equals("") ||
                        birthdate.getText().toString().equals("") ||
                        (!checkBoxMale.isChecked() && !checkBoxFemale.isChecked())) {

                    if (firstName.getText().toString().equals("")) {
                        firstName.setError("First Name missing");
                    }
                    else {
                        firstName.setError(null);
                    }
                    if (lastName.getText().toString().equals("")) {
                        lastName.setError("Last Name missing");
                    }
                    else {
                        lastName.setError(null);
                    }
                    if (birthdate.getText().toString().equals("")) {
                        birthdate.setError("Birth Date is missing");
                    }
                    else {
                        birthdate.setError(null);
                    }
                    if (!checkBoxMale.isChecked() && !checkBoxFemale.isChecked()){
                        checkBoxMale.setError("Need at least one interest");
                        checkBoxFemale.setError("Need at least one interest");
                    }
                    else {
                        checkBoxMale.setError(null);
                        checkBoxFemale.setError(null);
                    }
                }
                else {
                    progressBarHolder.setVisibility(View.VISIBLE);
                    firstName.setError(null);
                    lastName.setError(null);
                    birthdate.setError(null);
                    checkBoxMale.setError(null);
                    checkBoxFemale.setError(null);
                    user.setSexe(radioButtonMale.isChecked() ? "male" : "female");
                    user.setFirstname(firstName.getText().toString());
                    user.setLastname(lastName.getText().toString());
                    user.setBirthdate(birthdate.getText().toString());
                    user.setLikeMen(checkBoxMale.isChecked() ? true : false);
                    user.setLikeWomen(checkBoxFemale.isChecked() ? true : false);

                    if (image1Bitmap == null && image2Bitmap == null && image3Bitmap == null ){
                        mDatabase.child("user").child(userId).setValue(user);
                        progressBarHolder.setVisibility(View.GONE);
                        Intent intent = new Intent(EditProfileActivity.this, EditProfile2Activity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    }

                    if (image1Bitmap != null) {
                        StorageReference image1 = storageRef.child(userId + "/image1.jpg");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        image1Bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);

                        byte[] data = baos.toByteArray();
                        UploadTask uploadTask = image1.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.e(TAG, "FAILED UPLOAD image 1 : " + exception.getMessage());
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                String url = downloadUrl.toString();
                                user.setPicturesAtIndex(0, new Picture("image1.jpg", url));
                                if (image2Bitmap == null && image3Bitmap == null) {
                                    progressBarHolder.setVisibility(View.GONE);
                                    mDatabase.child("user").child(userId).setValue(user);
                                    Intent intent = new Intent(EditProfileActivity.this, EditProfile2Activity.class);
                                    intent.putExtra("userId", userId);
                                    progressBarHolder.setVisibility(View.GONE);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                    if (image2Bitmap != null){
                        StorageReference Storimage2 = storageRef.child(userId+"/image2.jpg");
                        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                        image2Bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos2);

                        byte[] data2 = baos2.toByteArray();
                        UploadTask uploadTask2 = Storimage2.putBytes(data2);
                        uploadTask2.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.e(TAG,"FAILED UPLOAD image 2: "+exception.getMessage());
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                String url = downloadUrl.toString();
                                if (image2Bitmap != null){
                                    if (user.getPictures().size() > 1) {
                                        user.setPicturesAtIndex(1,new Picture("image2.jpg",url));
                                    }
                                    else {
                                        user.addPicture(new Picture("image2.jpg", url));
                                    }
                                }

                                if (image3Bitmap == null){
                                    progressBarHolder.setVisibility(View.GONE);
                                    mDatabase.child("user").child(userId).setValue(user);
                                    Intent intent = new Intent(EditProfileActivity.this,EditProfile2Activity.class);
                                    intent.putExtra("userId",userId);
                                    progressBarHolder.setVisibility(View.GONE);
                                    startActivity(intent);
                                }
                            }
                        });
                    }



                    if (image3Bitmap != null){
                        StorageReference Stoimage3 = storageRef.child(userId+"/image3.jpg");
                        ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
                        image3Bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos3);
                        byte[] data3 = baos3.toByteArray();
                        UploadTask uploadTask3 = Stoimage3.putBytes(data3);
                        uploadTask3.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.e(TAG,"FAILED UPLOAD image 3: "+exception.getMessage());
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                String url = downloadUrl.toString();
                                progressBarHolder.setVisibility(View.GONE);
                                if (image3Bitmap != null){
                                    if (user.getPictures().size() > 2) {
                                        user.setPicturesAtIndex(2,new Picture("image3.jpg",url));
                                    }
                                    else {
                                        user.addPicture(new Picture("image3.jpg", url));
                                    }
                                }
                                mDatabase.child("user").child(userId).setValue(user);
                                Intent intent = new Intent(EditProfileActivity.this,EditProfile2Activity.class);
                                intent.putExtra("userId",userId);
                                startActivity(intent);
                            }
                        });
                    }


                }
            }
        });

        radioButtonMale = (RadioButton) findViewById(R.id.radioButtonMale);
        radioButtonFemale = (RadioButton) findViewById(R.id.radioButtonFemale);
        checkBoxMale = (CheckBox) findViewById(R.id.checkBoxMale);
        checkBoxFemale = (CheckBox) findViewById(R.id.checkBoxFemale);
        image_1 = (ImageView) findViewById(R.id.image_1);
        image_2 = (ImageView) findViewById(R.id.image_2);
        image_3 = (ImageView) findViewById(R.id.image_3);
        image_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(PICK_PHOTO_FOR_1);
            }
        });

        image_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(PICK_PHOTO_FOR_2);
            }
        });
        image_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(PICK_PHOTO_FOR_3);
            }
        });


        firstName = (EditText) findViewById(R.id.editText_firstname);
        lastName = (EditText) findViewById(R.id.editText_lastname);
        birthdate = (EditText) findViewById(R.id.editText_birthDate);
        final Calendar myCalendar = Calendar.getInstance();

        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd/MM/yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

                        birthdate.setText(sdf.format(myCalendar.getTime()));
                    }
                }, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

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




    public void pickImage(int pickFor) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, pickFor);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == PICK_PHOTO_FOR_1 || requestCode == PICK_PHOTO_FOR_2 || requestCode == PICK_PHOTO_FOR_3)  && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                if (requestCode == PICK_PHOTO_FOR_1) {
                    image_1.setImageBitmap(bitmap);
                    image1Bitmap = bitmap;
                }
                if (requestCode == PICK_PHOTO_FOR_2) {
                    image_2.setImageBitmap(bitmap);
                    image2Bitmap = bitmap;
                }
                if (requestCode == PICK_PHOTO_FOR_3) {
                    image_3.setImageBitmap(bitmap);
                    image3Bitmap = bitmap;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }



}
