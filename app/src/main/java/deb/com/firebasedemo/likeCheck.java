package deb.com.firebasedemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import deb.com.R;

/**
 * Created by Deb on 10/14/2017.
 */

@SuppressLint("AppCompatCustomView")
public class likeCheck extends CheckBox {


    public likeCheck(Context context, AttributeSet attrs) {
    super(context, attrs);
    //setButtonDrawable(new StateListDrawable());
}
    @Override
    public void setChecked(boolean t){
        if(t)
        {
            this.setBackgroundResource(R.drawable.lselect);
        }
        else
        {
            this.setBackgroundResource(R.drawable.ldeselect);
        }
        super.setChecked(t);
    }
}
