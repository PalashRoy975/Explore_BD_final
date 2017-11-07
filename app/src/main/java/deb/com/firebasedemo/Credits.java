package deb.com.firebasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import deb.com.R;

public class Credits extends AppCompatActivity {

    TextView txtCredit1,txtCredit2,txtCredit3,txtCredit4, txtCreditLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        Animation fall= AnimationUtils.loadAnimation(this, R.anim.animationfile);
        Animation fall2= AnimationUtils.loadAnimation(this, R.anim.animationfile);
        Animation fall3= AnimationUtils.loadAnimation(this, R.anim.animationfile);
        Animation fall4= AnimationUtils.loadAnimation(this, R.anim.animationfile);
        Animation overshoot= AnimationUtils.loadAnimation(this, R.anim.animation_overshoot);

        fall.setRepeatCount(Animation.INFINITE);

        txtCredit1= (TextView) findViewById(R.id.credit1);
        txtCredit2= (TextView) findViewById(R.id.credit2);
        txtCredit3= (TextView) findViewById(R.id.credit3);
        txtCredit4= (TextView) findViewById(R.id.credit4);
        txtCreditLast= (TextView) findViewById(R.id.creditLast);

        txtCredit1.setText("Credits");
        txtCredit2.setText("Deb Sourav");
        txtCredit3.setText("Mirajul Mohin");
        txtCredit4.setText("Palash Roy");
        txtCreditLast.setText("A project for\n\nDepartment Of\nComputer Science and Engineering,\nUniversity of Dhaka");


        txtCredit1.startAnimation(fall);
        txtCredit2.startAnimation(fall2);
        txtCredit3.startAnimation(fall3);
        txtCredit4.startAnimation(fall4);
        txtCreditLast.startAnimation(overshoot);

        fall.setDuration(5000);
        fall2.setDuration(5000);
        fall3.setDuration(5000);
        fall4.setDuration(5000);
        overshoot.setDuration(2000);

        fall2.setStartOffset(fall.getStartOffset()+3500);
        fall3.setStartOffset(fall2.getStartOffset()+3500);
        fall4.setStartOffset(fall3.getStartOffset()+3500);
        overshoot.setStartOffset(fall4.getStartOffset()+4000);




    }


}
