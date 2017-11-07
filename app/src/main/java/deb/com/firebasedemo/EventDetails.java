package deb.com.firebasedemo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import deb.com.R;


public class EventDetails extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView rView;
    DatabaseReference mRef, mEvent, mComments,mJoinEvent,mThisEvent, goingEvents;
    FirebaseAuth auth;
    ImageView eventImage, imgTick;
    Button btnPostEdit, btnBack, btnCommentIn, btnPostCommentIn, btnJoin;
    String postKey="";
    TextView txtTitle,txtDate,txtUser,txtDuration, txtEventHeader, fldCommentIn, txtMembers, txtSpace;
    LinearLayout expand;

    boolean mProcessJoin= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        auth=FirebaseAuth.getInstance();

        rView= (RecyclerView) findViewById(R.id.rCommentView);
        rView.setLayoutManager(new LinearLayoutManager(this));
        //rView.setHasFixedSize(true);




        final EventClass ec=(EventClass) getIntent().getParcelableExtra("EventClass");

        String eventID= ec.getEventID();

        postKey=ec.getEventID();

        expand= (LinearLayout) findViewById(R.id.expandInside);


        txtMembers= (TextView) findViewById(R.id.inMembers);
        txtSpace= (TextView) findViewById(R.id.inSpace);

        btnPostEdit= (Button) findViewById(R.id.btnPostEdit);
        btnBack= (Button) findViewById(R.id.btnBack);
        btnCommentIn= (Button) findViewById(R.id.commentInside);
        btnPostCommentIn= (Button) findViewById(R.id.postCommentIn);
        btnJoin= (Button) findViewById(R.id.btnJoin);

        fldCommentIn= (TextView) findViewById(R.id.fldCommentIn);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        });

        String currentUser= auth.getCurrentUser().getEmail();
        final String uid= auth.getCurrentUser().getUid();
        String postUser= ec.getUser();

        if(currentUser.equals(postUser))
            btnPostEdit.setVisibility(View.VISIBLE);

        mEvent= FirebaseDatabase.getInstance().getReference().child("Events");
        mRef= FirebaseDatabase.getInstance().getReference().child("Comments").child(eventID);
        mThisEvent= FirebaseDatabase.getInstance().getReference().child("Events").child(eventID);
        mJoinEvent=FirebaseDatabase.getInstance().getReference().child("JoinEvent");
        goingEvents= FirebaseDatabase.getInstance().getReference().child("GoingEvents");

        mEvent.keepSynced(true);
        mRef.keepSynced(true);
        mJoinEvent.keepSynced(true);
        mThisEvent.keepSynced(true);
        goingEvents.keepSynced(true);

        eventImage= (ImageView) findViewById(R.id.eventImage);
        imgTick= (ImageView) findViewById(R.id.tick);

        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.equals(eventImage)){
                    //Toast.makeText(getApplicationContext(),"Don't click on image!",Toast.LENGTH_SHORT).show();
                    final Dialog nagDialog = new Dialog(getApplicationContext());
                    nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    nagDialog.setCancelable(false);
                    nagDialog.setContentView(R.layout.preview_image);
                    Button btnClose = (Button)nagDialog.findViewById(R.id.btnIvClose);
                    ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.iv_preview_image);
                    //ivPreview.setImageResource(dd);
                    Picasso.with(getApplicationContext()).load(ec.getImage()).into(ivPreview);

                    nagDialog.setCancelable(true);

                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {

                            nagDialog.dismiss();
                        }
                    });
                    nagDialog.show();
                }
            }
        });





        btnCommentIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //If button is clicked, toggle the visibility of Enter Comment TextPane
                if(expand.getVisibility()==View.VISIBLE)
                    expand.setVisibility(View.GONE);
                else
                    expand.setVisibility(View.VISIBLE);

            }
        });

        btnPostCommentIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cmnt= fldCommentIn.getText().toString().trim();

                DatabaseReference newComment= mRef.push();

                newComment.child("Comment").setValue(cmnt);
                newComment.child("Poster").setValue(auth.getCurrentUser().getEmail());

                newComment.child("TimeMilli").setValue(System.currentTimeMillis());


                Toast.makeText(getApplicationContext(),"Comment posted successfully!",Toast.LENGTH_SHORT).show();

                //Set Comment TextPane null after comment has been posted
                fldCommentIn.setText("");

            }
        });

        mThisEvent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    EventClass newModel = dataSnapshot.getValue(EventClass.class);
                    txtMembers.setText(newModel.getGoing()+"");
                    txtSpace.setText(newModel.getSpace()+"");

                }
                else{
                    //Toast.makeText(EventDetails.this, "No datasnapshot found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mJoinEvent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Going
                if(dataSnapshot.child(postKey).hasChild(auth.getCurrentUser().getUid())){
                    btnJoin.setBackgroundColor(Color.parseColor("#0e7b2b"));
                    imgTick.setVisibility(View.VISIBLE);
                    btnJoin.setText("Going");

                }
                //Not Going
                else{
                    btnJoin.setBackgroundColor(Color.parseColor("#e91e28"));
                    imgTick.setVisibility(View.GONE);
                    btnJoin.setText("Join");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final int numSpace= ec.getSpace();
        final int numGoing= ec.getGoing();

        btnJoin.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   mProcessJoin = true;

                   mJoinEvent.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {

                           int Space= Integer.parseInt(txtSpace.getText().toString());
                           int Members= Integer.parseInt(txtMembers.getText().toString());

                           if (mProcessJoin) {

                               //Not Going
                               if (dataSnapshot.child(postKey).hasChild(uid)) {
                                   mJoinEvent.child(postKey).child(uid).removeValue();
                                   goingEvents.child(uid).child(postKey).removeValue();

                                   /*viewHolder.totalLikes.setText(""+(model.getLikes()-1));*/
                                    //mEvent.child(postKey).child("Going").setValue(ec.getGoing()-1);

                                   mThisEvent.child("going").setValue(Members-1);
                                   mProcessJoin = false;


                               }
                               //Join
                               else {
                                   if(Members>=Space)
                                   {

                                       AlertDialog alertDialog = new AlertDialog.Builder(EventDetails.this).create();
                                       alertDialog.setTitle("No Space Left");
                                       alertDialog.setMessage("Enough Members Are Recruited");
                                       alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                               new DialogInterface.OnClickListener() {
                                                   public void onClick(DialogInterface dialog, int which) {
                                                       dialog.dismiss();
                                                   }
                                               });
                                       alertDialog.show();

                                       mProcessJoin = false;
                                   }
                                   else {
                                       mThisEvent.child("going").setValue(Members+1);

                                       goingEvents.child(uid).child(postKey).setValue(postKey);


                                       mJoinEvent.child(postKey).child(uid).setValue(postKey);


                                    /*viewHolder.totalLikes.setText(""+(model.getLikes()+1));
                                     mRef.child(post_key).child("Likes").setValue(model.getLikes()+1);*/

                                       mProcessJoin = false;

                                   }
                               }
                           }

                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
               }
           });


        //Load event image at the imageview
        String image= ec.getImage();
        Picasso.with(getApplicationContext()).load(image).into(eventImage);

        putElements(ec);

    }


    public void showPopUp(View view){

        PopupMenu popup= new PopupMenu(this,view);

        popup.setOnMenuItemClickListener(EventDetails.this);

        MenuInflater inflater= popup.getMenuInflater();

        inflater.inflate(R.menu.menu_post_edit,popup.getMenu());

        popup.show();




    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuPostDelete:
                mEvent.child(postKey).removeValue();
                startActivity(new Intent(getApplicationContext(),HomePage.class));
                Toast.makeText(getApplicationContext(), "Event removed", Toast.LENGTH_SHORT).show();
                break;

        }

        return true;
    }

    //Initialize items on the view
    void putElements(EventClass ec){
        String title= ec.getTitle();
        String email= ec.getUser();
        String date= ec.getDate();
        String duration= ec.getDuration();
        String time= ec.getTime();
        String space= ec.getSpace()+"";
        String going= ec.getGoing()+"";

        txtTitle= (TextView) findViewById(R.id.inTitle);
        txtDate= (TextView) findViewById(R.id.inDate);
        txtDuration= (TextView) findViewById(R.id.inDuration);
        txtUser= (TextView) findViewById(R.id.inUser);
        txtEventHeader= (TextView) findViewById(R.id.eventHeader);
        txtMembers= (TextView) findViewById(R.id.inMembers);


        String userName= GetUserName.getUserName(email);

        txtEventHeader.setText(title);
        txtTitle.setText(title);
        txtUser.setText(userName+ " (" +time + ")");
        txtDuration.setText(duration);
        txtDate.setText(date);
        //txtMembers.setText(going+"/"+space);



    }




    @Override
    protected void onStart() {
        super.onStart();


        final FirebaseRecyclerAdapter<CommentClass, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CommentClass, BlogViewHolder>(
                CommentClass.class,
                R.layout.comment_row,
                BlogViewHolder.class,
                mRef) {
            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, final CommentClass model, int position) {

                //Set comment, commenter, poster

                viewHolder.setComment(model.getComment());
                viewHolder.setCommenter(model.getPoster());
                viewHolder.setTime(model.getTimeMilli());
            }
        };


        firebaseRecyclerAdapter.notifyDataSetChanged();
        rView.setAdapter(firebaseRecyclerAdapter);




    }




    public static class BlogViewHolder extends ViewHolder {

        TextView commenter, comment;
        View mView;
        FirebaseRecyclerAdapter adapter;

        public BlogViewHolder(View itemView) {
            super(itemView);


            mView= itemView;

        }


        public void setComment(String cmnt) {
            TextView comment= mView.findViewById(R.id.thisComment);

            comment.setText(cmnt);

        }

        public void setCommenter(String cmntr) {
            TextView commenter= mView.findViewById(R.id.commenter);

            commenter.setText(cmntr);

        }

        public void setImage(Context ctx, String image){
            ImageView post_img= mView.findViewById(R.id.eventImage);
            Picasso.with(ctx).load(image).into(post_img);

        }

        public void setTime(long timeThen){
            String time="";

            long timeNow= System.currentTimeMillis();

            long timeDiff=timeNow-timeThen;

            long days= timeDiff/ (24*60*60*1000);
            long hours= TimeUnit.MILLISECONDS.toHours(timeDiff);
            long minutes= TimeUnit.MILLISECONDS.toMinutes(timeDiff);

        if(days!=0)
            time=days+"d";
        else if (hours!=0)
            time=hours+"h";
        else
            time=minutes+"m";

            //time=""+minutes+"m";

            TextView txtTime= (TextView) mView.findViewById(R.id.thisTime);
            txtTime.setText(time);


        }

    }
}