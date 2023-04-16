package app;

import java.util.Objects;

public class Line {
    Point pointA;
    Point pointB;

    public Line(Point pointA, Point pointB) {
        this.pointA = pointA;
        this.pointB = pointB;

    }

    public double getDistance(Point pointC){
        double xA = this.pointA.pos.x;
        double xB = this.pointB.pos.x;
        double yA = this.pointA.pos.y;
        double yB = this.pointB.pos.y;
        double xC = pointC.pos.x;
        double yC = pointC.pos.y;
        double a = yB-yA;
        double b = xA-xB;
        double c = yA*xB-yB*xA;
        double dist = Math.abs(a*xC+b*yC+c)/Math.sqrt(a*a+b*b);
        return dist;
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