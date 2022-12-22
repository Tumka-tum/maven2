package app;

import io.github.humbleui.jwm.*;
import io.github.humbleui.jwm.skija.EventFrameSkija;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.RRect;
import io.github.humbleui.skija.Surface;
import misc.Misc;

import java.io.File;
import java.util.function.Consumer;

import static app.Colors.APP_BACKGROUND_COLOR;

/**
 * Класс окна приложения
 */
public class Application implements Consumer<Event> {
    /**
     * окно приложения
     */
    private Window window = null;

    /**
     * Конструктор окна приложения
     */
    public Application() {
        window = App.makeWindow();
        window.setEventListener(this);
        window.setTitle("Java 2D");
        window.setWindowSize(900, 900);
        window.setWindowPosition(100, 100);
        switch (Platform.CURRENT) {
            case WINDOWS -> window.setIcon(new File("src/main/resources/windows.ico"));
            case MACOS -> window.setIcon(new File("src/main/resources/macos.icns"));
        }
        String[] layerNames = new String[]{
                "LayerGLSkija", "LayerRasterSkija"
        };
        for (String layerName : layerNames) {
            String className = "io.github.humbleui.jwm.skija." + layerName;
            try {
                Layer layer = (Layer) Class.forName(className).getDeclaredConstructor().newInstance();
                window.setLayer(layer);
                break;
            } catch (Exception e) {
                System.out.println("Ошибка создания слоя " + className);
            }
        }
        if (window._layer == null)
            throw new RuntimeException("Нет доступных слоёв для создания");
        window.setVisible(true);
    }

    /**
     * Обработчик событий
     *
     * @param e событие
     */
    @Override
    public void accept(Event e) {
        if (e instanceof EventWindowClose) {
            App.terminate();
        } else if (e instanceof EventWindowCloseRequest) {
            window.close();
        } else if (e instanceof EventFrameSkija ee) {
            Surface s = ee.getSurface();
            paint(s.getCanvas(), s.getWidth(), s.getHeight());
        }
    }

    /**
     * Рисование
     *
     * @param canvas низкоуровневый инструмент рисования примитивов от Skija
     * @param height высота окна
     * @param width  ширина окна
     */
    public void paint(Canvas canvas, int height, int width) {
        canvas.save();
        canvas.clear(APP_BACKGROUND_COLOR);

        int rX = width / 3;
        int rY = height / 3;
        int rWidth = width / 3;
        int rHeight = height / 3;
        Paint paint = new Paint();
        paint.setColor(Misc.getColor(100, 255, 255, 255));
        canvas.drawRRect(RRect.makeXYWH(rX, rY, rWidth, rHeight, 4), paint);

        canvas.restore();
    }
}
