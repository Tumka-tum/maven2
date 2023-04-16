package app;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import misc.CoordinateSystem2i;

/**
 * Функциональный интерфейс примитива
 */
public interface Primitive {
    /**
     * рисование
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     * @param p        перо
     */
    void render(Canvas canvas, CoordinateSystem2i windowCS, Paint p);
}
