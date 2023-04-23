package app;

import java.util.Objects;

public class Line {
    public Point pointA;
    public  Point pointB;


    double a;
    double b;
    double c;

    public Line(Point pointA, Point pointB) {
        this.pointA = pointA;
        this.pointB = pointB;
        // коэффициенты канонического уравнения прямой: ax+by+c=0
        a = this.pointB.pos.y-this.pointA.pos.y;
        b = this.pointA.pos.x-this.pointB.pos.x;
        c = this.pointA.pos.y*this.pointB.pos.x-this.pointB.pos.y*this.pointA.pos.x;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(pointA, line.pointA) && Objects.equals(pointB, line.pointB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pointA, pointB);
    }
}