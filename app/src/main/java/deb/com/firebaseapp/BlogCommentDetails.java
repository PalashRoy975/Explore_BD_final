package deb.com.firebaseapp;

import deb.com.R;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

public class BlogCommentDetails extends AppCompatActivity {

    RecyclerView mblog;
    DatabaseReference mRef;
    FirebaseAuth auth;
    ImageView blogImage;
    TextView txtTitle, txtUserName, txtDescription;
    RatingBar rbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_comment_details);
        BlogDetails BC = (BlogDetails) getIntent().getParcelableExtra("BlogComment");

        String blogID = BC.getTourId();
        mRef = FirebaseDatabase.getInstance().getReference().child("BlogComments").child(blogID);

        mRef.keepSynced(true);

        mblog = (RecyclerView) findViewById(R.id.BlogCommentView);
        mblog.setHasFixedSize(true);
        mblog.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        blogImage = (ImageView) findViewById(R.id.BlogImage);
        String image = BC.getImage();
        Picasso.with(getApplicationContext()).load(image).into(blogImage);
        String title = BC.getTourName();
        String username = BC.getEmail();
        float rating = BC.getRb();
        String description = BC.getDesc();

        txtTitle = (TextView) findViewById(R.id.BlogTitle);
        txtUserName = (TextView) findViewById(R.id.BlogUser);
        txtDescription = (TextView) findViewById(R.id.BlogDescription);
        rbar = (RatingBar) findViewById(R.id.BlogratingBar);

        txtTitle.setText(BC.getTourName());
        txtUserName.setText(username);
        txtDescription.setText(description);



    }

    protected void onStart() {
        super.onStart();
        final FirebaseRecyclerAdapter<BlogComment, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BlogComment, BlogViewHolder>(
                BlogComment.class,
                R.layout.commentrow,
                BlogViewHolder.class,
                mRef) {
            protected void populateViewHolder(final BlogViewHolder viewHolder, final BlogComment model, int position) {


                viewHolder.setComment(model.getBlogComment());
                viewHolder.setCommenter(model.getBlogPoster());
                viewHolder.setTime(model.getTimeMilli());
            }

        };

        mblog.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;
        FirebaseRecyclerAdapter adapter;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setComment(String cmnt) {

            TextView comment = (TextView) mView.findViewById(R.id.BlogthisComment);
            comment.setText(cmnt);

        }

        public void setCommenter(String cmntr) {
            TextView commenter = (TextView) mView.findViewById(R.id.Blogcommenter);
            commenter.setText(cmntr);

        }

        public void setTime(long timeThen) {
            String time = "";

            long timeNow = System.currentTimeMillis();

            long timeDiff = timeNow - timeThen;

            long days = timeDiff / (24 * 60 * 60 * 1000);
            long hours = TimeUnit.MILLISECONDS.toHours(timeDiff);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff);

            if (days != 0)
                time = days + "d";
            else if (hours != 0)
                time = hours + "h";
            else
                time = minutes + "m";


            TextView txtTime = (TextView) mView.findViewById(R.id.BlogthisTime);
            txtTime.setText(time + " ago");


        }


    }
}


