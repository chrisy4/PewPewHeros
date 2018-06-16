package Skill;

import Entity.Projectile;
import RPG.Level;

public abstract class EnemySkill {

	private double manaCost, cooldown, cdr, count;

	public double getCount() {
		return count;
	}

	public EnemySkill setCount(double count) {
		this.count = count;
		return this;
	}

	public abstract EnemySkill update(Level l);

	public double getManaCost() {
		return manaCost;
	}

	public abstract Projectile getProjectile(double x, double y, Level l);

	public EnemySkill setManaCost(double manaCost) {
		this.manaCost = manaCost;
		return this;
	}

	public double getCooldown() {
		return cooldown * (100 - this.getCdr()) / 100;
	}

	public EnemySkill setCooldown(double cooldown) {
		this.cooldown = cooldown;
		return this;
	}

	public double getCdr() {
		return cdr;
	}

	public EnemySkill setCdr(double cdr) {
		this.cdr = cdr;
		return this;
	}

	public abstract EnemySkill onUse(Level l);
}
