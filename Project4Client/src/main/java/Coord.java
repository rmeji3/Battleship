import java.io.Serializable;
import java.util.Objects;


public class Coord implements Serializable {
    static final long serialVersionUID = 43L;

    Integer row;
    Integer col;

    public Coord(Integer row, Integer col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coord coord = (Coord) obj;
        return Objects.equals(row, coord.row) && Objects.equals(col, coord.col);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return String.format("{%d, %d}", row, col);
    }
}
