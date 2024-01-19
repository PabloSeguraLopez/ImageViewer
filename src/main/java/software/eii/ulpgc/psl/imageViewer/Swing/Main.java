package software.eii.ulpgc.psl.imageViewer;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        ImagePresenter presenter = new ImagePresenter(frame.getImageDisplay());
        presenter.show(image());
        frame.setVisible(true);
    }

    private static Image image() {
        return new FileImageLoader(new File("src/images")).load();
    }
}
