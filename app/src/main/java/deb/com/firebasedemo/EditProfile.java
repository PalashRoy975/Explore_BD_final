package deb.com.firebasedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import deb.com.R;

public class EditProfile extends AppCompatActivity {

    EditText upFname,upLname,upUname;
    Button btnUpdate,btnSeeUp;
    DatabaseReference dbStudent ;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        auth = FirebaseAuth.getInstance();

        dbStudent= FirebaseDatabase.getInstance().getReference().child("Details");

        upFname=(EditText) findViewById(R.id.updateFname);
        upLname=(EditText) findViewById(R.id.updateLname);
        upUname=(EditText) findViewById(R.id.updateUname);
        btnUpdate=(Button) findViewById(R.id.btnUpdate);
        btnSeeUp=(Button)findViewById(R.id.seeUpInfo);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String uid= auth.getCurrentUser().getUid();

                DatabaseReference user= dbStudent.child(uid);

                String firstName=upFname.getText().toString();
                String lastName=upLname.getText().toString();
                String userName=upUname.getText().toString();

                //AddDetails addDetails=new AddDetails(firstName,lastName,userName);

                user.child("First_Name").setValue(firstName);
                user.child("Last_Name").setValue(lastName);
                user.child("User_Name").setValue(userName);

                Toast.makeText(EditProfile.this,"Successfully Updated",Toast.LENGTH_SHORT).show();

            }
        });
        btnSeeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SeeProfile.class));
            }
        });


    }
}
