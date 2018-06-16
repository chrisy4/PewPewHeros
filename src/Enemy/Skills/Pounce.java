package Enemy.Skills;

import java.awt.Color;

import Enemy.PounceEnemy;
import Entity.BasicProjectileAOE;
import Entity.Projectile;
import RPG.Level;
import RPG.Utils;
import Skill.EnemySkill;

public class Pounce extends EnemySkill {

	private static final double MANA_COST = 10, COOLDOWN = 9000, ATTACK_RATIO = 1.75, DISTANCE = 500, RADIUS = 115,
			FLY_TIME = 250;

	private PounceEnemy p;

	private double flying = 0;

	private boolean done = true;

	public Pounce(PounceEnemy p) {
		this.p = p;
		this.setManaCost(Pounce.MANA_COST).setCooldown(Pounce.COOLDOWN).setCount(Pounce.COOLDOWN/2);
	}

	@Override
	public double getCdr() {
		return p.getCDR();
	}

	@Override
	public Projectile getProjectile(double x, double y, Level l) {
		if (Utils.getDistance(p, x, y) <= DISTANCE) {
			Utils.setDXDY(p, x, y, Utils.getDistance(p, x, y) / (this.flying / l.getRefreshTime()), l);
		} else {
			Utils.setDXDY(p, x, y, DISTANCE / l.getRefreshTime(), l);
		}
		return null;
	}

	@Override
	public EnemySkill onUse(Level l) {
		this.setCount(this.getCooldown());
		this.flying = FLY_TIME;
		p.setMoveStop(true);
		done = false;
		return this;
	}

	@Override
	public EnemySkill update(Level l) {
		if (flying <= 0 && !done) {
			p.setDX(0).setDY(0);
			if(p.getCollided()) {
				Utils.moveOut(p.getCollide(), p);
				p.setCollided(false, null);
			}
			l.addEntity(new BasicProjectileAOE(p, p.getX(), p.getY(), 0, p.getAttack() * ATTACK_RATIO, 0,
					p.getPenetration(), new Color(105 / 255, 105 / 255, 105 / 255, 0.1f), (int) RADIUS, (int) RADIUS, 0,
					l));
			done = true;
			p.setMoveStop(false);
		} else {
			flying -= l.getRefreshTime();
		}
		this.setCount(this.getCount() - l.getRefreshTime() > 0 ? this.getCount() - l.getRefreshTime() : 0);
		return this;
	}
}
