package deb.com.firebasedemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.zip.Inflater;

import deb.com.R;

import static android.R.attr.name;

public class Tools extends AppCompatActivity {
    static int option=1;
    RadioGroup radioGroup;
    RadioButton radioButton;
    RelativeLayout newsfeed;
    ImageView choosePic;
    Button btnSaveBack;
    DatabaseReference refBackGround;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        btnSaveBack= (Button) findViewById(R.id.saveBack);

        auth= FirebaseAuth.getInstance();

        refBackGround= FirebaseDatabase.getInstance().getReference().child("Background").child(auth.getCurrentUser().getUid());

        btnSaveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NewsFeed.option=option;
                refBackGround.child("Option").setValue(""+option);

                Toast.makeText(getApplicationContext(), "Background Changed!", Toast.LENGTH_SHORT).show();
            }
        });

        radioGroup= (RadioGroup) findViewById(R.id.rdGroup);
    }


    public void rbclick(View view) {
        int rBtnID = radioGroup.getCheckedRadioButtonId();

        radioButton = (RadioButton) findViewById(rBtnID);
        choosePic = (ImageView) findViewById(R.id.choosePic);

        if (radioButton.getText().equals("Field")) {
            option = 1;
            choosePic.setImageResource(R.mipmap.background4);
        }

        if (radioButton.getText().equals("Flower")) {
            option = 2;
            choosePic.setImageResource(R.mipmap.background11);
        }

        if (radioButton.getText().equals("Rain")){
            option = 3;
            choosePic.setImageResource(R.mipmap.background8);
        }
        if (radioButton.getText().equals("None")){
            option = -1;
            choosePic.setImageResource(0);
        }



    }
}
