package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import RPG.Level;

public class DamageDisplayEntity extends Entity {

	double time, max;

	private int xSize, ySize;

	private String message;

	private Color c;

	public DamageDisplayEntity(String message, int ySize, double x, double y, Color c, double time, Level l) {

		this.time = time;
		this.setX(x).setY(y);
		this.ySize = ySize;
		this.message = message;
		this.c = c;
		this.max = time;
		Font f =new Font(Font.SERIF, Font.BOLD,
				(int) Math.round((7 * ySize / 8) * Toolkit.getDefaultToolkit().getScreenResolution() / 72.0));
		this.xSize = l.getFontMetrics(f).stringWidth(message) + 4;
	}

	@Override
	public BufferedImage getImage() {
		BufferedImage image;
		image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, (int) xSize, (int) ySize);
		g.setColor(new Color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f,
				(float) ((time / max > 0 ? time / max : 0))));
		g.setFont(new Font(Font.SERIF, Font.BOLD,
				(int) Math.round((7 * ySize / 8) * Toolkit.getDefaultToolkit().getScreenResolution() / 72.0)));
		g.drawString(message, 0, ySize);
		return image;
	}

	@Override
	public int getSide() {
		return Entity.NUETRAL;
	}

	@Override
	public void update(Level l) {
		time -= l.getRefreshTime();
		if (time < 0) {
			l.removeEntity(this);
		}
	}
	
	@Override
	public int getCollision() {
		return Entity.NOTHING_ON_HIT;
	}
	
	@Override
	public double getSpeed() {
		return 0;
	}
}
