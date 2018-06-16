package Skill.Warlock;

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

public class DemonicOffering extends Skill {

	private static final double ATTACK_BOOST_PERCENT = 45, MANA_COST = 0, HEALTH_COST_PERCENT = 15, COOLDOWN = 15000,
			BOOST_DURATION = 5000;

	private Player p;

	private double boostCount;

	private boolean removed = true;

	public DemonicOffering(Player p, char c) {
		super(c);
		this.p = p;
		this.setManaCost(DemonicOffering.MANA_COST).setCooldown(DemonicOffering.COOLDOWN);
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
		return String.format(
				"Demonic Offering: Consumes %.1f health, and grants a %.0f percent attack boost for %.1f seconds.",
				DemonicOffering.HEALTH_COST_PERCENT * p.getHealth() / 100, DemonicOffering.ATTACK_BOOST_PERCENT,
				DemonicOffering.BOOST_DURATION / 1000);
	}

	@Override
	public Skill onUse(Level l) {
		l.getPlayer().decreaseHP(DemonicOffering.HEALTH_COST_PERCENT * p.getHealth() / 100);
		this.setCount(this.getCooldown());
		boostCount = BOOST_DURATION;
		l.getPlayer().giveAttackBoostPercent(ATTACK_BOOST_PERCENT);
		removed = false;
		return this;
	}

	@Override
	public BufferedImage getImage(int x, int y) {
		BufferedImage image = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();

		g.drawImage(ImageLoader.getSkillImage(ImageLoader.DEMONIC_OFFERING), 0, 0, x, y, null);

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
		boostCount = boostCount - l.getRefreshTime() > 0 ? boostCount - l.getRefreshTime() : 0;
		if (boostCount == 0 && !removed) {
			removed = true;
			l.getPlayer().reduceAttackBoostPercent(ATTACK_BOOST_PERCENT);
		}
		return this;
	}
}
