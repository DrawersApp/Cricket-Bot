/**
 * Created by harshit on 23/5/16.
 */
public class CricketMatchModel {
    private String de;
    private int id;
    private String si;

    public CricketMatchModel(String de, int id, String si) {
        this.de = de;
        this.id = id;
        this.si = si;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSi() {
        return si;
    }

    public void setSi(String si) {
        this.si = si;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CricketMatchModel that = (CricketMatchModel) o;

        if (id != that.id) return false;
        if (de != null ? !de.equals(that.de) : that.de != null) return false;
        return si != null ? si.equals(that.si) : that.si == null;

    }

    @Override
    public int hashCode() {
        int result = de != null ? de.hashCode() : 0;
        result = 31 * result + id;
        result = 31 * result + (si != null ? si.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "description: " + de + "\nid: " + id + "\nsides: " + si;
    }
}
