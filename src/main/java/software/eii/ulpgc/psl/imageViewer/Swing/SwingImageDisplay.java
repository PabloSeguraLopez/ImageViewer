package software.eii.ulpgc.psl.imageViewer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SwingImageDisplay extends JPanel implements ImageDisplay {
    private Shift shift = Shift.Null;
    private Released released = Released.Null;
    private int initShift;
    private List<Paint> paints = new ArrayList<>();

    public SwingImageDisplay() {
        this.addMouseListener(mouseListener());
        this.addMouseMotionListener(mouseMotionListener());
    }

    private MouseListener mouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                initShift = e.getX();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                released.offset(e.getX() - initShift);
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) { }
        };
    }

    private MouseMotionListener mouseMotionListener() {
        return new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                shift.offset(e.getX() - initShift);
            }

            @Override
            public void mouseMoved(MouseEvent e) {}
        };
    }

    @Override
    public void paint(String name, int offset) {
        paints.add(new Paint(name, offset));
        repaint();
    }

    @Override
    public void clear() {
        paints.clear();
    }


    @Override
    public void paint(Graphics g) {
        g.fillRect(0,0,this.getWidth(),this.getHeight());
        g.setColor(Color.BLACK);
        for (Paint paint : paints) {
            java.awt.Image img = load(paint.name());
            Dimension newDimension = resizeDimensions(img);
            int x = paint.offset + (this.getWidth()-newDimension.width)/2;
            int y = (this.getHeight()-newDimension.height)/2;
            int w = (int) newDimension.getWidth();
            int h = (int) newDimension.getHeight();
            g.drawImage(img, x, y, w, h, null);
        }
    }

    private Dimension resizeDimensions(java.awt.Image image) {
        int imgWidth = image.getWidth(null);
        int imgHeight = image.getHeight(null);
        int displayWidth = this.getWidth();
        int displayHeight = this.getHeight();
        if ( 1.0 * imgWidth/displayWidth <= 1 && 1.0 * imgHeight/displayHeight <= 1) {
            return new Dimension(imgWidth, imgHeight);
        }
        double constant;
        if ( 1.0 * imgWidth/displayWidth >= 1.0 * imgHeight/displayHeight) {
            constant = 1.0 * displayWidth/imgWidth;
        }
        else{
            constant = 1.0 * displayHeight/imgHeight;
        }
        return new Dimension((int) (imgWidth*constant), (int) (imgHeight*constant));
    }


    @Override
    public void on(Shift shift) {
        this.shift = shift != null ? shift : Shift.Null;
    }

    @Override
    public void on(Released released) {
        this.released = released != null ? released : Released.Null;
    }

    private record Paint(String name, int offset) {
    }

    private java.awt.Image load(String name) {
        try {
            return ImageIO.read(new File(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
