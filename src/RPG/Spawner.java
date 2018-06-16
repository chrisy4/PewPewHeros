package RPG;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Enemy.BasicEnemy;
import Enemy.BossBailey;
import Enemy.PounceEnemy;
import Entity.Entity;

public class Spawner {

	private Level l;

	public static final int BASIC = 0, BAILEY = 1, POUNCE = 2;

	public static final int LEVEL_PER_SPAWN = 0, MATCH_PLAYER = 1;

	private ArrayList<Spawn> spawns = new ArrayList<Spawn>();

	private long wave = 0, level = 1;

	public void setLevel(Level l) {
		this.l = l;
	}

	public void registerSpawn(int entity, int num, int count, int xDis, int yDis, int start, int levelType) {
		spawns.add(new Spawn(entity, num, count, xDis, yDis, start, levelType));
	}

	public void update(int time) {
		for (Spawn s : spawns) {
			s.update(l.getRefreshTime());
		}
	}

	public BufferedImage getImage(int x, int y, Color c) {
		BufferedImage image = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(c);
		g.setFont(new Font(Font.SERIF, Font.ITALIC, (int) Math
				.round((y / (spawns.size() + 1)) * Toolkit.getDefaultToolkit().getScreenResolution() / 144.0)));
		g.drawString(String.format("Wave: %d", wave), 0, g.getFontMetrics().getHeight());
		for (int i = 0; i < spawns.size(); i++) {
			g.drawString(spawns.get(i).getDescription(), 0, g.getFontMetrics().getHeight() * (i + 2));
		}
		return image;
	}

	class Spawn {

		int entity, sTime, num, xDis, yDis, count = 0, type;

		public Spawn(int entity, int num, int count, int xDis, int yDis, int start, int levelType) {
			this.entity = entity;
			this.num = num;
			this.sTime = count;
			this.count = start;
			this.xDis = xDis;
			this.yDis = yDis;
			this.type = levelType;
		}

		public void update(int time) {
			count += time;
			if (count >= sTime) {
				count = 0;
				wave++;
				for (int i = 0; i < num; i++) {
					l.addEntityMoveOut(Spawner.getEntity(entity, l.getPlayer().getLevel(),
							(int) (l.getPlayer().getX() + Math.random() * xDis * 2 - xDis),
							(int) (l.getPlayer().getY() + Math.random() * yDis * 2 - yDis)));
				}
				if (type == LEVEL_PER_SPAWN)
					level++;
			}
		}

		public int getLevel() {
			switch (type) {
			case LEVEL_PER_SPAWN:
				return (int) level;
			case MATCH_PLAYER:
				return l.getPlayer().getLevel();
			default:
				return 0;
			}
		}

		public String getDescription() {
			return String.format("%s: Spawn Timer -  %.1fs, Level - %d", getName(entity),
					((float) sTime - count) / 1000f, getLevel());
		}
	}

	public static Entity getEntity(int num, int level, int x, int y) {
		switch (num) {
		case BASIC:
			return new BasicEnemy(level, x, y);
		case BAILEY:
			return new BossBailey(level, x, y);
		case POUNCE:
			return new PounceEnemy(level, x, y);
		}
		return null;
	}

	public static String getName(int entity) {
		switch (entity) {
		case BASIC:
			return "Basic";
		case BAILEY:
			return "Bailey";
		case POUNCE:
			return "Pounce";
		default:
			return null;
		}
	}
}
