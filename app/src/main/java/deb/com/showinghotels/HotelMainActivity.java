package deb.com.showinghotels;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import deb.com.*;

public class HotelMainActivity extends Fragment {

    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    private Spinner spinner;
    private Button btnSrch;
    private Button imgv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the view; this adds the fragment view to the view.
        return inflater.inflate(R.layout.activity_hotel_main, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();

        spinner=(Spinner) getView().findViewById(R.id.hotels_details);



        String spt=spinner.getSelectedItem().toString();


        mDatabase= FirebaseDatabase.getInstance().getReference().child("Hotels").child(spt);
        Toast.makeText(getContext(),"Showing Hotels of "+spt,Toast.LENGTH_SHORT).show();

        mBlogList=(RecyclerView) getView().findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));


        FirebaseRecyclerAdapter<Hotels,BlogViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Hotels, BlogViewHolder>(

                Hotels.class,
                R.layout.hotel_row,
                BlogViewHolder.class,
                mDatabase) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Hotels model, int position) {

                viewHolder.setActivities(model.getActivities());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setGeneral(model.getGeneral());
                viewHolder.setInternet(model.getInternet());
                viewHolder.setName(model.getName());
                viewHolder.setParking(model.getParking());
                viewHolder.setImage(getActivity().getApplicationContext(),model.getImage());

            }

        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String spt=spinner.getSelectedItem().toString();
                mDatabase= FirebaseDatabase.getInstance().getReference().child("Hotels").child(spt);
                Toast.makeText(getActivity().getApplicationContext(),"Showing Hotels of "+spt,Toast.LENGTH_SHORT).show();


                mBlogList=(RecyclerView) getView().findViewById(R.id.blog_list);
                mBlogList.setHasFixedSize(true);
                mBlogList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));


                FirebaseRecyclerAdapter<Hotels,BlogViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Hotels, BlogViewHolder>(

                        Hotels.class,
                        R.layout.hotel_row,
                        BlogViewHolder.class,
                        mDatabase) {
                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, Hotels model, int position) {


                        viewHolder.setActivities(model.getActivities());
                        viewHolder.setDescription(model.getDescription());
                        viewHolder.setGeneral(model.getGeneral());
                        viewHolder.setInternet(model.getInternet());
                        viewHolder.setName(model.getName());
                        viewHolder.setParking(model.getParking());
                        viewHolder.setImage(getActivity().getApplicationContext(),model.getImage());

                    }

                };
                mBlogList.setAdapter(firebaseRecyclerAdapter);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });






        /*imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String spt=spinner.getSelectedItem().toString();
                mDatabase= FirebaseDatabase.getInstance().getReference().child("Hotels").child(spt);
                Toast.makeText(getApplicationContext(),"Showing Hotels of "+spt,Toast.LENGTH_SHORT).show();


                mBlogList=(RecyclerView) findViewById(R.id.blog_list);
                mBlogList.setHasFixedSize(true);
                mBlogList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


                FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(

                        Blog.class,
                        R.layout.hotel_row,
                        BlogViewHolder.class,
                        mDatabase) {
                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {


                        viewHolder.setActivities(model.getActivities());
                        viewHolder.setDescription(model.getDescription());
                        viewHolder.setGeneral(model.getGeneral());
                        viewHolder.setInternet(model.getInternet());
                        viewHolder.setName(model.getName());
                        viewHolder.setParking(model.getParking());
                        viewHolder.setImage(getApplicationContext(),model.getImage());

                    }

                };
                mBlogList.setAdapter(firebaseRecyclerAdapter);

            }
        });
*/
    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);

            mView=itemView;
        }

        public void setActivities(String activities)
        {
            TextView post_activities=(TextView) mView.findViewById(R.id.activities);
            post_activities.setText(activities);
        }
        public void setDescription(String description) {
            TextView post_description=(TextView) mView.findViewById(R.id.description);
            post_description.setText(description);
        }
        public void setGeneral(String general) {
            TextView post_general=(TextView) mView.findViewById(R.id.general);
            post_general.setText(general);
        }
        public void setImage(Context ctx, String image) {
            ImageView post_image=(ImageView) mView.findViewById(R.id.postImage);
            Picasso.with(ctx).load(image).into(post_image);

        }
        public void setInternet(String internet) {
            TextView post_internet=(TextView) mView.findViewById(R.id.internet);
            post_internet.setText(internet);
        }

        public void setName(String name) {
            TextView post_name=(TextView) mView.findViewById(R.id.name);
            post_name.setText(name);
        }
        public void setParking(String parking) {
            TextView post_parking=(TextView) mView.findViewById(R.id.parking);
            post_parking.setText(parking);
        }
    }
}
