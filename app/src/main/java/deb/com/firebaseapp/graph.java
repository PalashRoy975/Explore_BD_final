package deb.com.firebaseapp;;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import deb.com.R;


public class graph extends AppCompatActivity {

    DatabaseReference databasegarph;
    int foodcost, guidecost, resortcost, vehiclecost, othercost;
    EditText food, guide, resort, vehicle, others;
    TextView txtfood, txtguide, txtresort, txtvehicle, txtothers,txtTotal;
    private static String tag = "MainActivity";
    PieChart piechart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        final BlogDetails artist = (BlogDetails) getIntent().getParcelableExtra("Expend");
        final String expend = artist.getTourId();

        piechart = (PieChart) findViewById(R.id.Piegraph);
        piechart.setUsePercentValues(true);
        piechart.getDescription().setEnabled(false);
        piechart.setExtraOffsets(5, 10, 5, 5);
        piechart.setDragDecelerationFrictionCoef(.95f);
        piechart.setHoleColor(Color.WHITE);
        piechart.setDrawHoleEnabled(true);
        piechart.setCenterText("Expenditure");
        piechart.setTransparentCircleRadius(61f);
        piechart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        final ArrayList<PieEntry> yValues = new ArrayList<>();

        txtfood = (TextView) findViewById(R.id.fooddata);
        txtguide = (TextView) findViewById(R.id.guidedata);
        txtresort = (TextView) findViewById(R.id.resortdata);
        txtvehicle = (TextView) findViewById(R.id.Vehicledata);
        txtothers = (TextView) findViewById(R.id.otherdata);


        databasegarph = FirebaseDatabase.getInstance().getReference();

        Query mQueryRef = databasegarph.child("Expend").child(expend);

        mQueryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Graphical_repesent gr = dataSnapshot.getValue(Graphical_repesent.class);
                txtfood.setText(gr.getFoood() + " Tk");
                txtguide.setText(gr.getGuuide() + " Tk");
                txtresort.setText(gr.getReesort() + " Tk");
                txtvehicle.setText(gr.getVeehicle() + " Tk");
                txtothers.setText(gr.getOtthes() + " Tk");



                foodcost = gr.getFoood();
                vehiclecost = gr.getVeehicle();
                guidecost = gr.getGuuide();
                resortcost = gr.getReesort();
                othercost = gr.getOtthes();


                yValues.add(new PieEntry(foodcost, "Food"));
                yValues.add(new PieEntry(vehiclecost, "Vehicle"));
                yValues.add(new PieEntry(resortcost, "Resort"));
                yValues.add(new PieEntry(guidecost, "Guide"));
                yValues.add(new PieEntry(othercost, "Others"));

                Description des = new Description();
                des.setText("Graphical Representation of Total Expenditure");
                des.setTextSize(15);
                piechart.setDescription(des);
                PieDataSet dataSet = new PieDataSet(yValues, "");
                dataSet.setSliceSpace(1f);
                dataSet.setSelectionShift(5f);
                dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                PieData data = new PieData(dataSet);
                data.setValueTextSize(10f);
                data.setValueTextColor(Color.BLACK);
                piechart.setData(data);
                piechart.invalidate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
