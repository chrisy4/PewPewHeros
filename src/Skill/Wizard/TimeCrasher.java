package Skill.Wizard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import Entity.Player;
import Entity.Projectile;
import RPG.ImageLoader;
import RPG.Level;
import Skill.Skill;

public class TimeCrasher extends Skill {

	private static final double MANA_COST = 60, COOLDOWN = 45000, COOLDOWN_RECUTION_FLAT = 5000, SPEED_DURATION = 5000,
			SPEED_PERCENT = 25;

	private Player p;

	private double boostCount;

	private boolean removed = true;

	public TimeCrasher(Player p, char c) {
		super(c);
		this.p = p;
		this.setManaCost(TimeCrasher.MANA_COST).setCooldown(TimeCrasher.COOLDOWN);
	}

	@Override
	public double getCdr() {
		return p.getCDR();
	}

	@Override
	public Projectile getProjectile(double x, double y, Level l) {
		return null;
	}

	@Override
	public String getInfo() {
		return String
				.format("Time Crasher: Reduces all other cooldowns by 5 seconds and increases movement speed by 25 percent.");
	}

	@Override
	public Skill onUse(Level l) {
		for (Skill s : l.getPlayer().getSkills()) {
			if (s != this) {
				s.setCount(s.getCount() - TimeCrasher.COOLDOWN_RECUTION_FLAT > 0
						? s.getCount() - TimeCrasher.COOLDOWN_RECUTION_FLAT
						: 0);
			}
		}
		l.getPlayer().increaseSpeedPercent(TimeCrasher.SPEED_PERCENT);
		this.setCount(this.getCooldown());
		boostCount = TimeCrasher.SPEED_DURATION;
		removed = false;
		return this;
	}

	@Override
	public BufferedImage getImage(int x, int y) {
		BufferedImage image = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();

		g.drawImage(ImageLoader.getSkillImage(ImageLoader.TimeCrasher), 0, 0, x, y, null);

		// Semi-transparent grey
		g.setColor(new Color(105f / 255, 105f / 255, 105f / 255, 0.4f));

		g.fillRect(0, 0, x, (int) Math.round(y * this.getCount() / this.getCooldown()));

		g.setColor(new Color(0f / 255, 0f / 0, 105f / 255, 0.9f));

		g.setFont(new Font(Font.SERIF, Font.ITALIC,
				(int) Math.round((y / 4 - 2) * Toolkit.getDefaultToolkit().getScreenResolution() / 72.0)));

		if (this.getCount() != 0)
			g.drawString(String.format("%.1f", this.getCount() / 1000),
					x / 2 - g.getFontMetrics().stringWidth(String.format("%.1f", this.getCount() / 1000)) / 2,
					2 * y / 3 - g.getFontMetrics().getHeight() / 2);

		g.drawString(this.getKey() + "", x / 2 - g.getFontMetrics().stringWidth(this.getKey() + "") / 2,
				g.getFontMetrics().getHeight() / 2);

		g.drawString(String.format("%.1f", this.getManaCost()),
				x / 2 - g.getFontMetrics().stringWidth(String.format("%.1f", this.getManaCost())),
				2 * y / 3 + g.getFontMetrics().getHeight() / 2);

		return image;
	}

	@Override
	public Skill update(Level l) {
		boostCount = boostCount - l.getRefreshTime() > 0 ? boostCount - l.getRefreshTime() : 0;
		if (boostCount == 0 && !removed) {
			removed = true;
			l.getPlayer().decreaseSpeedPercent(TimeCrasher.SPEED_PERCENT);
		}
		this.setCount(this.getCount() - l.getRefreshTime() > 0 ? this.getCount() - l.getRefreshTime() : 0);
		return this;
	}
}