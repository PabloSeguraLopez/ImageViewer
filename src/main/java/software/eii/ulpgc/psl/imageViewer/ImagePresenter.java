package software.eii.ulpgc.psl.imageViewer;

import java.util.Timer;
import java.util.TimerTask;

import static software.eii.ulpgc.psl.imageViewer.ImageDisplay.*;

public class ImagePresenter {
    private final ImageDisplay display;
    private Image image;
    private Timer timer;

    public ImagePresenter(ImageDisplay display) {
        this.display = display;
        this.display.on((Shift) this::shift);
        this.display.on((Released) this::released);
    }

    private void shift(int offset) {
        if (timer != null){
            timer.cancel();
            timer = null;
        }
        display.clear();
        display.paint(image.name(), offset);
        if (offset > 0)
            display.paint(image.prev().name(), offset - display.getWidth());
        else
            display.paint(image.next().name(), display.getWidth() + offset);

    }

    private void released(int offset) {
        if (Math.abs(offset) >= display.getWidth() / 2)
            image = offset > 0 ? image.prev() : image.next();
        createTimerForGif();
        repaint();
    }

    private void createTimerForGif() {
        if (timer == null && image.name().endsWith(".gif")){
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    repaint();
                }
            }, 0, 100);
        }
    }

    public void show(Image image) {
        this.image = image;
        createTimerForGif();
        repaint();
    }

    private void repaint() {
        this.display.clear();
        this.display.paint(image.name(), 0);
    }
}
