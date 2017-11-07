package deb.com.firebasedemo;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import deb.com.R;

/**
 * Created by Deb on 10/20/2017.
 */

//Fragment sub-class to be adapted by TabProfile
public class Fragment_MyEvents extends Fragment{

    RecyclerView rView;
    DatabaseReference mRef;
    FirebaseAuth auth;
    LinearLayout layoutPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the view; this adds the fragment view to the view.
        return inflater.inflate(R.layout.activity_news_feed, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        rView= (RecyclerView) getView().findViewById(R.id.rView);
        //rView.setHasFixedSize(true);
        rView.setLayoutManager(new LinearLayoutManager(getActivity()));

        auth=FirebaseAuth.getInstance();

        //Get current user's UID and Email
        String uid= auth.getCurrentUser().getUid();
        String email= auth.getCurrentUser().getEmail();

        layoutPost= (LinearLayout) getView().findViewById(R.id.layoutPost);
        layoutPost.setVisibility(View.GONE);

        mRef= FirebaseDatabase.getInstance().getReference().child("Events");

        //Query on Events to find childs that refer to
        //current users Eventss
        Query qRef= mRef.orderByChild("user").equalTo(email);

        //RecyclerAdapter, used to populate the card view (BlogViewHolder)
        FirebaseRecyclerAdapter<EventClass,Profile.BlogViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<EventClass, Profile.BlogViewHolder>(
                EventClass.class,
                R.layout.profile_event_row,
                Profile.BlogViewHolder.class,
                qRef)
        {
            @Override
            protected void populateViewHolder(Profile.BlogViewHolder viewHolder, EventClass model, int position) {

                //SetUp the viewHolder (Card View) using EventClass object
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDate());
                viewHolder.setDuration(model.getDuration());
                viewHolder.setImage(getActivity(), model.getImage());
                viewHolder.setUser(model.getUser() + " (" + model.getTime() + ")");

            }
        };

        rView.setAdapter(firebaseRecyclerAdapter);



    }

    //ViewHolderClass, representing each event in Card View
    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        LinearLayout linearLayout;

        public BlogViewHolder(View itemView) {
            super(itemView);

            //No like, comment option for my profile
            linearLayout= (LinearLayout) itemView.findViewById(R.id.layoutLikeComments);
            linearLayout.setVisibility(View.GONE);


            mView= itemView;
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
    }
}