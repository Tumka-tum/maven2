package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.humbleui.jwm.MouseButton;
import io.github.humbleui.skija.*;
import lombok.Getter;
import misc.CoordinateSystem2d;
import misc.CoordinateSystem2i;
import misc.Vector2d;
import misc.Vector2i;
import panels.PanelLog;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static app.Colors.*;


/**
 * Класс задачи
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class Task {
    /**
     * Текст задачи
     */
    public static final String TASK_TEXT = """
            ПОСТАНОВКА ЗАДАЧИ:
            На плоскости задано множество точек, и множество 
            окружностей. Множество точек образует все возможные 
            прямые, которые могут быть построены парами точек 
            множества. Найти такую прямую и такую окружность, 
            что эта прямая пересекает указанную окружность, и 
            при этом длина отрезка прямой, находящейся внутри 
            окружности, максимальна. В качестве ответа: выделить 
            найденные две точки, выделить найденную окружность, 
            нарисовать прямую, которая через них проходит,
            выделить на этой прямой отрезок между двумя 
            найденными точками пересечения.""";

    /**
     * Вещественная система координат задачи
     */
    @Getter
    private final CoordinateSystem2d ownCS;
    /**
     * Список точек
     */
    @Getter
    private final ArrayList<Point> points;
    /**
     * Список окружностей
     */
    @Getter
    private final ArrayList<Circle> circles;
    /**
     * Список линий
     */
    @Getter
    @JsonIgnore
    private final ArrayList<Line> lines;
    /**
     * коэффициент колёсика мыши
     */
    private static final float WHEEL_SENSITIVE = 0.001f;
    /**
     * Размер точки
     */
    private static final int POINT_SIZE = 3;


    protected CoordinateSystem2i lastWindowCS;


    /**
     * Очистить задачу
     */
    public void clear() {
        points.clear();
        circles.clear();
        solved = false;
    }

    /**
     * Порядок разделителя сетки, т.е. раз в сколько отсечек
     * будет нарисована увеличенная
     */
    private static final int DELIMITER_ORDER = 10;
    /**
     * Список точек в пересечении
     */
    @Getter
    @JsonIgnore
    private final ArrayList<Point> crossed;
    /**
     * Список точек в разности
     */
    @Getter
    @JsonIgnore
    private final ArrayList<Point> single;
    /**
     * Флаг, решена ли задача
     */
    private boolean solved;

    /**
     * Решить задачу
     */
    public void solve() {
        // очищаем списки
        crossed.clear();
        single.clear();

        // перебираем пары точек
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                // сохраняем точки
                Point a = points.get(i);
                Point b = points.get(j);
                // если точки совпадают по положению
                if (a.pos.equals(b.pos)) {
                    if (!crossed.contains(a)) {
                        crossed.add(a);
                        crossed.add(b);
                    }
                }
            }
        }

        /// добавляем вс
        for (Point point : points)
            if (!crossed.contains(point))
                single.add(point);

        // задача решена
        solved = true;
    }

    /**
     * Отмена решения задачи
     */
    public void cancel() {
        solved = false;
    }

    /**
     * проверка, решена ли задача
     *
     * @return флаг
     */
    public boolean isSolved() {
        return solved;
    }

    /**
     * Задача
     *
     * @param ownCS  СК задачи
     * @param points массив точек
     */
    @JsonCreator
    public Task(
            @JsonProperty("ownCS") CoordinateSystem2d ownCS,
            @JsonProperty("points") ArrayList<Point> points,
            @JsonProperty("circles") ArrayList<Circle> circles
    ) {
        this.ownCS = ownCS;
        this.points = points;
        this.circles = circles;

        circles.add(new Circle(
                new Point(new Vector2d(0, 0)),
                new Point(new Vector2d(0, 1))
        ));
        this.crossed = new ArrayList<>();
        this.single = new ArrayList<>();
        this.lines = new ArrayList<>();
    }

    /**
     * Рисование
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    public void paint(Canvas canvas, CoordinateSystem2i windowCS) {
        // Сохраняем последнюю СК
        lastWindowCS = windowCS;
        // рисуем координатную сетку
        renderGrid(canvas, lastWindowCS);
        // рисуем задачу
        renderTask(canvas, windowCS);
    }

    /**
     * Клик мыши по пространству задачи
     *
     * @param pos         положение мыши
     * @param mouseButton кнопка мыши
     */
    public void click(Vector2i pos, MouseButton mouseButton) {
        if (lastWindowCS == null) return;
        // получаем положение на экране
        Vector2d taskPos = ownCS.getCoords(pos, lastWindowCS);
        if (mouseButton.equals(MouseButton.PRIMARY)) {
            addPoint(taskPos);
        }
    }

    /**
     * Рисование задачи
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    private void renderTask(Canvas canvas, CoordinateSystem2i windowCS) {
        canvas.save();
        // создаём перо
        try (var paint = new Paint()) {
            for (Point p : points) {
                if (crossed.contains(p))
                    paint.setColor(CROSSED_COLOR);
                else
                    paint.setColor(SUBTRACTED_COLOR);
                // y-координату разворачиваем, потому что у СК окна ось y направлена вниз,
                // а в классическом представлении - вверх
                Vector2i windowPos = windowCS.getCoords(p.pos.x, p.pos.y, ownCS);
                // рисуем точку
                canvas.drawRect(Rect.makeXYWH(windowPos.x - POINT_SIZE, windowPos.y - POINT_SIZE, POINT_SIZE * 2, POINT_SIZE * 2), paint);
            }
            for (Line l : lines) {
                // опорные точки линии
                Vector2i cA = windowCS.getCoords(l.pointA.pos, ownCS);
                Vector2i cB = windowCS.getCoords(l.pointB.pos, ownCS);
                // рисуем линию
                canvas.drawLine(cA.x, cA.y, cB.x, cB.y, paint);
            }
            for (Circle c : circles) {
                // центр окружности и точка на окружности
                Vector2i centre = windowCS.getCoords(c.centre.pos, ownCS);
                Vector2i circ = windowCS.getCoords(c.circ.pos, ownCS);
                // радиус окружности
                double dx = centre.x - circ.x;
                double dy = centre.y - circ.y;
                double r = Math.sqrt(dx * dx + dy * dy);
                // кол-во отсчётов цикла
                int loopCnt = 40;
                // создаём массив координат опорных точек
                float[] points = new float[loopCnt * 4];
                // запускаем цикл
                for (int i = 0; i < loopCnt; i++) {
                    // x координата первой точки
                    points[i * 4] = (float) (centre.x + r * Math.cos(Math.PI / 20 * i));
                    // y координата первой точки
                    points[i * 4 + 1] = (float) (centre.y + r* Math.sin(Math.PI / 20 * i));

                    // x координата второй точки
                    points[i * 4 + 2] = (float) (centre.x + r * Math.cos(Math.PI / 20 * (i + 1)));
                    // y координата второй точки
                    points[i * 4 + 3] = (float) (centre.y + r * Math.sin(Math.PI / 20 * (i + 1)));
                }
                canvas.drawLines(points, paint);
            }
        }
        canvas.restore();
    }

    /**
     * Рисование сетки
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    public void renderGrid(Canvas canvas, CoordinateSystem2i windowCS) {
        // сохраняем область рисования
        canvas.save();
        // получаем ширину штриха(т.е. по факту толщину линии)
        float strokeWidth = 0.03f / (float) ownCS.getSimilarity(windowCS).y + 0.5f;
        // создаём перо соответствующей толщины
        try (var paint = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(strokeWidth).setColor(TASK_GRID_COLOR)) {
            // перебираем все целочисленные отсчёты нашей СК по оси X
            for (int i = (int) (ownCS.getMin().x); i <= (int) (ownCS.getMax().x); i++) {
                // находим положение этих штрихов на экране
                Vector2i windowPos = windowCS.getCoords(i, 0, ownCS);
                // каждый 10 штрих увеличенного размера
                float strokeHeight = i % DELIMITER_ORDER == 0 ? 5 : 2;
                // рисуем вертикальный штрих
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x, windowPos.y + strokeHeight, paint);
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x, windowPos.y - strokeHeight, paint);
            }
            // перебираем все целочисленные отсчёты нашей СК по оси Y
            for (int i = (int) (ownCS.getMin().y); i <= (int) (ownCS.getMax().y); i++) {
                // находим положение этих штрихов на экране
                Vector2i windowPos = windowCS.getCoords(0, i, ownCS);
                // каждый 10 штрих увеличенного размера
                float strokeHeight = i % 10 == 0 ? 5 : 2;
                // рисуем горизонтальный штрих
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x + strokeHeight, windowPos.y, paint);
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x - strokeHeight, windowPos.y, paint);
            }
        }
        // восстанавливаем область рисования
        canvas.restore();
    }

    /**
     * Добавить точку
     *
     * @param pos положение
     */
    public void addPoint(Vector2d pos) {
        solved = false;
        Point newPoint = new Point(pos);
        points.add(newPoint);
        // Добавляем в лог запись информации
        PanelLog.info("точка " + newPoint + " добавлена");
    }

    /**
     * Добавить случайные точки
     *
     * @param cnt кол-во случайных точек
     */
    public void addRandomPoints(int cnt) {
        // если создавать точки с полностью случайными координатами,
        // то вероятность того, что они совпадут крайне мала
        // поэтому нужно создать вспомогательную малую целочисленную ОСК
        // для получения случайной точки мы будем запрашивать случайную
        // координату этой решётки (их всего 30х30=900).
        // после нам останется только перевести координаты на решётке
        // в координаты СК задачи
        CoordinateSystem2i addGrid = new CoordinateSystem2i(30, 30);

        // повторяем заданное количество раз
        for (int i = 0; i < cnt; i++) {
            // получаем случайные координаты на решётке
            Vector2i gridPos = addGrid.getRandomCoords();
            // получаем координаты в СК задачи
            Vector2d pos = ownCS.getCoords(gridPos, addGrid);
            addPoint(pos);
        }
    }

    /**
     * Добавить окружность
     */
    public void addCircle(double x1, double y1, double x2, double y2) {
        Vector2d pointCentre = new Vector2d(x1,y1);
        Vector2d pointCirc = new Vector2d(x2,y2);
        circles.add(new Circle(new Point(pointCentre), new Point(pointCirc)));
    }

    /**
     * Добавить случайную окружность
     *
     * @param cnt кол-во случайных окружностей
     */
    public void addRandomCircle(int cnt) {
        // повторяем заданное количество раз
        for (int i = 0; i < cnt; i++) {
            solved = false;
            Vector2d pointA = ownCS.getRandomCoords();
            Vector2d pointB = ownCS.getRandomCoords();
            circles.add(new Circle(new Point(pointA), new Point(pointB)));
        }
    }

    /**
     * Масштабирование области просмотра задачи
     *
     * @param delta  прокрутка колеса
     * @param center центр масштабирования
     */
    public void scale(float delta, Vector2i center) {
        if (lastWindowCS == null) return;
        // получаем координаты центра масштабирования в СК задачи
        Vector2d realCenter = ownCS.getCoords(center, lastWindowCS);
        // выполняем масштабирование
        ownCS.scale(1 + delta * WHEEL_SENSITIVE, realCenter);
    }

    /**
     * Получить положение курсора мыши в СК задачи
     *
     * @param x        координата X курсора
     * @param y        координата Y курсора
     * @param windowCS СК окна
     * @return вещественный вектор положения в СК задачи
     */
    @JsonIgnore
    public Vector2d getRealPos(int x, int y, CoordinateSystem2i windowCS) {
        return ownCS.getCoords(x, y, windowCS);
    }

    /**
     * Рисование курсора мыши
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     * @param font     шрифт
     * @param pos      положение курсора мыши
     */
    public void paintMouse(Canvas canvas, CoordinateSystem2i windowCS, Font font, Vector2i pos) {
        // создаём перо
        try (var paint = new Paint().setColor(TASK_GRID_COLOR)) {
            // сохраняем область рисования
            canvas.save();
            // рисуем перекрестие
            canvas.drawRect(Rect.makeXYWH(0, pos.y - 1, windowCS.getSize().x, 2), paint);
            canvas.drawRect(Rect.makeXYWH(pos.x - 1, 0, 2, windowCS.getSize().y), paint);
            // смещаемся немного для красивого вывода текста
            canvas.translate(pos.x + 3, pos.y - 5);
            // положение курсора в пространстве задачи
            Vector2d realPos = getRealPos(pos.x, pos.y, lastWindowCS);
            // выводим координаты
            canvas.drawString(realPos.toString(), 0, 0, font, paint);
            // восстанавливаем область рисования
            canvas.restore();
        }
    }
}