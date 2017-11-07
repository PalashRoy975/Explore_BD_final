package deb.com.firebaseapp;
import deb.com.R;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.N)
public class DatePicker extends AppCompatActivity {

    Button btndate;

    static final int DILOG_ID = 0;
    DateFormat formatDateTime = DateFormat.getDateTimeInstance();
    private DatePickerDialog.OnDateSetListener mDateSetListeneer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        btndate = (Button) findViewById(R.id.DateBtn);
        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();

               int  year_x = cal.get(Calendar.YEAR);
                int month_x = cal.get(Calendar.MONTH);
                int day_x = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DatePicker.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListeneer,
                        year_x, month_x, day_x);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });

        mDateSetListeneer = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {


                Toast.makeText(DatePicker.this,day+"/ "+ month+"/"+ year,Toast.LENGTH_LONG).show();
            }
        };



    }


}
