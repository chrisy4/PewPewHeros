package RPG;

import java.awt.Rectangle;

import Entity.Entity;
import Entity.Projectile;

public class Utils {
	public static double calculateDamage(Projectile p, Entity e) {
		return (p.getDamage() * p.getPenetration() / 100)
				+ (p.getDamage() * ((100 - p.getPenetration()) / 100)) * ((100 - e.getDefense()) / 100) > 0
						? (p.getDamage() * p.getPenetration() / 100)
								+ (p.getDamage() * ((100 - p.getPenetration()) / 100)) * ((100 - e.getDefense()) / 100)
						: 0;
	}

	public static double calculateXP(int playerLevel, int enemyLevel, double xp) {
		return xp - ((playerLevel - enemyLevel) * xp / 10) > 0 ? xp - ((playerLevel - enemyLevel) * xp / 10) : 0;
	}

	public static boolean isEnemy(Entity e1, Entity e2) {
		return e1.getSide() == Entity.ENEMY && e2.getSide() == Entity.PLAYER
				|| e1.getSide() == Entity.PLAYER && e2.getSide() == Entity.ENEMY;
	}

	public static double getDistance(Entity e1, Entity e2) {
		return Math.sqrt(Math.pow(e1.getX() - e2.getX(), 2) + Math.pow(e1.getY() - e2.getY(), 2));
	}

	public static double getDistance(Entity e1, double targetX, double targetY) {
		return Math.sqrt(Math.pow(targetX - e1.getX(), 2) + Math.pow(targetY - e1.getY(), 2));
	}

	public static void move(Entity e, double targetX, double targetY, double distance) {
		if (distance > 0) {
			e.setX(e.getX() + ((targetX - e.getX()) * (distance / Utils.getDistance(e, targetX, targetY))))
					.setY(e.getY() + ((targetY - e.getY()) * (distance / Utils.getDistance(e, targetX, targetY))));
		}
	}

	public static void setDXDY(Entity e1, double targetX, double targetY, double speed, Level l) {
		e1.setDX(targetX == e1.getX() ? 0
				: (20 / l.getRefreshTime()) * speed * (targetX - e1.getX()) / Utils.getDistance(e1, targetX, targetY))
				.setDY(targetY == e1.getY() ? 0
						: (20 / l.getRefreshTime()) * speed * (targetY - e1.getY())
								/ Utils.getDistance(e1, targetX, targetY));
	}

	public static double calcItemValue(int level, double baseMax, double growthMax) {
		return Math.random() * level * growthMax + Math.random() * baseMax;
	}

	public static void moveOut(Rectangle r, Entity e) {
		/*
		 * Left Math.abs(r.getX() - e.getX() - e.getImage().getWidth() / 2); Right
		 * Math.abs(r.getX() + r.getWidth() - e.getX() + e.getImage().getWidth() / 2);
		 * Top Math.abs(r.getY() - e.getY() - e.getImage().getHeight() / 2); Bottom
		 * Math.abs(r.getY() + r.getHeight() - e.getY() + e.getImage().getHeight() / 2);
		 */
		if (Math.abs(r.getX() - e.getX()) <= Math.abs(r.getX() + r.getWidth() - e.getX())
				&& Math.abs(r.getX() - e.getX()) <= Math.abs(r.getY() - e.getY())
				&& Math.abs(r.getX() - e.getX()) <= Math.abs(r.getY() + r.getHeight() - e.getY())) {
			e.setX(r.getX() - e.getImage().getWidth() / 2);
		} else if (Math.abs(r.getX() + r.getWidth() - e.getX()) <= Math.abs(r.getX() - e.getX())
				&& Math.abs(r.getX() + r.getWidth() - e.getX()) <= Math.abs(r.getY() - e.getY())
				&& Math.abs(r.getX() + r.getWidth() - e.getX()) <= Math.abs(r.getY() + r.getHeight() - e.getY())) {
			e.setX(r.getX() + r.getWidth() + e.getImage().getWidth() / 2);
		} else if (Math.abs(r.getY() - e.getY()) <= Math.abs(r.getX() - e.getX())
				&& Math.abs(r.getY() - e.getY()) <= Math.abs(r.getX() + r.getWidth() - e.getX())
				&& Math.abs(r.getY() - e.getY()) <= Math.abs(r.getY() + r.getHeight() - e.getY())) {
			e.setY(r.getY() - e.getImage().getHeight() / 2);
		} else {
			e.setY(r.getY() + r.getHeight() + e.getImage().getHeight() / 2);
		}
	}

	public static boolean moveAround(Rectangle r, Entity e, double targetX, double targetY, Level l) {
		double x = 0, y = 0;
		if (Math.abs(r.getX() - e.getX()) <= Math.abs(r.getX() + r.getWidth() - e.getX())
				&& Math.abs(r.getX() - e.getX()) <= Math.abs(r.getY() - e.getY())
				&& Math.abs(r.getX() - e.getX()) <= Math.abs(r.getY() + r.getHeight() - e.getY())) {
			y = targetY > r.getY() + r.getHeight() / 2 ? 1 : -1;
		} else if (Math.abs(r.getX() + r.getWidth() - e.getX()) <= Math.abs(r.getX() - e.getX())
				&& Math.abs(r.getX() + r.getWidth() - e.getX()) <= Math.abs(r.getY() - e.getY())
				&& Math.abs(r.getX() + r.getWidth() - e.getX()) <= Math.abs(r.getY() + r.getHeight() - e.getY())) {
			y = targetY > r.getY() + r.getHeight() / 2 ? 1 : -1;
		} else if (Math.abs(r.getY() - e.getY()) <= Math.abs(r.getX() - e.getX())
				&& Math.abs(r.getY() - e.getY()) <= Math.abs(r.getX() + r.getWidth() - e.getX())
				&& Math.abs(r.getY() - e.getY()) <= Math.abs(r.getY() + r.getHeight() - e.getY())) {
			x = targetX > r.getX() + r.getWidth() / 2 ? 1 : -1;
		} else {
			x = targetX > r.getX() + r.getWidth() / 2 ? 1 : -1;
		}
		Utils.setDXDY(e, x + e.getX(), y + e.getY(), e.getSpeed(), l);
		if ((x == 1 && e.getX() - e.getImage().getWidth() / 2 > r.getX() + r.getWidth())
				|| (x == -1 && e.getX() + e.getImage().getWidth() / 2 < r.getX())
				|| (y == 1 && e.getY() - e.getImage().getHeight() / 2 > r.getY() + r.getHeight())
				|| (y == -1 && e.getY() + e.getImage().getHeight() / 2 < r.getY())) {
			return true;
		}
		return false;
	}
}
