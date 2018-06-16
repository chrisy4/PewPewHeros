package Skill.Warlock;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import Entity.DrainProjectile;
import Entity.Player;
import Entity.Projectile;
import RPG.ImageLoader;
import RPG.Level;
import Skill.Skill;

public class Drain extends Skill {

	private static final double ATTACK_SCALE = 7.5, HEAL_PERCENT = 75, SPEED = 6, DISTANCE = 1000, MANA_COST = 20,
			HEALTH_COST_PERCENT = 10, COOLDOWN = 5000;

	private Player p;

	public Drain(Player p, char c) {
		super(c);
		this.p = p;
		this.setManaCost(Drain.MANA_COST).setCooldown(Drain.COOLDOWN);
	}

	@Override
	public double getCdr() {
		return p.getCDR();
	}

	@Override
	public Projectile getProjectile(double x, double y, Level l) {
		return new DrainProjectile(p, x, y, Drain.SPEED, p.getAttack() * Drain.ATTACK_SCALE, 0, p.getPenetration(),
				Color.GRAY, 20, 20, Drain.DISTANCE, Drain.HEAL_PERCENT, l);
	}

	@Override
	public String getInfo() {
		return String.format("Drain: Deals %.1f damage, consuming %.1f health. Heals for %.0f percent of damage dealt.",
				p.getAttack() * Drain.ATTACK_SCALE, Drain.HEALTH_COST_PERCENT * p.getHealth() / 100,
				Drain.HEAL_PERCENT);
	}

	@Override
	public Skill onUse(Level l) {
		l.getPlayer().decreaseHP(Drain.HEALTH_COST_PERCENT * p.getHealth() / 100);
		this.setCount(this.getCooldown());
		return this;
	}

	@Override
	public BufferedImage getImage(int x, int y) {
		BufferedImage image = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();

		g.drawImage(ImageLoader.getSkillImage(ImageLoader.DRAIN), 0, 0, x, y, null);

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
		this.setCount(this.getCount() - l.getRefreshTime() > 0 ? this.getCount() - l.getRefreshTime() : 0);
		return this;
	}
}
