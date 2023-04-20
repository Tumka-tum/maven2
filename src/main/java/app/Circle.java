package app;

import misc.Vector2d;

import java.util.Objects;

public class Circle {
    Point centre;
    Point circ;

    double rad;
    double t;
    double k;
    double v;
    double y1;
    double y2;
    double x1;
    double x2;

    public Circle(Point centre, Point circ) {
        this.centre = centre;
        this.circ = circ;
        //рассчитать радиус окружности
        rad = Math.sqrt((this.circ.pos.x - this.centre.pos.x) * (this.circ.pos.x - this.centre.pos.x) + (this.circ.pos.y - this.centre.pos.y) * (this.circ.pos.y - this.centre.pos.y));
    }

   public Line cross(Line l) {
        // если линия не пересекает, возвращаем null
        // иначе создаём новую линию на основе переданной в аргументах (Line l),
        // но образующие точки меняются на новые, которые явялются точками пересечения окружности и исходной линии
        if (Math.abs(l.a * centre.pos.x + l.b * centre.pos.y + l.c) / Math.sqrt(l.a * l.a + l.b * l.b) <= rad) {
            // найти координаты точек пересечения
            t = l.a * l.a + l.b * l.b;
            v = l.a * l.b * centre.pos.x + l.b * l.c - l.a * l.a * centre.pos.y;
            k = l.a * l.a * centre.pos.x * centre.pos.x + l.a * l.c + l.c * l.c + l.a * l.a * centre.pos.y - rad * rad * l.a * l.a;
            y1 = (-v + Math.sqrt(v * v - t * k)) / t;
            y2 = (-v - Math.sqrt(v * v - t * k)) / t;
            x1 = -(l.b * y1 + l.c) / l.a;
            x2 = -(l.b * y2 + l.c) / l.a;
        } else {
            return null;
        }

        // пусть получены две точки пересечения cA, cB
        Vector2d cA = new Vector2d(x1, y1);
        Vector2d cB = new Vector2d(x2, y2);

        Line line = new Line(new Point(cA), new Point(cB));

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