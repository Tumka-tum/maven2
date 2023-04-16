package panels;

import app.Primitive;
import io.github.humbleui.jwm.Event;
import io.github.humbleui.jwm.EventKey;
import io.github.humbleui.jwm.Window;
import io.github.humbleui.skija.*;
import misc.CoordinateSystem2i;
import misc.Misc;
import misc.Vector2d;
import misc.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Панель игры
 */
public class PanelPrimitives extends Panel {
    /**
     * Список примитивов
     */
    private final ArrayList<Primitive> primitives = new ArrayList<>();
    /**
     * Положение текущего примитива
     */
    private int primitivePos;

    /**
     * Конструктор панели
     *
     * @param window          окно
     * @param drawBG          нужно ли рисовать подложку
     * @param backgroundColor цвет фона
     * @param padding         отступы
     */
    public PanelPrimitives(Window window, boolean drawBG, int backgroundColor, int padding) {
        super(window, drawBG, backgroundColor, padding);
        // добавляем окружность
        primitives.add((canvas, windowCS, p) -> {
            // центр окружности
            Vector2i center = new Vector2i(350, 350);
            // радиус окружности
            int rad = 200;
            // радиус вдоль оси x
            int radX = (int) (rad * 1.3);
            // радиус вдоль оси y
            int radY = (int) (rad * 0.9);
            // кол-во отсчётов цикла
            int loopCnt = 40;
            // создаём массив координат опорных точек
            float[] points = new float[loopCnt * 4];
            // запускаем цикл
            for (int i = 0; i < loopCnt; i++) {
                // x координата первой точки
                points[i * 4] = (float) (center.x + radX * Math.cos(Math.PI / 20 * i));
                // y координата первой точки
                points[i * 4 + 1] = (float) (center.y + radY * Math.sin(Math.PI / 20 * i));

                // x координата второй точки
                points[i * 4 + 2] = (float) (center.x + radX * Math.cos(Math.PI / 20 * (i + 1)));
                // y координата второй точки
                points[i * 4 + 3] = (float) (center.y + radY * Math.sin(Math.PI / 20 * (i + 1)));
            }
            // рисуем линии
            canvas.drawLines(points, p);
        });

        primitivePos = primitives.size() - 1;
    }

    /**
     * Обработчик событий
     *
     * @param e событие
     */
    @Override
    public void accept(Event e) {
        // кнопки клавиатуры
        if (e instanceof EventKey eventKey) {
            // кнопка нажата с Ctrl
            if (eventKey.isPressed()) {
                switch (eventKey.getKey()) {
                    // Следующий примитив
                    case LEFT -> primitivePos = (primitivePos - 1 + primitives.size()) % primitives.size();
                    // Предыдущий примитив
                    case RIGHT -> primitivePos = (primitivePos + 1) % primitives.size();
                }
            }
        }
    }


    /**
     * Метод под рисование в конкретной реализации
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    @Override
    public void paintImpl(Canvas canvas, CoordinateSystem2i windowCS) {
        // создаём перо
        Paint p = new Paint();
        // задаём цвет
        p.setColor(Misc.getColor(200, 255, 255, 255));
        // задаём толщину пера
        p.setStrokeWidth(5);
        // рисуем текущий примитив
        primitives.get(primitivePos).render(canvas, windowCS, p);
    }

}
