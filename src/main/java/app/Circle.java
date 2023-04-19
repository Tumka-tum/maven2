package app;

import misc.Vector2d;

import java.util.Objects;

public class Circle {
    Point centre;
    Point circ;

    double rad;

    public Circle(Point centre, Point circ) {
        this.centre = centre;
        this.circ = circ;
        //рассчитать радиус окружности
        rad = Math.sqrt((this.circ.pos.x-this.centre.pos.x)*(this.circ.pos.x-this.centre.pos.x) + (this.circ.pos.y-this.centre.pos.y)*(this.circ.pos.y-this.centre.pos.y));
    }

    Line cross(Line l){
        // если линия не пересекает, возвращаем null
        // иначе создаём новую линию на основе переданной в аргументах (Line l),
        // но образующие точки меняются на новые, которые явялются точками пересечения окружности и исходной линии
        if (Math.abs(a*centre.pos.x + b*centre.pos.y + c)/Math.sqrt(a*a+b*b) <= rad){
            //найти координаты точек пересечения
        } else {
            return null;
        }

        // пусть получены две точки пересечения cA, cB
        Vector2d cA = new Vector2d(0,0);
        Vector2d cB = new Vector2d(0,0);


        Line line = new Line(cA,cB);

        return line;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circle circle = (Circle) o;
        return Objects.equals(centre, circle.centre) && Objects.equals(circ, circle.circ);
    }

    @Override
    public int hashCode() {
        return Objects.hash(centre, circ);
    }
}