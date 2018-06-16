package Skill.Gunslinger;

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
import RPG.Utils;
import Skill.Skill;

public class GroundPound extends Skill {

	private static final double MANA_COST = 10, COOLDOWN = 8000, ATTACK_RATIO = 8, DISTANCE = 600, RADIUS = 150,
			FLY_TIME = 250;

	private Player p;

	private double flying = 0;

	private boolean done = true;

	public GroundPound(Player p, char c) {
		super(c);
		this.p = p;
		this.setManaCost(GroundPound.MANA_COST).setCooldown(GroundPound.COOLDOWN);
	}

	@Override
	public double getCdr() {
		return p.getCDR();
	}

	@Override
	public Projectile getProjectile(double x, double y, Level l) {
		if (Utils.getDistance(l.getPlayer(), x, y) <= DISTANCE) {
			Utils.setDXDY(l.getPlayer(), x, y, Utils.getDistance(p, x, y) / (this.flying / l.getRefreshTime()), l);
		} else {
			Utils.setDXDY(p, x, y, DISTANCE / l.getRefreshTime(), l);
		}
		return null;
	}

	@Override
	public String getInfo() {
		return String.format(
				"Ground Pound: Teleports the player %.1f units in the direction of cursor, dealing %.1f damage to enemies around where the player lands",
				GroundPound.DISTANCE, GroundPound.ATTACK_RATIO * p.getAttack());
	}

	@Override
	public Skill onUse(Level l) {
		this.setCount(this.getCooldown());
		this.flying = FLY_TIME;
		l.getPlayer().setMoveStop(true);
		done = false;
		return this;
	}

	@Override
	public BufferedImage getImage(int x, int y) {
		BufferedImage image = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();

		g.drawImage(ImageLoader.getSkillImage(ImageLoader.GROUND_POUND), 0, 0, x, y, null);

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
		if (flying <= 0 && !done) {
			p.setDX(0).setDY(0);
			if(l.getPlayer().getCollided()) {
				Utils.moveOut(l.getPlayer().getCollide(), l.getPlayer());
				l.getPlayer().setCollided(false, null);
			}
			l.addEntity(new BasicProjectileAOE(p, l.getPlayer().getX(), l.getPlayer().getY(), 0,
					p.getAttack() * ATTACK_RATIO, 0, p.getPenetration(),
					new Color(105 / 255, 105 / 255, 105 / 255, 0.1f), (int) RADIUS, (int) RADIUS, 0, l));
			done = true;
			l.getPlayer().setMoveStop(false);
		} else {
			flying -= l.getRefreshTime();
		}
		this.setCount(this.getCount() - l.getRefreshTime() > 0 ? this.getCount() - l.getRefreshTime() : 0);
		return this;
	}
}
