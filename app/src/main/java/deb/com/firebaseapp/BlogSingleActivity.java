package deb.com.firebaseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import deb.com.R;

public class BlogSingleActivity extends AppCompatActivity {

    private String mPost_key = null;
    private DatabaseReference BloginfoDetails;
    private FirebaseAuth mAuth;
    private TextView txtPlace,txtDistrict, txtType, txtSeason, txtGuideName, txtGuideNo, txtGuideDesc, txtDesc,txtuser;
    private ImageView imagePlace;
    private RatingBar ratebar;
    private Button btnExpenditure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blogsinglesctivity);
        final BlogDetails artist = (BlogDetails) getIntent().getParcelableExtra("blog_id");

        mAuth = FirebaseAuth.getInstance();
        BloginfoDetails = FirebaseDatabase.getInstance().getReference().child("Blog");


        txtPlace = (TextView) findViewById(R.id.textViewName);
        txtDistrict = (TextView) findViewById(R.id.textViewDistrict);
        txtType = (TextView) findViewById(R.id.textViewGenere);
        txtSeason = (TextView) findViewById(R.id.textViewSeason);
        txtGuideName = (TextView) findViewById(R.id.Gname);
        txtGuideNo = (TextView) findViewById(R.id.Gno);
        txtGuideDesc = (TextView) findViewById(R.id.GDesc);
        txtDesc = (TextView) findViewById(R.id.Tdesc);
        imagePlace = (ImageView) findViewById(R.id.blogImage);
        ratebar = (RatingBar) findViewById(R.id.SingleratingBar);
        btnExpenditure= (Button) findViewById(R.id.btnExpend);
        txtuser = (TextView) findViewById(R.id.UserName);



        BloginfoDetails.child(artist.getTourId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String TourPlace = (String) dataSnapshot.child("tourName").getValue();
                String TourDistrict = (String) dataSnapshot.child("tourdis").getValue();
                String PlaceType = (String) dataSnapshot.child("tourGenre").getValue();
                String Season = (String) dataSnapshot.child("season").getValue();
                String GuideName = (String) dataSnapshot.child("guname").getValue();
                String GuideNo = (String) dataSnapshot.child("guno").getValue();
                String GuideDesc = (String) dataSnapshot.child("gudesc").getValue();
                String Desc = (String) dataSnapshot.child("desc").getValue();
                String postImage = (String) dataSnapshot.child("image").getValue();
                String user = (String) dataSnapshot.child("email").getValue();
             //   float rate = (float) dataSnapshot.child("rb").getValue();



                txtPlace.setText(TourPlace);
                txtDistrict.setText(TourDistrict);
                txtType.setText(PlaceType);
                txtSeason.setText(Season);
                txtGuideName.setText(GuideName);
                txtGuideNo.setText(GuideNo);
                txtGuideDesc.setText(GuideDesc);
                txtDesc.setText(Desc);
                txtuser.setText(user);
               // ratebar.setRating(rate);


                Picasso.with(BlogSingleActivity.this).load(postImage).into(imagePlace);

                btnExpenditure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(BlogSingleActivity.this, graph.class);
                        intent.putExtra("Expend", artist);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
