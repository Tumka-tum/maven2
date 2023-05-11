import app.Circle;
import app.Line;
import app.Point;
import misc.Vector2d;

public class Test {
    @org.junit.Test
    public void test1() {

        Circle c = new Circle(new Point(new Vector2d(0, 0)), new Point(new Vector2d(1, 1)));
        Line l = new Line(new Point(new Vector2d(3, 0)), new Point(new Vector2d(0, -3)));

        Line r = c.cross(l);

        assert r == null;
    }


    @org.junit.Test
    public void test2() {

        Circle c = new Circle(new Point(new Vector2d(-3, 1)), new Point(new Vector2d(0, 3)));
        Line l = new Line(new Point(new Vector2d(1, 1)), new Point(new Vector2d(2, 0)));

        Line r = c.cross(l);
        System.out.println(r.pointA.pos.x + " " + r.pointA.pos.y + " " + r.pointB.pos.x + " " + r.pointB.pos.x + " ");

        assert r != null;
    }

    @org.junit.Test
    public void test3() {

        Circle c = new Circle(new Point(new Vector2d(1, 2)), new Point(new Vector2d(0, 2)));
        Line l = new Line(new Point(new Vector2d(1, 1)), new Point(new Vector2d(2, 1)));

        Line r = c.cross(l);
        System.out.println(r.pointA.pos.x + " " + r.pointA.pos.y + " " + r.pointB.pos.x + " " + r.pointB.pos.x + " ");

        assert r != null;
    }
}
