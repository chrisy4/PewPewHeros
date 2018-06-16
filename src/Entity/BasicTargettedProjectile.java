package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import RPG.Level;
import RPG.Utils;

public class BasicTargettedProjectile extends Projectile {

	private BufferedImage image;

	private Entity target, source;

	private double life, count;

	public BasicTargettedProjectile(Entity target, Entity source, double speed, double damage, double ccDuration,
			double penetration, Color c, int x, int y, double distance) {
		image = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();

		// fills image with transparent pixels
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, x, y);

		g.setColor(c);
		g.fillOval(1, 1, x - 2, y - 2);

		this.setCCDuration(ccDuration).setDamage(damage).setPenetration(penetration).setSpeed(speed).setX(source.getX())
				.setY(source.getY());

		this.source = source;
		this.target = target;
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
		} else if (this.getX() > target.getX() - target.getImage().getWidth() / 2
				&& this.getX() < target.getX() + target.getImage().getWidth() / 2
				&& this.getY() > target.getY() - target.getImage().getHeight() / 2
				&& this.getY() < target.getY() + target.getImage().getHeight() / 2 && Utils.isEnemy(source, target)
				|| (target.getX() > this.getX() - this.getImage().getWidth() / 2
						&& target.getX() < this.getX() + this.getImage().getWidth() / 2
						&& target.getY() > this.getY() - this.getImage().getHeight() / 2
						&& target.getY() < this.getY() + this.getImage().getHeight() / 2
						&& Utils.isEnemy(source, target))) {
			l.removeEntity(this);
			target.setHealth(target.getHealth() - Utils.calculateDamage(this, target)).ccHit(this.getCCDuration());
			l.addEntity(new DamageDisplayEntity("" + (int) Utils.calculateDamage(this, target), l.getHeight() / 50,
					this.getX(), this.getY(), Color.RED, Projectile.STRING_TIME, l));
			if (target.getHealth() <= 0) {
				target.killedBy(source);
			}
		} else {
			double dis = Math.sqrt(Math.pow(target.getX() - this.getX(), 2) + Math.pow(target.getY() - this.getY(), 2));
			this.setDX(this.getSpeed() * (target.getX() - this.getX()) / dis)
					.setDY(this.getSpeed() * (target.getY() - this.getY()) / dis);
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
