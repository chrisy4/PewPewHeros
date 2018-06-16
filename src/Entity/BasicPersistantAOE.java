package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import RPG.Level;
import RPG.Utils;

public class BasicPersistantAOE extends Projectile {

	private BufferedImage image;

	private Entity source;

	private int life, count, damageCount;

	private double damageTick, ccDamageTick;

	public BasicPersistantAOE(Entity source, double targetX, double targetY, double speed, double damageTick,
			double ccDamageTick, double ccDuration, double penetration, Color c, int x, int y, double distance, Level l,
			int life) {
		image = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();

		// fills image with transparent pixels
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, x, y);

		g.setColor(c);
		g.fillOval(1, 1, x - 2, y - 2);

		this.damageTick = damageTick;
		this.ccDamageTick = ccDamageTick;

		this.setCCDuration(ccDuration).setPenetration(penetration).setSpeed(speed).setX(source.getX())
				.setY(source.getY());

		Utils.setDXDY(this, targetX, targetY, this.getSpeed(), l);

		this.source = source;
		this.life = life / l.getRefreshTime();;
	}

	public BasicPersistantAOE(Entity source, double targetX, double targetY, double speed, double damageTick,
			double ccDamageTick, double ccDuration, double penetration, int sX, int sY, Color c, int x, int y,
			double distance, Level l, int life) {
		image = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();

		// fills image with transparent pixels
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, x, y);

		this.damageTick = damageTick;
		this.ccDamageTick = ccDamageTick;

		g.setColor(c);
		g.fillOval(1, 1, x - 2, y - 2);

		this.setCCDuration(ccDuration).setPenetration(penetration).setSpeed(speed).setX(sX).setY(sY);

		Utils.setDXDY(this, targetX, targetY, this.getSpeed(), l);

		this.source = source;
		this.life = life / l.getRefreshTime();
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
		if (this.damageCount >= 1000) {
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
					this.setDamage(l.getPlayer().canMove() ? damageTick : ccDamageTick);
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
						this.setDamage(e.canMove() ? damageTick : ccDamageTick);
						e.setHealth(e.getHealth() - Utils.calculateDamage(this, e)).ccHit(this.getCCDuration());
						l.addEntity(new DamageDisplayEntity("" + (int) Utils.calculateDamage(this, e),
								l.getHeight() / 50, e.getX(), e.getY(), Color.RED, Projectile.STRING_TIME, l));
						if (e.getHealth() <= 0) {
							e.killedBy(source);
						}
					}
				}
			}
		}
		if (this.count == this.life) {
			l.removeEntity(this);
		} else {
			this.count++;
		}
		if (this.damageCount >= 1000) {
			this.damageCount = l.getRefreshTime();
		} else {
			this.damageCount += l.getRefreshTime();
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
