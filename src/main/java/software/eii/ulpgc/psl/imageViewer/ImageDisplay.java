package software.eii.ulpgc.psl.imageViewer;

public interface ImageDisplay {
    void paint(String name, int offset);
    int getWidth();
    int getHeight();
    void clear();
    void on(Shift shift);
    void on(Released released);

    interface Shift {
        Shift Null = offset -> {};
        void offset(int offset);
    }
    interface Released {
        Released Null = offset -> {};
        void offset(int offset);
    }
}