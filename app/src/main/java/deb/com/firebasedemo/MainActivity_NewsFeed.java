package deb.com.firebasedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import deb.com.R;

public class MainActivity_NewsFeed extends AppCompatActivity {
    EditText edittextname;
    Button buttonadd, btnFinish;
    Spinner spinnerGeneres;
    Spinner season;
    RatingBar rb;
    EditText guidename;
    EditText guideNo;
    EditText guideDesc;

    DatabaseReference databaseArtists;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        auth= FirebaseAuth.getInstance();
        databaseArtists = FirebaseDatabase.getInstance().getReference().child("EventClass");

        edittextname=(EditText)findViewById(R.id.editTextName);
        buttonadd=(Button) findViewById(R.id.btnAdd);
        btnFinish = (Button) findViewById(R.id.btnFinish);
        spinnerGeneres=(Spinner) findViewById(R.id.spinnerGenre);
        season=(Spinner) findViewById(R.id.preferableSeason);
        rb= (RatingBar) findViewById(R.id.ratingBar);
        guidename= (EditText) findViewById(R.id.TourguideName);
        guideNo= (EditText) findViewById(R.id.TourguideNo);
        guideDesc= (EditText) findViewById(R.id.Tourguidedescrib);



        buttonadd.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {

                        addArtist();
                    }
                }
        );

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getBaseContext(),News_Feed.class));
            }
        });

    }


    public void addArtist()
    {
        String name = edittextname.getText().toString().trim();
        String genre= spinnerGeneres.getSelectedItem().toString();
        String Season= season.getSelectedItem().toString();
        float ratingNum= rb.getRating();
        String Guidename = guidename.getText().toString().trim();
        String Guideno= guideNo.getText().toString();
        String GuideDesc = guideDesc.getText().toString().trim();
        if(!TextUtils.isEmpty(name))

        {
            String uid= auth.getCurrentUser().getUid();

            DatabaseReference user= databaseArtists.child(uid);
            String id = user.push().getKey();

            Artist artist = new Artist(id,name,genre,Season,ratingNum,Guidename,Guideno,GuideDesc);

            //user.child(id).setValue(artist);



            user.child(id).child("artistId").setValue(id);
            user.child(id).child("artistName").setValue(name);
            user.child(id).child("Season").setValue(Season);
            user.child(id).child("artistGenre").setValue(genre);
            user.child(id).child("rb").setValue(ratingNum);
            user.child(id).child("Guname").setValue(Guidename);
            user.child(id).child("Guno").setValue(Guideno);
            user.child(id).child("Gudesc").setValue(GuideDesc);


            //Log.i("listener", "the data has changed");

            Toast.makeText(this,"Artist added",Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(this,"You should enter a naame",Toast.LENGTH_LONG).show();
        }
    }


}

