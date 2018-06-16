package Skill;

import java.awt.image.BufferedImage;

import Entity.Projectile;
import RPG.Level;

public abstract class Skill {

	private double manaCost, cooldown, cdr, count;
	
	private boolean display;
	
	private char key;

	public Skill(char key) {
		this.key = key;
	}

	public abstract String getInfo();

	public abstract Skill onUse(Level l);

	public abstract Projectile getProjectile(double x, double y, Level l);

	public abstract BufferedImage getImage(int x, int y);

	public abstract Skill update(Level l);

	public double getManaCost() {
		return manaCost;
	}

	public Skill setManaCost(double manaCost) {
		this.manaCost = manaCost;
		return this;
	}

	public double getCooldown() {
		return cooldown * (100 - this.getCdr()) / 100;
	}

	public Skill setCooldown(double cooldown) {
		this.cooldown = cooldown;
		return this;
	}

	public double getCdr() {
		return cdr;
	}

	public Skill setCdr(double cdr) {
		this.cdr = cdr;
		return this;
	}

	public char getKey() {
		return key;
	}

	public Skill setKey(char key) {
		this.key = key;
		return this;
	}

	public double getCount() {
		return count;
	}

	public Skill setCount(double count) {
		this.count = count;
		return this;
	}
	
	public boolean getDisplay() {
		return this.display;
	}
	
	public Skill setDisplay(boolean display) {
		this.display = display;
		return this;
	}
}
