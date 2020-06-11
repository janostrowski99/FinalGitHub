package poker;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;


public class ImagePanel extends JPanel { //panel z grafik�

	private static final long serialVersionUID = 1L;
	BufferedImage image;
	public ImagePanel(BufferedImage a) {
		super();
		image = a;
	}
	
	public void paint(Graphics g) {
      g.drawImage(image, 0, 0, this);
    }
}	

