package deb.com.firebasedemo;

import deb.com.firebaseapp.*;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;

import com.google.firebase.auth.FirebaseAuth;

import deb.com.R;

public class LoggedIn extends AppCompatActivity {

    FirebaseAuth auth;
    Button btnLogout;
    MediaController mediaController;

    Button btnEvents, btnMyProfile, btnBlogs, btnEdit,btnInfo;
    private Uri uri;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        //Get FirebaseAuth instance for user information
        auth= FirebaseAuth.getInstance();



        //TypeCast objects to button found by ID
        btnLogout= (Button) findViewById(R.id.logout);
        btnEdit= (Button) findViewById(R.id.edit);
        btnEvents= (Button) findViewById(R.id.events);
        btnMyProfile= (Button) findViewById(R.id.myProfile);
        btnBlogs = (Button) findViewById(R.id.crtBlog);
        btnInfo= (Button) findViewById(R.id.myInfo);

        btnEdit.setText("Blog Post");


        //Listeners for each button
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SeeProfile.class));
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ActivityBlogPost.class));
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        btnEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsFeed newsFeed= new NewsFeed();
                startActivity(new Intent(getApplicationContext(),ActivityPost.class));
                //getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,newsFeed).commit();

            }
        });

        btnMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainProfile.class));
            }
        });

        btnBlogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        });



    }
}
