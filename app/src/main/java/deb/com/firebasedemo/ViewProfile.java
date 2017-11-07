package deb.com.firebasedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import deb.com.R;

public class ViewProfile extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    DatabaseReference mUsers;
    FirebaseAuth auth;
    TextView txtName, txtHome, txtAge, txtStudy, txtInterest, txtEmail, txtWork;
    CircleImageView DP;
    Button back, btnProfileEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        auth=FirebaseAuth.getInstance();

        DP= (CircleImageView) findViewById(R.id.imgDP);
        back= (Button) findViewById(R.id.btnPerBack);
        btnProfileEdit= (Button) findViewById(R.id.btnProfileEdit);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        });


    }

    public void showPopUps(View view){

        PopupMenu popup= new PopupMenu(this,view);

        popup.setOnMenuItemClickListener(ViewProfile.this);

        MenuInflater inflater= popup.getMenuInflater();

        inflater.inflate(R.menu.menu_profile_edit,popup.getMenu());

        popup.show();




    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuProfileEdit:

                startActivity(new Intent(getApplicationContext(),AddUserDetails.class));
                break;

        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        String email="debx@gmail.com";
        try {
            email = getIntent().getExtras().getString("email");
        } catch (NullPointerException e ) {
            Toast.makeText(getApplicationContext(), "Sorry. Can't process right now. Please go back.", Toast.LENGTH_SHORT).show();
        }

        if(email.equals(auth.getCurrentUser().getEmail())){
            btnProfileEdit.setVisibility(View.VISIBLE);
        }

        String userName= GetUserName.getUserName(email);
        mUsers= FirebaseDatabase.getInstance().getReference().child("Users");

        putElements(userName,email);
    }

    void putElements(String userName, final String email){

        txtName= (TextView) findViewById(R.id.txtName);
        txtAge= (TextView) findViewById(R.id.txtAge);
        txtInterest= (TextView) findViewById(R.id.txtInterest);
        txtHome= (TextView) findViewById(R.id.txtHome);
        txtStudy= (TextView) findViewById(R.id.txtStudy);
        txtWork= (TextView) findViewById(R.id.txtWork);
        txtEmail= (TextView) findViewById(R.id.txtEmail);

        DatabaseReference mUserDetails= mUsers.child(userName);

        mUserDetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDetails u= dataSnapshot.getValue(UserDetails.class);

                txtName.setText(u.getName());
                txtAge.setText(u.getAge());
                txtInterest.setText(u.getInterest());
                txtWork.setText(u.getWork());
                txtStudy.setText(u.getStudy());
                txtHome.setText(u.getCity());
                txtEmail.setText(email);

                String image= u.getDp();
                Picasso.with(getApplicationContext()).load(image).into(DP);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


}
