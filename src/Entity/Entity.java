package Entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import RPG.Level;

public abstract class Entity {

	public static final int PLAYER = 0, NUETRAL = 1, ENEMY = 2;

	public static final int REMOVE_ON_HIT = 0, STOP_ON_HIT = 1, NOTHING_ON_HIT = 2, PATHING_NOTHING_ON_HIT = 3;

	private double x, y, dx, dy, health, mana, maxMana, defense, maxHealth, ccDuration;

	private int level;

	private Entity killedBy;

	private boolean collided, moveStop;
	
	private Rectangle collide;

	public abstract BufferedImage getImage();

	public abstract int getSide();

	public abstract void update(Level l);

	public Entity ccHit(double ccDuration) {
		this.ccDuration += ccDuration;
		return this;
	}

	public boolean canMove() {
		return this.ccDuration <= 0;
	}

	public Entity reduceCCDuration(double time) {
		this.ccDuration = this.ccDuration - time > 0 ? this.ccDuration - time : 0;
		return this;
	}

	public double getMaxHealth() {
		return maxHealth;
	}

	public Entity setMaxHealth(double maxHealth) {
		this.maxHealth = maxHealth;
		return this;
	}

	public double getDefense() {
		return defense;
	}

	public Entity setDefense(double defense) {
		this.defense = defense;
		return this;
	}

	public double getMaxMana() {
		return maxMana;
	}

	public Entity setMaxMana(double maxMana) {
		this.maxMana = maxMana;
		return this;
	}

	public double getHealth() {
		return health;
	}

	public Entity setHealth(double health) {
		this.health = health;
		return this;
	}

	public double getMana() {
		return mana;
	}

	public Entity setMana(double mana) {
		this.mana = mana;
		return this;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getDY() {
		return dy;
	}

	public double getDX() {
		return dx;
	}

	public Entity setX(double x) {
		this.x = x;
		return this;
	}

	public Entity setY(double y) {
		this.y = y;
		return this;
	}

	public Entity setDX(double dx) {
		this.dx = dx;
		return this;
	}

	public Entity setDY(double dy) {
		this.dy = dy;
		return this;
	}

	public Entity killedBy(Entity e) {
		this.killedBy = e;
		return this;
	}

	public Entity getKilledBy() {
		return this.killedBy;
	}

	public int getLevel() {
		return level;
	}

	public Entity setLevel(int level) {
		this.level = level;
		return this;
	}

	public abstract int getCollision();

	public boolean getCollided() {
		return collided;
	}

	public Entity setCollided(boolean collided, Rectangle r) {
		this.collided = collided;
		this.collide = r;
		return this;
	}
	
	public Rectangle getCollide() {
		return collide;
	}
	
	public Entity setMoveStop(boolean moveStop) {
		this.moveStop = moveStop;
		return this;
	}
	
	public boolean moveStop() {
		return moveStop;
	}
	
	public abstract double getSpeed();
}
