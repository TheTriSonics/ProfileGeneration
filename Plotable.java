import java.awt.*;

public abstract class Plotable {
    FigurePanel panel;
    Color color = Color.black;
    public abstract void plot(Graphics2D g);
    public void setFigurePanel(FigurePanel fp) { panel = fp; }
    public void setColor(Color c) {  color = c; }
}
