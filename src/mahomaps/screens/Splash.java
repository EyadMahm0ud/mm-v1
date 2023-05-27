package mahomaps.screens;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Splash extends Canvas {

	private Image image;

	public Splash() {
		this.setFullScreenMode(true);

		try {
			if (getWidth() < 256 || getHeight() < 240) {
				// 320x240 (horizontal 9.3) must show full image, so "H<240"
				image = Image.createImage("/icon.png");
			} else {
				image = Image.createImage("/splash.png");
			}
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	protected void paint(Graphics g) {
		g.setColor(255, 255, 255);
		g.fillRect(0, 0, getWidth(), getHeight());

		if (this.image != null) {
			g.drawImage(image, getWidth() >> 1, getHeight() >> 1, Graphics.VCENTER | Graphics.HCENTER);
		}
	}
}
