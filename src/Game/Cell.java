package Game;

import java.util.Objects;

public record Cell(int x, int y) {
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cell(int x1, int y1))) {
            return false;
        }

        return x == x1 && y == y1;

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(x) + Objects.hashCode(y);
    }
}
