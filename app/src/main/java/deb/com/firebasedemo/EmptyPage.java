package deb.com.firebasedemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import deb.com.R;

/**
 * Created by Deb on 10/26/2017.
 */

public class EmptyPage extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the view; this adds the fragment view to the view.
        return inflater.inflate(R.layout.empty_blog, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
    }


}
