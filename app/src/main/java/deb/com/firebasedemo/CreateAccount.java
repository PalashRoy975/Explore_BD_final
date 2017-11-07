package deb.com.firebasedemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import deb.com.*;

public class CreateAccount extends AppCompatActivity {

    EditText emailField;
    EditText passField;
    EditText confirmPassField;
    Button btnSignUp;

    FirebaseAuth auth, auth2;
    DatabaseReference mRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        auth= FirebaseAuth.getInstance();
        auth2= FirebaseAuth.getInstance();

        mRef= FirebaseDatabase.getInstance().getReference().child("Users");

        emailField= (EditText) findViewById(R.id.newEmail);
        passField= (EditText) findViewById(R.id.newPass);
        confirmPassField= (EditText) findViewById(R.id.confirmPass);

        btnSignUp= (Button) findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

    }

    void createAccount(){

        final String newEmail= emailField.getText().toString();
        String newPass= passField.getText().toString();
        String confirmPass= confirmPassField.getText().toString();

        if(newEmail.equals("")||newPass.equals("")||confirmPass.equals("")){
            Toast.makeText(getApplicationContext(),"Enter Email ID and Password!",Toast.LENGTH_LONG).show();
        }

        //Check if password and confirm password field matches
        else if(!newPass.equals(confirmPass)){
            Toast.makeText(CreateAccount.this,"Passwords didn't match!",Toast.LENGTH_LONG).show();

        }

        else{
            auth.createUserWithEmailAndPassword(newEmail,newPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(CreateAccount.this,"Account created succssfully!",Toast.LENGTH_LONG).show();
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        String userName= GetUserName.getUserName(newEmail);

                        UserDetails u= new UserDetails("Tourist","Not provided","Not provided","Not provided","Not provided","Not provided","deb33@gmail.com","https://firebasestorage.googleapis.com/v0/b/du-fall.appspot.com/o/images%2FG2DUDMX24L29CG2ZMG.jpg?alt=media&token=afe52cdc-fcac-4a7b-917d-7caa8fe262d7");

                        DatabaseReference newUser= mRef.child(userName);
                        newUser.setValue(u);


                        //startActivity(new Intent(CreateAccount.this,MainActivity.class));
                        startActivity(new Intent(CreateAccount.this,AddUserDetails.class));
                    }
                    else
                        Toast.makeText(CreateAccount.this,"Account creation unsuccssfull",Toast.LENGTH_LONG).show();

                }
            });

        }



    }


}
