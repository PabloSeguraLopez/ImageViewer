package software.eii.ulpgc.psl.imageViewer;

public interface Image {
    String name();
    Image next();
    Image prev();
}
