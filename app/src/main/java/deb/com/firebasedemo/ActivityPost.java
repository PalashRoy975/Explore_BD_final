package deb.com.firebasedemo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import deb.com.R;


public class ActivityPost extends AppCompatActivity {

    ImageButton btnSelectImg;
    Button btnPost, btnSignOut, btnNewsFeed, btnDate;
    FirebaseAuth auth;
    EditText fldTitle, fldDuration, fldSpace;
    TextView fldDate;
    Uri imageUri= null,resultUri=null;
    StorageReference storageReference;
    ProgressDialog progressBar;
    DatabaseReference mRef, allRef;
    private ProgressDialog progressDialog;

    DatePickerDialog.OnDateSetListener dateSetListener;

    static final int Gallery_Request=1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        storageReference= FirebaseStorage.getInstance().getReference();

        btnSelectImg= (ImageButton) findViewById(R.id.imageSelect);
        btnPost= (Button) findViewById(R.id.btnPost);
        btnNewsFeed= (Button) findViewById(R.id.btnNewsFeed);
        btnDate= (Button) findViewById(R.id.btnDate);

        fldTitle= (EditText) findViewById(R.id.fldTitle);
        fldDate = (TextView) findViewById(R.id.fldDesc);
        fldDuration= (EditText) findViewById(R.id.fldDuration);
        fldSpace= (EditText) findViewById(R.id.fldSpace);

        auth= FirebaseAuth.getInstance();


        mRef= FirebaseDatabase.getInstance().getReference().child("MyEvents");
        allRef= FirebaseDatabase.getInstance().getReference().child("Events");

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar= Calendar.getInstance();

                int  year= calendar.get(Calendar.YEAR);
                int  month= calendar.get(Calendar.MONTH);
                int  day= calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dateDialog= new DatePickerDialog(
                        ActivityPost.this,
                        dateSetListener,
                        year,month,day);

                dateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dateDialog.show();
            }
        });



        dateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                fldDate.setText(""+day+"/"+(month+1)+"/"+year);

            }
        };




        btnSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent= new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_Request);

            }
        });


        btnNewsFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
                finish();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startPosting();
            }
        });



    }

    public void checkDate(){
        String datePicked= fldDate.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        try {
            strDate = sdf.parse(datePicked);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(DateUtils.isToday(strDate.getTime()))
        {
            Toast.makeText(ActivityPost.this,"The day is today",Toast.LENGTH_SHORT).show();
        }
        else if (new Date().after(strDate)) {
            Toast.makeText(ActivityPost.this,new Date().toString()+ " is before "+ datePicked,Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(ActivityPost.this,"Date not expired!",Toast.LENGTH_SHORT).show();



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
                    .setAspectRatio(7,4)
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

        final String tit= fldTitle.getText().toString();
        final String des= fldDate.getText().toString();
        final String duration = fldDuration.getText().toString();

        String strSpace="";
        strSpace= fldSpace.getText().toString();


        int spaceTemp=1;

        if(!strSpace.equals(""))
        spaceTemp= Integer.parseInt(strSpace);

        final int space= spaceTemp;

        if(imageUri==null)
            Toast.makeText(getApplicationContext(), "Please Upload An Event Picture", Toast.LENGTH_SHORT).show();

        else if((fldTitle.getText().toString().trim().length() == 0)){

            fldTitle.setError("Please Enter A Place For Your Event");
        }
        else if((fldDate.getText().toString().trim().length() == 0)){

            Toast.makeText(getApplicationContext(),"Please Enter The Event Date" , Toast.LENGTH_SHORT).show();
        }

        else if((fldDuration.getText().toString().trim().length() == 0)){
            fldDuration.setError("Please enter the duration of your event");
        }
        else if((strSpace.equals(""))){
            fldSpace.setError("Please enter the numbers of participants");
        }






        else if (imageUri != null) {

            progressDialog = ProgressDialog.show(ActivityPost.this, "Please wait.","Uploading Image...", true);

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

                        riversRef.putFile(resultUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                        String uid= auth.getCurrentUser().getUid();
                                        DatabaseReference newPost= mRef.child(uid).push();

                                        DatabaseReference newAllPost = allRef.push();

                                        @SuppressWarnings("VisibleForTests")
                                        Uri downloadUri= taskSnapshot.getDownloadUrl();

                            /*newAllPost.child("Title").setValue(tit);
                            newAllPost.child("Description").setValue(des);
                            newAllPost.child("Duration").setValue(duration);
                            newAllPost.child("Likes").setValue(0);
                            newAllPost.child("eventID").setValue(newAllPost.getKey());

                            newAllPost.child("Image").setValue(downloadUri.toString());
                            newAllPost.child("User").setValue(auth.getCurrentUser().getEmail());
                            newAllPost.child("Space").setValue(space);
                            newAllPost.child("Going").setValue(0);*/




                                        String timeStamp= new SimpleDateFormat("HH:mm dd MMM, yyyy").format(new Date());
                                        long timeMS= System.currentTimeMillis();



                            /*newAllPost.child("Time").setValue(timeStamp);
                            newAllPost.child("TimeMilli").setValue(-1*timeMS);*/

                                        EventClass ec= new EventClass(tit,des,downloadUri.toString(),auth.getCurrentUser().getEmail(),timeStamp,duration,newAllPost.getKey(),0,(-1*timeMS),space,0);
                                        newAllPost.setValue(ec);

                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Event Updated", Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(getApplicationContext(), HomePage.class));
                                        progressDialog.dismiss();

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
