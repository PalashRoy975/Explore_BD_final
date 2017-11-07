package deb.com.firebasedemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import deb.com.R;

public class Notifications extends AppCompatActivity {

    DatabaseReference mJoin, mRef, mEvents, mNotification;
    FirebaseAuth auth;
    TextView txtJoinedEvents, eventToday;
    RecyclerView rView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        rView= (RecyclerView) findViewById(R.id.rNotiView);
        rView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        auth=FirebaseAuth.getInstance();
        mJoin= FirebaseDatabase.getInstance().getReference().child("JoinEvent");
        mNotification= FirebaseDatabase.getInstance().getReference().child("Notification");

        auth= FirebaseAuth.getInstance();







    }

    void checkNotification(Noti model){
        String uid= auth.getCurrentUser().getUid();
        String postKey= model.getPostKey();
        final String notiDate= model.getDate();

        DatabaseReference newNoti= mNotification.child(uid).child(postKey);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date strDate = new Date();
        try {
            strDate = sdf.parse(notiDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(DateUtils.isToday(strDate.getTime()))
        {
            System.out.println("nothing");
        }
        else if (new Date().after(strDate)){
            newNoti.removeValue();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();


        String uid= auth.getCurrentUser().getUid();
        mRef= FirebaseDatabase.getInstance().getReference().child("Notification").child(uid);
        mEvents= FirebaseDatabase.getInstance().getReference().child("Events");

        final FirebaseRecyclerAdapter<Noti,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Noti,BlogViewHolder>(
                Noti.class,
                R.layout.card_notification,
                BlogViewHolder.class,
                mRef) {


            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, final Noti model, int position) {
                checkNotification(model);
                viewHolder.setEventName(model.getPlace());


                viewHolder.eventToday.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String postKey= model.getPostKey();


                        DatabaseReference mThisEvent= mEvents.child(postKey);

                        mThisEvent.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                EventClass eventClass= dataSnapshot.getValue(EventClass.class);

                                Intent intent= new Intent(getBaseContext(),EventDetails.class);

                                //Pass the EventClass object to EventDetails class
                                intent.putExtra("EventClass",eventClass);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });

            }
        };


        rView.setAdapter(firebaseRecyclerAdapter);




    }




    public static class BlogViewHolder extends RecyclerView.ViewHolder {


        View mView;
        FirebaseRecyclerAdapter adapter;
        TextView eventToday;
        public BlogViewHolder(View itemView) {
            super(itemView);

            eventToday= (TextView) itemView.findViewById(R.id.eventToday);
            mView= itemView;

        }


        public void setEventName(String title) {
            TextView eventName= mView.findViewById(R.id.eventName);

            eventName.setText("Event at "+title+". Click here to visit the event!");

        }




    }
}
