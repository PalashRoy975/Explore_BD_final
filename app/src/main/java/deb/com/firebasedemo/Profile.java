package deb.com.firebasedemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import deb.com.R;

public class Profile extends AppCompatActivity {

    RecyclerView rView;
    DatabaseReference mRef;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        rView= (RecyclerView) findViewById(R.id.rView);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(new LinearLayoutManager(this));

        auth=FirebaseAuth.getInstance();

        String uid= auth.getCurrentUser().getUid();
        mRef= FirebaseDatabase.getInstance().getReference().child("MyEvents").child(uid);




    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<EventClass,BlogViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<EventClass, BlogViewHolder>(
                EventClass.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mRef)
        {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, EventClass model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDate());
                viewHolder.setDuration(model.getDuration());

                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setUser(model.getUser() + " (" + model.getTime() + ")");
            }
        };

        rView.setAdapter(firebaseRecyclerAdapter);



    }

    public static class BlogViewHolder extends ViewHolder{
        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);

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
