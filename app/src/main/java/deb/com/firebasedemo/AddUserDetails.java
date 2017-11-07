package deb.com.firebasedemo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import deb.com.R;

public class AddUserDetails extends AppCompatActivity {

    ImageButton btnSelectImg;
    Button btnPost, btnSignOut, btnNewsFeed, btnDate;
    FirebaseAuth auth;
    EditText fldName, fldHome, fldAge, fldStudy, fldInterest, fldEmail, fldWork;
    Uri imageUri= null,resultUri=null;
    StorageReference storageReference;
    ProgressDialog progressBar;
    DatabaseReference mUsers;
    Button btnAddDetails, btnGoHome;
    private ProgressDialog progressDialog;


    static final int Gallery_Request=1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_details);

        storageReference= FirebaseStorage.getInstance().getReference();

        fldName = (EditText) findViewById(R.id.fldName);
        fldAge = (EditText) findViewById(R.id.fldAge);
        fldInterest = (EditText) findViewById(R.id.fldInterest);
        fldHome = (EditText) findViewById(R.id.fldHome);
        fldStudy = (EditText) findViewById(R.id.fldStudy);
        fldWork = (EditText) findViewById(R.id.fldWork);

        btnSelectImg= (ImageButton) findViewById(R.id.dpSelect);
        btnAddDetails= (Button) findViewById(R.id.btnAddDetails);
        btnGoHome= (Button) findViewById(R.id.btnGoHome);

        auth= FirebaseAuth.getInstance();

        String email= auth.getCurrentUser().getEmail();
        String userName= GetUserName.getUserName(email);

        mUsers= FirebaseDatabase.getInstance().getReference().child("Users").child(userName);

        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        });

        btnSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent= new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_Request);

            }
        });

        btnAddDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });


    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Request && resultCode==RESULT_OK){


            imageUri= data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                resultUri = result.getUri();
                btnSelectImg.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private void startPosting() {

        final String name= fldName.getText().toString();
        final String age= fldAge.getText().toString();
        final String home = fldHome.getText().toString();
        final String work = fldWork.getText().toString();
        final String study = fldStudy.getText().toString();
        final String interest = fldInterest.getText().toString();


        if(imageUri==null)
            Toast.makeText(getApplicationContext(), "Please Upload An Profile Picture", Toast.LENGTH_SHORT).show();

        else if((fldName.getText().toString().trim().length() == 0)){

            fldName.setError("Please your name");
        }
        else if((fldAge.getText().toString().trim().length() == 0)){

            fldAge.setError("Please enter your age");
        }

        else if((fldHome.getText().toString().trim().length() == 0)){

            fldHome.setError("Please enter your current city");
        }
        else if(fldWork.getText().toString().trim().length() == 0){
            fldWork.setError("Please enter your proffession");
        }

        else if(fldStudy.getText().toString().trim().length() == 0){
            fldInterest.setError("Please enter your last educational institute");
        }
        else if(fldInterest.getText().toString().trim().length() == 0){
            fldInterest.setError("Please enter your area of interest");
        }






        else if (imageUri != null) {

            progressDialog = ProgressDialog.show(AddUserDetails.this, "Please wait.","Uploading Image...", true);

            //progressBar.setMessage("Uploading picture...");
            //progressBar.show();


            new Thread()
            {
                public void run()
                {

                    try
                    {
                        //sleep(5000);

                        String imgName= getSaltString()+".jpg";
                        StorageReference riversRef = storageReference.child("images/"+imgName);

                        riversRef.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                String uid= auth.getCurrentUser().getUid();



                                @SuppressWarnings("VisibleForTests")
                                Uri downloadUri= taskSnapshot.getDownloadUrl();

                                        UserDetails u= new UserDetails(name,home,age,work,study,interest,auth.getCurrentUser().getEmail(),downloadUri.toString());
                                        mUsers.setValue(u);


                                        progressDialog.dismiss();
                                        Toast.makeText(AddUserDetails.this, "Profile details updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), HomePage.class));


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                                    }
                                });

                    }
                    catch (Exception e)
                    {

                    }


                }
            }.start();







        } else {
            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
        }
    };

}
