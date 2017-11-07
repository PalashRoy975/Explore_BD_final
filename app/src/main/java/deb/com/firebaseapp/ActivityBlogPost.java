package deb.com.firebaseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

import deb.com.R;

public class ActivityBlogPost extends AppCompatActivity {
    EditText edittextname,editTextDistrict;
    Button buttonadd;
    Spinner spinnerGeneres;
    Spinner season;
    RatingBar rb;
    Uri imageUri = null,resultUri=null;
    EditText guidename;
    EditText guideNo;
    EditText guideDesc;
    EditText TourDesc;
    FirebaseAuth auth;
    ImageButton btnSelectImg;
    StorageReference storageReference;
    private ProgressDialog mprogress;
    DatabaseReference mdbexpediture;

    DatabaseReference BloginfoDetails;
    static final int Gallery_Request = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogpost);

        auth = FirebaseAuth.getInstance();
        BloginfoDetails = FirebaseDatabase.getInstance().getReference().child("Blog");

        storageReference = FirebaseStorage.getInstance().getReference();

        edittextname = (EditText) findViewById(R.id.editTextName);
        editTextDistrict  = (EditText) findViewById(R.id.editDistrict);
        buttonadd = (Button) findViewById(R.id.btnAdd);
        spinnerGeneres = (Spinner) findViewById(R.id.spinnerGenre);
        season = (Spinner) findViewById(R.id.preferableSeason);
        rb = (RatingBar) findViewById(R.id.ratingBar);
        guidename = (EditText) findViewById(R.id.TourguideName);
        guideNo = (EditText) findViewById(R.id.TourguideNo);
        guideDesc = (EditText) findViewById(R.id.Tourguidedescrib);
        TourDesc = (EditText) findViewById(R.id.Tour_Desc);
        mprogress = new ProgressDialog(this);

        btnSelectImg = (ImageButton) findViewById(R.id.imageSelect);


        buttonadd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startPosting();

                    }
                }
        );


        btnSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Request);

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

        if (requestCode == Gallery_Request && resultCode == RESULT_OK) {
            imageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(7, 4)
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


    public void startPosting() {
        mprogress=ProgressDialog.show(ActivityBlogPost.this, "Please wait.","Uploading Image...", true);

        final String name = edittextname.getText().toString().trim();
        final String district = editTextDistrict.getText().toString();
        final String type = spinnerGeneres.getSelectedItem().toString();
        final String Season = season.getSelectedItem().toString();
        final float ratingNum = rb.getRating();
        final String Guidename = guidename.getText().toString().trim();
        final String Guideno = guideNo.getText().toString();
        final String GuideDesc = guideDesc.getText().toString().trim();
        final String Tourdesc = TourDesc.getText().toString().trim();


        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(district) && !TextUtils.isEmpty(Tourdesc) &&!TextUtils.isEmpty(type) && !TextUtils.isEmpty(Season) &&  imageUri !=null) {


            new Thread() {
                public void run() {
                    String imgName = getSaltString() + ".jpg";

                    StorageReference riversRef = storageReference.child("images/" + imgName);
                    riversRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests")
                            Uri downloadUri = taskSnapshot.getDownloadUrl();
                            String id = BloginfoDetails.push().getKey();
                            String email = auth.getCurrentUser().getEmail();



                            BlogDetails artist = new BlogDetails(downloadUri.toString(), id, name, district, email, type, Season, ratingNum, Guidename, Guideno, GuideDesc, Tourdesc, 0);

                            BloginfoDetails.child(id).setValue(artist);
                 /*   mdbexpediture = FirebaseDatabase.getInstance().getReference().child("Expend").child(artist.getTourId());
                    Graphical_repesent gr = new Graphical_repesent("11",0,0,0,0,0,0);
                    mdbexpediture.setValue(gr);*/

                            Intent intent = new Intent(getBaseContext(), Expenditure.class);

                            intent.putExtra("BlogID", artist);

                            startActivity(intent);

                            Toast.makeText(getApplicationContext(), "Place added", Toast.LENGTH_LONG).show();
                            mprogress.dismiss();

                        }

                    });
                }
            }.start();

        } else {
            Toast.makeText(this, "You should fill all of the information", Toast.LENGTH_LONG).show();
        }
    }


}

