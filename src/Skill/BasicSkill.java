package Skill;

import java.awt.Color;
import java.awt.image.BufferedImage;

import Entity.BasicProjectile;
import Entity.Player;
import Entity.Projectile;
import RPG.Level;

public class BasicSkill extends Skill {

	private Player p;

	private boolean toggle = false;

	public BasicSkill(Player p, char c) {
		super(c);
		this.p = p;
		this.setCooldown(125).setManaCost(-1);
	}

	@Override
	public double getCdr() {
		return p.getCDR();
	}

	@Override
	public String getInfo() {
		return null;
	}

	@Override
	public Skill onUse(Level l) {
		this.setCount(this.getCooldown());
		return this;
	}

	@Override
	public Projectile getProjectile(double x, double y, Level l) {
		return new BasicProjectile(p, x, y, Player.BASIC_ATTACK_SPEED, p.getAttack(), 0, p.getPenetration(), Color.BLUE,
				20, 20, Player.BASIC_ATTACK_DISTANCE, l);
	}

	@Override
	public BufferedImage getImage(int x, int y) {
		return null;
	}

	@Override
	public Skill update(Level l) {
		this.setCount(this.getCount() - l.getRefreshTime() > 0 ? this.getCount() - l.getRefreshTime() : 0);
		if (toggle && this.getManaCost() < l.getPlayer().getMana() && this.getCount() == 0) {
			l.addEntity(this.onUse(l).getProjectile(l.getMouse().x, l.getMouse().y, l));
			l.getPlayer().decreaseMana(this.getManaCost());
		}
		return this;
	}

	public BasicSkill toggle() {
		toggle = !toggle;
		return this;
	}
}
