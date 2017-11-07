package deb.com.firebasedemo;

/**
 * Created by Deb on 11/3/2017.
 */

public class GetUserName {

    static String getUserName(String email){
        String userName="";

        for(int i=0;i<email.length();i++){

            if(email.charAt(i)=='@')
                break;

            userName+=email.charAt(i);
        }

        return userName;
    }


}
