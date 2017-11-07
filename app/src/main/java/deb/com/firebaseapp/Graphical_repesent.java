package deb.com.firebaseapp;

/**
 * Created by palash on 10/13/17.
 */

public class Graphical_repesent {

    private int foood, guuide, reesort, veehicle, otthes, total;
    private String id;

    public Graphical_repesent() {

    }

    public Graphical_repesent(String id, int foood, int guuide, int reesort, int veehicle, int otthes, int total) {
        this.id = id;
        this.foood = foood;
        this.guuide = guuide;
        this.reesort = reesort;
        this.veehicle = veehicle;
        this.otthes = otthes;
        this.total = total;

    }

    public int getFoood() {
        return foood;
    }

    public void setFoood(int foood) {
        this.foood = foood;
    }

    public int getGuuide() {
        return guuide;
    }

    public void setGuuide(int guuide) {
        this.guuide = guuide;
    }

    public int getReesort() {
        return reesort;
    }

    public void setReesort(int reesort) {
        this.reesort = reesort;
    }

    public int getVeehicle() {
        return veehicle;
    }

    public void setVeehicle(int veehicle) {
        this.veehicle = veehicle;
    }

    public int getOtthes() {
        return otthes;
    }

    public void setOtthes(int otthes) {
        this.otthes = otthes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
