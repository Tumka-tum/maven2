package app;

import java.util.Objects;

public class Circle {
    Point centre;
    Point pointR;

    public Circle(Point centre, Point pointR) {
        this.centre = centre;
        this.pointR = pointR;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circle circle = (Circle) o;
        return Objects.equals(centre, circle.centre) && Objects.equals(pointR, circle.pointR);
    }

    @Override
    public int hashCode() {
        return Objects.hash(centre, pointR);
    }
}