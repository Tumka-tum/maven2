package app;

import misc.Vector2d;

import java.util.Objects;

public class Circle {
    Point centre;
    Point pointR;

    double rad;

    public Circle(Point centre, Point pointR) {
        this.centre = centre;
        this.pointR = pointR;
        // ToDo: рассчитать радиус окружности
        rad = 0;
    }

    Line cross(Line l){
        // если линия не пересекает, возвращаем null
        // иначе создаём новую линию на основе переданной в аргументах (Line l),
        // но образующие точки меняются на новые, которые явялются точками пересечения окружности и исходной линии

        // пусть получены две точки пересечения cA, cB
        Vector2d cA = new Vector2d(0,0);
        Vector2d cB = new Vector2d(0,0);


        Line l = new Line(cA,cB);

        return new Line(cA,cB);
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