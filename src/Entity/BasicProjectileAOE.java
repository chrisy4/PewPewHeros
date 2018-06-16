package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import RPG.Level;
import RPG.Utils;

public class BasicProjectileAOE extends Projectile {

	private BufferedImage image;

	private Entity source;

	private int life, count;

	public BasicProjectileAOE(Entity source, double targetX, double targetY, double speed, double damage,
			double ccDuration, double penetration, Color c, int x, int y, double distance, Level l) {
		image = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();

		// fills image with transparent pixels
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, x, y);

		g.setColor(c);
		g.fillOval(1, 1, x - 2, y - 2);

		this.setCCDuration(ccDuration).setDamage(damage).setPenetration(penetration).setSpeed(speed).setX(source.getX())
				.setY(source.getY());

		Utils.setDXDY(this, targetX, targetY, this.getSpeed(), l);

		this.source = source;
		this.life = (int) Math.round((distance / speed));
	}

	public BasicProjectileAOE(Entity source, double targetX, double targetY, double speed, double damage,
			double ccDuration, double penetration, int sX, int sY, Color c, int x, int y, double distance, Level l) {
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
		if (this.source.getSide() == Entity.ENEMY) {
			if (this.getX() > l.getPlayer().getX() - l.getPlayer().getImage().getWidth() / 2
					&& this.getX() < l.getPlayer().getX() + l.getPlayer().getImage().getWidth() / 2
					&& this.getY() > l.getPlayer().getY() - l.getPlayer().getImage().getHeight() / 2
					&& this.getY() < l.getPlayer().getY() + l.getPlayer().getImage().getHeight() / 2
					|| (l.getPlayer().getX() > this.getX() - this.getImage().getWidth() / 2
							&& l.getPlayer().getX() < this.getX() + this.getImage().getWidth() / 2
							&& l.getPlayer().getY() > this.getY() - this.getImage().getHeight() / 2
							&& l.getPlayer().getY() < this.getY() + this.getImage().getHeight() / 2)
							&& Utils.isEnemy(source, l.getPlayer())) {
				l.removeEntity(this);
				l.getPlayer().setHealth(l.getPlayer().getHealth() - Utils.calculateDamage(this, l.getPlayer()))
						.ccHit(this.getCCDuration());
				l.addEntity(new DamageDisplayEntity("" + (int) Utils.calculateDamage(this, l.getPlayer()),
						l.getHeight() / 50, this.getX(), this.getY(), Color.RED, Projectile.STRING_TIME, l));
				if (l.getPlayer().getHealth() <= 0) {
					l.getPlayer().killedBy(source);
				}
			}
		} else {
			for (Entity e : l.getEntites()) {
				if (this.getX() > e.getX() - e.getImage().getWidth() / 2
						&& this.getX() < e.getX() + e.getImage().getWidth() / 2
						&& this.getY() > e.getY() - e.getImage().getHeight() / 2
						&& this.getY() < e.getY() + e.getImage().getHeight() / 2 && Utils.isEnemy(source, e)
						|| (e.getX() > this.getX() - this.getImage().getWidth() / 2
								&& e.getX() < this.getX() + this.getImage().getWidth() / 2
								&& e.getY() > this.getY() - this.getImage().getHeight() / 2
								&& e.getY() < this.getY() + this.getImage().getHeight() / 2
								&& Utils.isEnemy(source, e))) {
					e.setHealth(e.getHealth() - Utils.calculateDamage(this, e)).ccHit(this.getCCDuration());
					l.addEntity(new DamageDisplayEntity("" + (int) Utils.calculateDamage(this, e), l.getHeight() / 50,
							e.getX(), e.getY(), Color.RED, Projectile.STRING_TIME, l));
					l.removeEntity(this);
					if (e.getHealth() <= 0) {
						e.killedBy(source);
					}
				}
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
		return Entity.NOTHING_ON_HIT;
	}
}
