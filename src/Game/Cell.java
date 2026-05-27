package Game;

import java.util.Objects;

public record Cell(int row, int colomn) {
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cell(int r1, int c1))) {
            return false;
        }

        return row == r1 && colomn == c1;

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(row) + Objects.hashCode(colomn);
    }
}
