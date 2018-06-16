package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import RPG.Level;
import Skill.Skill;

public class SkillDisplayEntity extends Entity {

	private BufferedImage image, empty;

	private Skill s;

	public SkillDisplayEntity(String message, int ySize, int xSize, double x, double y, Color c, Skill s, Level l) {

		this.s = s;

		Font f = new Font(Font.SERIF, Font.BOLD,
				(int) Math.round((7 * ySize / 8) * Toolkit.getDefaultToolkit().getScreenResolution() / 72.0));

		FontMetrics metrics = l.getFontMetrics(f);

		ArrayList<String> strings = new ArrayList<String>();

		int start = 0;

		for (int i = 0; i < message.length(); i++) {
			if (metrics.stringWidth(message.substring(start, i)) > xSize) {
				strings.add(message.substring(start, i - 1));
				start = i - 1;
			}
		}

		strings.add(message.substring(start));

		empty = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g1 = (Graphics2D) empty.getGraphics();
		g1.setColor(new Color(0f, 0f, 0f, 0f));
		g1.fillRect(0, 0, 1, 1);
		image = new BufferedImage(xSize, ySize * strings.size(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, (int) xSize, (int) ySize);
		g.setColor(c);
		g.setFont(new Font(Font.SERIF, Font.BOLD,
				(int) Math.round((7 * ySize / 8) * Toolkit.getDefaultToolkit().getScreenResolution() / 72.0)));
		for (int i = 0; i < strings.size(); i++) {
			g.drawString(strings.get(i), 0, ySize * (i + 1));
		}

		this.setX(x + xSize / 2 - l.getWidth() / 2 + l.getPlayer().getX())
				.setY(y - (strings.size() * ySize) - l.getHeight() / 2 + l.getPlayer().getY());
	}

	@Override
	public BufferedImage getImage() {
		return s.getDisplay() ? image : empty;
	}

	@Override
	public int getSide() {
		return Entity.NUETRAL;
	}

	@Override
	public void update(Level l) {
		if (!s.getDisplay()) {
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
