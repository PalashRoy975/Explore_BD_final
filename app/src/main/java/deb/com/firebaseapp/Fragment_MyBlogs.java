package deb.com.firebaseapp;

import deb.com.*;
import deb.com.firebasedemo.ActivityPost;
import deb.com.firebasedemo.GetUserName;
import deb.com.firebasedemo.ViewProfile;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.data.DataBufferObserverSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Fragment_MyBlogs extends Fragment {

    DatabaseReference BloginfoDetails, BlogLike, Blogcomment;
    private boolean mProcessLike = false;
    FirebaseAuth auth;
    Button btnPostBlog;
    TextView txtPostBlog, postUser;
    String postID;
    RecyclerView mblog;
    LinearLayout layoutBlogPost;
    Toolbar blogToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the view; this adds the fragment view to the view.
        return inflater.inflate(R.layout.card_design, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        blogToolbar= (Toolbar) getView().findViewById(R.id.blogfeedToolbar);
        layoutBlogPost= (LinearLayout) getView().findViewById(R.id.layoutBlogPost);
        layoutBlogPost.setVisibility(View.GONE);
        blogToolbar.setVisibility(View.GONE);

        auth= FirebaseAuth.getInstance();

        mblog = (RecyclerView) getView().findViewById(R.id.blog_list);
        mblog.setLayoutManager(new LinearLayoutManager(getContext()));


        btnPostBlog= (Button) getView().findViewById(R.id.btnGoPostBlog);
        txtPostBlog= (TextView) getView().findViewById(R.id.txtGoPostBlog);

        BloginfoDetails = FirebaseDatabase.getInstance().getReference().child("Blog");
        BlogLike = FirebaseDatabase.getInstance().getReference().child("BlogLikes");
        Blogcomment = FirebaseDatabase.getInstance().getReference("BlogComments");
        auth = FirebaseAuth.getInstance();


        btnPostBlog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ActivityBlogPost.class));
            }
        });


        txtPostBlog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ActivityBlogPost.class));
            }
        });

        Query mQueryRef = BloginfoDetails.orderByChild("email").equalTo(auth.getCurrentUser().getEmail());

        FirebaseRecyclerAdapter<BlogDetails, BlogViewHolder> mFirebase = new FirebaseRecyclerAdapter<BlogDetails, BlogViewHolder>(
                BlogDetails.class,
                R.layout.showingblog,
                BlogViewHolder.class,
                mQueryRef) {

            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, final BlogDetails model, final int position) {

                final String post_key = getRef(position).getKey();
                viewHolder.settourName(model.getTourName());
                viewHolder.setRb(model.getRb());

                String email= model.getEmail();
                String userName="";

                for(int i=0;i<email.length();i++){

                    if(email.charAt(i)=='@')
                        break;

                    userName+=email.charAt(i);
                }

                viewHolder.setUser(userName);
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getContext(), model.getImage());
                viewHolder.setLike(post_key);
                viewHolder.totalLikes.setText(""+(model.getLikes()));

                viewHolder.post_img.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent blogSingleActivity = new Intent(getContext(), BlogSingleActivity.class);
                        blogSingleActivity.putExtra("blog_id", model);
                        startActivity(blogSingleActivity);

                    }
                });

                viewHolder.postUser.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(getActivity().getBaseContext(),ViewProfile.class);
                        String email=model.getEmail();
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                });

                viewHolder.btnBlogComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (viewHolder.LI.getVisibility() == View.VISIBLE)
                            viewHolder.LI.setVisibility(View.GONE);
                        else
                            viewHolder.LI.setVisibility(View.VISIBLE);

                    }
                });

                final String BlogID = model.getTourId();

                viewHolder.btnBlogSeeComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), BlogCommentDetails.class);
                        intent.putExtra("BlogComment", model);

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        startActivity(intent);
                    }
                });

                viewHolder.btnBlogPostComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String cmnt = viewHolder.fldBlogComment.getText().toString();

                        DatabaseReference newComment = Blogcomment.child(BlogID).push();

                        BlogComment bc= new BlogComment(cmnt,auth.getCurrentUser().getEmail(),System.currentTimeMillis());

                        newComment.setValue(bc);


                        Toast.makeText(getContext(), "Comment posted successfully!", Toast.LENGTH_LONG).show();

                        viewHolder.fldBlogComment.setText("");

                    }
                });

                viewHolder.mLikeButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mProcessLike = true;


                        BlogLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //UnLike
                                if (mProcessLike) {
                                    if (dataSnapshot.child(post_key).hasChild(auth.getCurrentUser().getUid())) {
                                        BlogLike.child(post_key).child(auth.getCurrentUser().getUid()).removeValue();

                                        BloginfoDetails.child(post_key).child("likes").setValue(model.getLikes()-1);
                                        mProcessLike = false;
                                        //like
                                    } else {
                                        BlogLike.child(post_key).child(auth.getCurrentUser().getUid()).setValue("Randomvalues");

                                        BloginfoDetails.child(post_key).child("likes").setValue(model.getLikes()+1);
                                        mProcessLike = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
/*
                viewHolder.btnShowExpenditure.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getBaseContext(), graph.class);
                        intent.putExtra("Expend", model);
                        startActivity(intent);
                    }
                });*/

            }
        };
        mblog.setAdapter(mFirebase);
        mblog.getItemAnimator().setChangeDuration(0);

    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mview;

        ImageButton mLikeButton;
        Button btnShowExpenditure;
        DatabaseReference mdatabaseLike;
        FirebaseAuth auth;
        EditText fldBlogComment;
        TextView totalLikes;
        Button btnBlogComment, btnBlogPostComment, btnBlogSeeComments;
        LinearLayout LI;
        ImageView post_img;
        TextView postUser;


        public BlogViewHolder(View itemView) {
            super(itemView);

            mview = itemView;
            postUser= (TextView) mview.findViewById(R.id.UserName);
            post_img = itemView.findViewById(R.id.blogImage);
            mLikeButton = (ImageButton) mview.findViewById(R.id.like);
            mdatabaseLike = FirebaseDatabase.getInstance().getReference().child("BlogLikes");
            btnBlogComment = (Button) mview.findViewById(R.id.Blogcomment);
            btnBlogSeeComments = (Button) mview.findViewById(R.id.BlogseeComments);
            btnBlogPostComment = (Button) mview.findViewById(R.id.BlogpostComment);
            fldBlogComment = (EditText) mview.findViewById(R.id.BlogfldComment);
            totalLikes = (TextView) mview.findViewById(R.id.blognoLikes);
            LI = (LinearLayout) mview.findViewById(R.id.Blogexpand);
            auth = FirebaseAuth.getInstance();

        }

        public void setLike(final String post_key) {
            mdatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(post_key).hasChild(auth.getCurrentUser().getUid())) {
                        mLikeButton.setImageResource(R.drawable.ic_like_blue);

                    } else {

                        mLikeButton.setImageResource(R.drawable.ic_like_grey);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


        public void settourName(String tourName) {

            TextView nameview = (TextView) mview.findViewById(R.id.textViewName);
            nameview.setText(tourName);
        }

        public void setRb(float rb) {
            RatingBar rating = (RatingBar) mview.findViewById(R.id.ratingBar);
            rating.setRating(rb);

        }

        public void setDesc(String desc) {
            TextView desep = (TextView) mview.findViewById(R.id.Tdesc);
            desep.setText(desc);
        }

        public void setUser(String user) {
            TextView name = (TextView) mview.findViewById(R.id.UserName);
            name.setText(user);
        }

        public void setImage(Context applicationContext, String image) {
            ImageView post_img = itemView.findViewById(R.id.blogImage);
            Picasso.with(applicationContext).load(image).into(post_img);
        }

        public void setLikes(String like) {

            totalLikes.setText(like);

        }

    }


}

