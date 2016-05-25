import java.util.List;

/**
 * Created by harshit on 23/5/16.
 */
public class CricketMatchListModel {
    @Override
    public String toString() {
        return new StringBuilder().append("match id:").
                append(id).append("team 1:").
                append(t1).append("team2:").
                append(t2).toString();
    }

    private int id;
    private String t2;
    private String t1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getT2() {
        return t2;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CricketMatchListModel that = (CricketMatchListModel) o;

        if (id != that.id) return false;
        if (!t2.equals(that.t2)) return false;
        return t1.equals(that.t1);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + t2.hashCode();
        result = 31 * result + t1.hashCode();
        return result;
    }
}
