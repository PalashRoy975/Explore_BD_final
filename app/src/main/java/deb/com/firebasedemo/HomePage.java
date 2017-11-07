package deb.com.firebasedemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import deb.com.*;
import deb.com.firebaseapp.ActivityBlogPost;
import deb.com.firebaseapp.Blog_Feed;
import deb.com.showinghotels.HotelMainActivity;


public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    TextView headerEmail;
    CircleImageView headerDP;
    FirebaseAuth auth;

    public void setHeader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);

        headerEmail= (TextView) hView.findViewById(R.id.headerEmail);
        headerDP= (CircleImageView) hView.findViewById(R.id.navDP);

        headerEmail.setText(auth.getCurrentUser().getEmail());

        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String userName= GetUserName.getUserName(email);
        DatabaseReference mUser= FirebaseDatabase.getInstance().getReference().child("Users").child(userName);

        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDetails u= dataSnapshot.getValue(UserDetails.class);
                Picasso.with(getApplicationContext()).load(u.getDp()).into(headerDP);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        auth= FirebaseAuth.getInstance();

        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawerLayout);
        mActionBarDrawerToggle= new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);


        setHeader();




        final Fragment_MyEvents my=new Fragment_MyEvents();
        final TabProfile tabProfile= new TabProfile();
        final NewsFeed newsFeed= new NewsFeed();
        final EmptyPage emptyPage= new EmptyPage();
        final MapsMainActivity mapsMainActivity= new MapsMainActivity();
        final MapsActivity mapsActivity= new MapsActivity();
        final TabMap tabMap= new TabMap();
        final HotelMainActivity hotel= new HotelMainActivity();
        final Blog_Feed blog_feed= new Blog_Feed();


        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,newsFeed).commit();


        BottomNavigationView bottomNavigationItemView= (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationItemView);

        bottomNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()){
                    case R.id.navEvents:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,newsFeed).commit();
                        break;
                    case R.id.navBlogs:
                        //startActivity(new Intent(getApplicationContext(),Credits.class));
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,blog_feed).commit();
                        break;
                    case R.id.navMyProfile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, tabProfile).commit();
                        break;
                    case R.id.navMaps:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, tabMap).commit();
                        break;
                    case R.id.navHotels:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, hotel).commit();
                        break;
                }

                return true;
            }

        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.drawer_logout:
                auth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
            case R.id.drawer_account:
                startActivity(new Intent(getApplicationContext(),ActivityBlogPost.class));
                //Toast.makeText(getApplicationContext(), "Account settings not added yet...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.drawer_details:
                Intent intent= new Intent(getBaseContext(),ViewProfile.class);
                String email=auth.getCurrentUser().getEmail();
                intent.putExtra("email",email);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), "Details not added yet...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.drawer_tools:
                startActivity(new Intent(getApplicationContext(),Tools.class));
                break;
            case R.id.drawer_notification:
                startActivity(new Intent(getApplicationContext(),Notifications.class));
                break;
            case R.id.drawer_credits:
                startActivity(new Intent(getApplicationContext(),Credits.class));
                break;
        }
        return true;
    }
}
