package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import RPG.Level;
import RPG.Utils;

public class DrainProjectile extends Projectile {

	private BufferedImage image;

	private Entity source;

	private int life, count;

	private double drainPercent;

	public DrainProjectile(Entity source, double targetX, double targetY, double speed, double damage,
			double ccDuration, double penetration, Color c, int x, int y, double distance, double drainPercent,
			Level l) {
		image = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();

		// fills image with transparent pixels
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, x, y);

		g.setColor(c);
		g.fillOval(1, 1, x - 2, y - 2);

		this.setCCDuration(ccDuration).setDamage(damage).setPenetration(penetration).setSpeed(speed)
				.setX(source.getX() + source.getImage().getWidth() / 2)
				.setY(source.getY() + source.getImage().getHeight() / 2);

		Utils.setDXDY(this, targetX, targetY, this.getSpeed(), l);

		this.source = source;
		this.life = (int) Math.round((distance / speed));
		this.drainPercent = drainPercent;
	}

	public DrainProjectile(Entity source, double targetX, double targetY, double speed, double damage,
			double ccDuration, double penetration, int sX, int sY, Color c, int x, int y, double distance,
			double drainPercent, Level l) {
		image = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();

		// fills image with transparent pixels
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, x, y);

		g.setColor(c);
		g.fillOval(1, 1, x - 2, y - 2);

		this.setCCDuration(ccDuration).setDamage(damage).setPenetration(penetration).setSpeed(speed).setX(sX).setY(sY);

		Utils.setDXDY(this, targetX, targetY, this.getSpeed(), l);

		this.source = source;
		this.life = (int) Math.round((distance / speed));
		this.drainPercent = drainPercent;
	}

	@Override
	public BufferedImage getImage() {
		return image;
	}

	@Override
	public int getSide() {
		return Entity.NUETRAL;
	}

	@Override
	public void update(Level l) {
		for (Entity e : l.getEntites()) {
			if ((this.getX() > e.getX() - e.getImage().getWidth() / 2
					&& this.getX() < e.getX() + e.getImage().getWidth() / 2)
					&& this.getY() > e.getY() - e.getImage().getHeight() / 2
					&& this.getY() < e.getY() + e.getImage().getHeight() / 2 && Utils.isEnemy(source, e)) {
				l.addEntity(new DamageDisplayEntity("" + (int) Utils.calculateDamage(this, e), l.getHeight() / 50,
						this.getX(), this.getY(), Color.RED, Projectile.STRING_TIME, l));
				l.removeEntity(this);
				e.setHealth(e.getHealth() - Utils.calculateDamage(this, e));
				if (e.getHealth() <= 0) {
					e.killedBy(source);
				}
				source.setHealth(source.getHealth() + (source.getHealth() < source.getMaxHealth()
						? drainPercent * Utils.calculateDamage(this, e) / 100
						: 0));
				break;
			}
		}
		if (this.count == this.life) {
			l.removeEntity(this);
		} else {
			this.count++;
		}
	}

	@Override
	public Entity ccHit(double ccDuration) {
		return this;
	}
	
	@Override
	public int getCollision() {
		return Entity.REMOVE_ON_HIT;
	}
}