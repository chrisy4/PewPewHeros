package Entity;

public abstract class Projectile extends Entity {

	public static final double STRING_TIME = 400;
	
	private double damage, penetration, ccDuration, speed;

	public double getCCDuration() {
		return ccDuration;
	}

	public Projectile setCCDuration(double ccDuration) {
		this.ccDuration = ccDuration;
		return this;
	}

	public double getPenetration() {
		return penetration;
	}

	public Projectile setPenetration(double penetration) {
		this.penetration = penetration;
		return this;
	}

	public double getDamage() {
		return damage;
	}

	public Projectile setDamage(double damage) {
		this.damage = damage;
		return this;
	}

	public double getSpeed() {
		return this.speed;
	}

	public Projectile setSpeed(double speed) {
		this.speed = speed;
		return this;
	}
}
