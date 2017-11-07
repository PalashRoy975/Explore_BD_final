package deb.com.firebaseapp;
import deb.com.R;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Expenditure extends AppCompatActivity {

    Button btnsubmit, btngraph;
    EditText food, guide, resort, vehicle, others;
    TextView totalcost;
    DatabaseReference mdbexpediture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenditure);

        final BlogDetails artist = (BlogDetails) getIntent().getParcelableExtra("BlogID");

        mdbexpediture = FirebaseDatabase.getInstance().getReference().child("Expend").child(artist.getTourId());

        btnsubmit = (Button) findViewById(R.id.submit);
        btngraph = (Button) findViewById(R.id.graph);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCost();

            }
        });
        btngraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), graph.class);
                intent.putExtra("Expend", artist);
                startActivity(intent);
            }
        });

        food = (EditText) findViewById(R.id.foodfield);
        guide = (EditText) findViewById(R.id.guidefield);
        resort = (EditText) findViewById(R.id.resortfield);
        vehicle = (EditText) findViewById(R.id.vehiclefield);
        others = (EditText) findViewById(R.id.otherfield);
        totalcost = (TextView) findViewById(R.id.totalfield);

    }

    public void addCost() {

        String tempID = "11";
        ActivityBlogPost main = new ActivityBlogPost();

        int foood = Integer.parseInt(food.getText().toString());
        int guuide = Integer.parseInt(guide.getText().toString());
        int veehicle = Integer.parseInt(vehicle.getText().toString());
        int reesort = Integer.parseInt(resort.getText().toString());
        int otthers = Integer.parseInt(others.getText().toString());
        int total = foood + guuide + veehicle + reesort + otthers;
        totalcost.setText(total + "");


        Graphical_repesent gr = new Graphical_repesent(tempID, foood, guuide, veehicle, reesort, otthers, total);
        mdbexpediture.setValue(gr);

        Toast.makeText(this, "Expenditure added", Toast.LENGTH_LONG).show();

    }
}
