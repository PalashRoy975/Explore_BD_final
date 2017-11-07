package deb.com.firebasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import deb.com.R;

public class SeeProfile extends AppCompatActivity {
    DatabaseReference dbStudent ;
    TextView fName,lName,uName;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_profile);


        auth= FirebaseAuth.getInstance();
        fName=(TextView)findViewById(R.id.tv1);
        lName=(TextView) findViewById(R.id.tv2);
        uName=(TextView) findViewById(R.id.tv3);

        String uid= auth.getCurrentUser().getUid();
        dbStudent= FirebaseDatabase.getInstance().getReference().child("Details").child(uid);

        dbStudent.addValueEventListener(new ValueEventListener() {
            int i=0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot statusSnapshot: dataSnapshot.getChildren()) {
                    if(statusSnapshot.getKey().equals("First_Name"))
                        fName.setText(statusSnapshot.getValue(String.class));

                    if(statusSnapshot.getKey().equals("Last_Name"))
                        lName.setText(statusSnapshot.getValue(String.class));

                    if(statusSnapshot.getKey().equals("User_Name"))
                        uName.setText(statusSnapshot.getValue(String.class));





                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
