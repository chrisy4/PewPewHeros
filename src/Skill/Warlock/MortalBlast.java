package Skill.Warlock;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import Entity.BasicProjectileAOE;
import Entity.Player;
import Entity.Projectile;
import RPG.ImageLoader;
import RPG.Level;
import Skill.Skill;

public class MortalBlast extends Skill {

	private static final double ATTACK_SCALE = 6.5, SPEED = 0, DISTANCE = 0, MANA_COST = 30, HEALTH_COST_PERCENT = 10,
			COOLDOWN = 15000, RADIUS = 600;

	private Player p;

	public MortalBlast(Player p, char c) {
		super(c);
		this.p = p;
		this.setManaCost(MortalBlast.MANA_COST).setCooldown(MortalBlast.COOLDOWN);
	}

	@Override
	public double getCdr() {
		return p.getCDR();
	}

	@Override
	public Projectile getProjectile(double x, double y, Level l) {
		return new BasicProjectileAOE(p, l.getPlayer().getX(), l.getPlayer().getY(), MortalBlast.SPEED,
				p.getAttack() * MortalBlast.ATTACK_SCALE, 0, p.getPenetration(), Color.GRAY, (int) RADIUS, (int) RADIUS,
				MortalBlast.DISTANCE, l);
	}

	@Override
	public String getInfo() {
		return String.format("Mortal Blast: Deals %.1f damage in a massive AOE, consuming %.1f health.",
				p.getAttack() * MortalBlast.ATTACK_SCALE, MortalBlast.HEALTH_COST_PERCENT * p.getHealth() / 100);
	}

	@Override
	public Skill onUse(Level l) {
		l.getPlayer().decreaseHP(MortalBlast.HEALTH_COST_PERCENT * p.getHealth() / 100);
		this.setCount(this.getCooldown());
		return this;
	}

	@Override
	public BufferedImage getImage(int x, int y) {
		BufferedImage image = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();

		g.drawImage(ImageLoader.getSkillImage(ImageLoader.MORTAL_BLAST), 0, 0, x, y, null);

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
