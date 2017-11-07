package deb.com.firebasedemo;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import deb.com.R;

public class NewsFeed extends Fragment {

    RecyclerView rView;
    DatabaseReference mRef, mComments, mUserLikes, refBackGround, mLike, mGoingEvents,mNotification;
    FirebaseAuth auth;
    CardView cardView;
    String postID;
    TextView barName, txt_Post;
    LinearLayout layoutPost;
    Toolbar feedToolbar;
    Button btn_Post;
    RelativeLayout newsfeedBack;

    boolean mProcessLike= false;

    static int option;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the view; this adds the fragment view to the view.
        return inflater.inflate(R.layout.activity_news_feed, container, false);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_add){

            ActivityPost activityPost= new ActivityPost();
            Intent intent= new Intent(getActivity().getApplicationContext(),ActivityPost.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }*/


    void checkDate(String postDate, final String postKey, final String title){

        String datePicked= postDate;
        mGoingEvents= FirebaseDatabase.getInstance().getReference().child("GoingEvents");
        mNotification= FirebaseDatabase.getInstance().getReference().child("Notification");
        final String uid= auth.getCurrentUser().getUid();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        try {
            strDate = sdf.parse(datePicked);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(DateUtils.isToday(strDate.getTime()))
        {
            mGoingEvents.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(uid).hasChild(postKey)){
                        String pattern = "dd/MM/yyyy";
                        String date =new SimpleDateFormat(pattern).format(new Date());

                        Noti nt= new Noti(postKey,title,date);

                        DatabaseReference newNoti= mNotification.child(uid).child(postKey);
                        newNoti.setValue(nt);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if (new Date().after(strDate)) {
            mRef.child(postKey).removeValue();
        }
        else {

        }


    }


    @Override
    public void onStart() {
        super.onStart();

        auth=FirebaseAuth.getInstance();

        refBackGround= FirebaseDatabase.getInstance().getReference().child("Background");

        Query mBack= refBackGround.child(auth.getCurrentUser().getUid());

        newsfeedBack= (RelativeLayout) getView().findViewById(R.id.newsfeed_background);

        mBack.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    String opt = dataSnapshot.getValue(String.class);
                    option = Integer.parseInt(opt);
                }
                else if (!dataSnapshot.exists()){
                    option=-1;
                }

                if(option==-1){
                    newsfeedBack.setBackgroundColor(Color.parseColor("#fbfbfb"));
                }
                if(option==1){
                    newsfeedBack.setBackgroundResource(R.mipmap.background4);
                }
                else if(option==3){
                    newsfeedBack.setBackgroundResource(R.mipmap.background8);
                }
                else if (option==2){
                    newsfeedBack.setBackgroundResource(R.mipmap.background11);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        rView= (RecyclerView) getView().findViewById(R.id.rView);
        rView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        barName= getView().findViewById(R.id.barName);
        feedToolbar= getView().findViewById(R.id.newsfeedToolbar);



        //The name for this action bar
        barName.setText("Events");

        //Set this toolbar "Event" for news feed
        feedToolbar.setVisibility(View.VISIBLE);



        layoutPost= (LinearLayout) getView().findViewById(R.id.layoutPost);
        btn_Post= (Button) getView().findViewById(R.id.btnGoPostEvent);
        txt_Post= (TextView) getView().findViewById(R.id.txtGoPostEvent);





        layoutPost.setVisibility(View.VISIBLE);

        txt_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityPost activityPost= new ActivityPost();
                Intent intent= new Intent(getActivity().getApplicationContext(),ActivityPost.class);
                startActivity(intent);
            }
        });

        btn_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityPost activityPost= new ActivityPost();
                Intent intent= new Intent(getActivity().getApplicationContext(),ActivityPost.class);
                startActivity(intent);
            }
        });


        //Get current users UID to use as primary key
        String uid= auth.getCurrentUser().getUid();





        mRef= FirebaseDatabase.getInstance().getReference().child("Events");
        mComments= FirebaseDatabase.getInstance().getReference().child("Comments");
        mUserLikes= FirebaseDatabase.getInstance().getReference().child("Likes");
        mLike=FirebaseDatabase.getInstance().getReference().child("Like");


        mRef.keepSynced(true);
        mComments.keepSynced(true);
        mUserLikes.keepSynced(true);
        mLike.keepSynced(true);

        Query qRef= mRef.orderByChild("timeMilli");

        //RecyclerAdapter, used to populate the card view (BlogViewHolder)
        final FirebaseRecyclerAdapter<EventClass,BlogViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<EventClass, BlogViewHolder>(
                EventClass.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                qRef)
        {
            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, final EventClass model, int position) {

                final String uid= auth.getCurrentUser().getUid();
                final String post_key= model.getEventID();

                checkDate(model.getDate(),model.getEventID(),model.getTitle());

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDate());
                viewHolder.setDuration(model.getDuration());
                viewHolder.setLikes(""+model.getLikes());

                viewHolder.setBtnLike(model.getEventID());

                //viewHolder.checkLikes(model.getEventID(),model.getLikes());

                viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());

                String email= model.getUser();
                String userName="";

                for(int i=0;i<email.length();i++){

                    if(email.charAt(i)=='@')
                        break;

                    userName+=email.charAt(i);
                }

                viewHolder.setUser(userName);
                viewHolder.setTime("(" + model.getTime() + ")");




                viewHolder.mBtnLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mProcessLike= true;

                        mLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                if(mProcessLike){

                                    //UnLike
                                    if(dataSnapshot.child(post_key).hasChild(uid)){
                                        mLike.child(post_key).child(uid).removeValue();


                                        viewHolder.totalLikes.setText(""+(model.getLikes()-1));
                                        mRef.child(post_key).child("Likes").setValue(model.getLikes()-1);

                                        mProcessLike=false;


                                    }
                                    //Like
                                    else{
                                        mLike.child(post_key).child(uid).setValue("RandomValue");

                                        viewHolder.totalLikes.setText(""+(model.getLikes()+1));
                                        mRef.child(post_key).child("Likes").setValue(model.getLikes()+1);

                                        mProcessLike=false;




                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });

                viewHolder.btnComment.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        //If button is clicked, toggle the visibility of Enter Comment TextPane
                        if(viewHolder.linearLayout.getVisibility()==View.VISIBLE)
                            viewHolder.linearLayout.setVisibility(View.GONE);
                        else
                            viewHolder.linearLayout.setVisibility(View.VISIBLE);

                    }
                });

                final String evID= model.getEventID();

                viewHolder.btnSeeComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(getActivity().getBaseContext(),EventDetails.class);

                        //Pass the EventClass object to EventDetails class
                        intent.putExtra("EventClass",model);
                        startActivity(intent);
                    }
                });



                viewHolder.btnPostComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String cmnt= viewHolder.fldComment.getText().toString();

                        DatabaseReference newComment= mComments.child(evID).push();

                        newComment.child("Comment").setValue(cmnt);
                        newComment.child("Poster").setValue(auth.getCurrentUser().getEmail());

                        //put Time in MilliSeconds to query to show most recent feeds first
                        newComment.child("TimeMilli").setValue(System.currentTimeMillis());


                        Toast.makeText(getActivity().getApplicationContext(),"Comment posted successfully!",Toast.LENGTH_LONG).show();

                        //Set Comment TextPane null after comment has been posted
                        viewHolder.fldComment.setText("");

                    }
                });

                viewHolder.post_Title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(getActivity().getBaseContext(),EventDetails.class);

                        //Pass the EventClass object to EventDetails class
                        intent.putExtra("EventClass",model);
                        startActivity(intent);
                    }
                });

                viewHolder.post_Description.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(getActivity().getBaseContext(),EventDetails.class);

                        //Pass the EventClass object to EventDetails class
                        intent.putExtra("EventClass",model);
                        startActivity(intent);
                    }
                });

                viewHolder.post_Dur.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(getActivity().getBaseContext(),EventDetails.class);

                        //Pass the EventClass object to EventDetails class
                        intent.putExtra("EventClass",model);
                        startActivity(intent);
                    }
                });

                viewHolder.post_User.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(getActivity().getBaseContext(),ViewProfile.class);
                        String email=model.getUser();
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                });


                viewHolder.post_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent= new Intent(getActivity().getBaseContext(),EventDetails.class);

                        //Pass the EventClass object to EventDetails class
                        intent.putExtra("EventClass",model);
                        startActivity(intent);


                        /*final Dialog nagDialog = new Dialog(getContext());
                        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        nagDialog.setCancelable(false);
                        nagDialog.setContentView(R.layout.preview_image);
                        Button btnClose = (Button)nagDialog.findViewById(R.id.btnIvClose);
                        ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.iv_preview_image);
                        //ivPreview.setImageResource(dd);
                        Picasso.with(getActivity().getApplicationContext()).load(model.getImage()).into(ivPreview);

                        nagDialog.setCancelable(true);

                        btnClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {

                                nagDialog.dismiss();
                            }
                        });
                        nagDialog.show();*/


                    }
                });





            }
        };



        rView.setAdapter(firebaseRecyclerAdapter);

        // Set Animator duration zero to prevent any animation during
        // item change
        rView.getItemAnimator().setChangeDuration(0);





    }

    //ViewHolderClass, representing each event in Card View
    public static class BlogViewHolder extends ViewHolder{
        final View mView;
        EditText fldComment;
        TextView totalLikes, post_User, post_Title, post_Description, post_Dur, duration;
        ImageView post_img;
        Button btnComment, btnPostComment, btnSeeComments;
        LinearLayout linearLayout;
        likeCheck btnLike;
        ImageButton mBtnLike;

        FirebaseAuth auth;
        DatabaseReference mLike;


        //Construct the cardView inner items
        public BlogViewHolder(View itemView) {
            super(itemView);

            totalLikes= itemView.findViewById(R.id.noLikes);
            fldComment= (EditText) itemView.findViewById(R.id.fldComment);
            btnComment= (Button) itemView.findViewById(R.id.comment);
            btnSeeComments= (Button) itemView.findViewById(R.id.seeComments);

            post_img=(ImageView) itemView.findViewById(R.id.postImage);


            post_Title= (TextView) itemView.findViewById(R.id.postTitle);
            post_Description= (TextView) itemView.findViewById(R.id.postDesc);
            post_Dur= (TextView) itemView.findViewById(R.id.postDuration);
            post_User= itemView.findViewById(R.id.postUser);

            btnPostComment = (Button) itemView.findViewById(R.id.postComment);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.expand);
            //btnLike=(likeCheck) itemView.findViewById(R.id.likes);
            mBtnLike=(ImageButton) itemView.findViewById(R.id.like_btn);

            auth= FirebaseAuth.getInstance();
            mLike= FirebaseDatabase.getInstance().getReference().child("Like");


            mView= itemView;



        }



        public void setBtnLike(final String postKey){

            mLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(postKey).hasChild(auth.getCurrentUser().getUid())){

                        mBtnLike.setImageResource(R.drawable.ic_like_blue);
                    }
                    else{
                        mBtnLike.setImageResource(R.drawable.ic_like_grey);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        }



        public void setTitle(String Title){
            TextView post_Title= (TextView) mView.findViewById(R.id.postTitle);
            post_Title.setText(Title);

        }

        public void setDescription(String Description){
            TextView post_Description= (TextView) mView.findViewById(R.id.postDesc);
            post_Description.setText(Description);

        }

        public void setImage(Context ctx, String image){
            ImageView post_img= mView.findViewById(R.id.postImage);
            //User picasso to load the image with Context ctx
            Picasso.with(ctx).load(image).into(post_img);

        }

        public void setUser(String User){
            TextView post_User= (TextView) mView.findViewById(R.id.postUser);
            post_User.setText(User);

        }

        public void setDuration(String Dur){
            TextView post_Dur= (TextView) mView.findViewById(R.id.postDuration);
            post_Dur.setText(Dur);

        }

        public void setTime(String Time){
            TextView post_Time= (TextView) mView.findViewById(R.id.postTime);
            post_Time.setText(Time);

        }


        public void setLikes(String like){
            totalLikes.setText(like);
        }






    }
}
