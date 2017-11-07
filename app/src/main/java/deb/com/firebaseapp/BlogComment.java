package deb.com.firebaseapp;

/**
 * Created by palash on 11/2/17.
 */

public class BlogComment {

    private String BlogComment, BlogPoster;
    private long TimeMilli;

    BlogComment()
    {

    }

    public BlogComment(String blogComment, String blogPoster, long timeMilli) {
        BlogComment = blogComment;
        BlogPoster = blogPoster;
        TimeMilli = timeMilli;
    }

    public String getBlogComment() {
        return BlogComment;
    }

    public void setBlogComment(String blogComment) {
        BlogComment = blogComment;
    }

    public String getBlogPoster() {
        return BlogPoster;
    }

    public void setBlogPoster(String blogPoster) {
        BlogPoster = blogPoster;
    }

    public long getTimeMilli() {
        return TimeMilli;
    }

    public void setTimeMilli(long timeMilli) {
        TimeMilli = timeMilli;
    }
}
