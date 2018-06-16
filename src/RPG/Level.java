package RPG;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Entity.Entity;
import Entity.Player;

public class Level extends JPanel implements Runnable, KeyListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1230495266243438959L;
	private MapGenerator gen;
	private ArrayList<Entity> entities, add, remove;
	private ArrayList<Updatable> updates;
	private Player player;
	private Main m;

	private int refreshTime = 20;

	private Spawner spawn;

	double x, y;

	private boolean pause;

	public final boolean debug = false;

	public Level(Main m, Player player, int refresh, Spawner spawn) {
		this.m = m;
		this.player = player;
		player.setX(0).setDX(0).setY(0).setDY(0);
		gen = new MapGenerator();
		this.refreshTime = refresh;
		add = new ArrayList<Entity>();
		remove = new ArrayList<Entity>();
		entities = new ArrayList<Entity>();
		updates = new ArrayList<Updatable>();
		for (Rectangle r : gen.getObstacles((int) Math.round(-player.getX() + gen.size * 128 - this.getWidth() / 2),
				(int) Math.round(-player.getY() + gen.size * 128 - this.getHeight() / 2), this.getWidth(),
				this.getHeight(), this)) {
			if (r.intersects(new Rectangle((int) (player.getX() + player.getDX() - player.getImage().getWidth() / 2),
					(int) (player.getY() + player.getDY() - player.getImage().getHeight() / 2),
					player.getImage().getWidth(), player.getImage().getHeight()))) {
				Utils.moveOut(r, player);
			}
		}
		this.spawn = spawn;
		spawn.setLevel(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D gI = (Graphics2D) g;
		gI.fillRect(0, 0, this.getWidth(), this.getHeight());

		gI.drawImage(gen.getTempImage((int) Math.round(-player.getX() + gen.size * 128 - this.getWidth() / 2),
				(int) Math.round(-player.getY() + gen.size * 128 - this.getHeight() / 2), this.getWidth(),
				this.getHeight()), 0, 0, null);

		if (debug) {
			gI.setColor(Color.RED);

			for (Rectangle r : gen.getObstacles((int) Math.round(-player.getX() + gen.size * 128 - this.getWidth() / 2),
					(int) Math.round(-player.getY() + gen.size * 128 - this.getHeight() / 2), this.getWidth(),
					this.getHeight(), this)) {
				gI.drawRect((int) (r.getX() - player.getX() + this.getWidth() / 2),
						(int) (r.getY() - player.getY() + this.getHeight() / 2), r.width, r.height);
			}
		}

		synchronized (entities) {
			for (Entity e : entities) {
				gI.drawImage(e.getImage(),
						(int) Math.round(e.getX() - player.getX() + (this.getWidth() - e.getImage().getWidth()) / 2),
						(int) Math.round(e.getY() - player.getY() + (this.getHeight() - e.getImage().getHeight()) / 2),
						this);
			}
		}

		gI.drawImage(spawn.getImage(this.getWidth() / 4, this.getHeight() / 7, Color.BLUE), 3 * this.getWidth() / 4, 0,
				null);

		gI.drawImage(player.getPlayerInfoBar(400, 120), 10, 10, null);

		gI.drawImage(player.getImage(), (this.getWidth() - player.getImage().getWidth()) / 2,
				(this.getHeight() - player.getImage().getHeight()) / 2, null);

		g.drawImage(player.getPlayerSkills(4 * this.getHeight() / 10, this.getHeight() / 10),
				(int) (1.5 * this.getWidth() / 4), (int) 8.5 * this.getHeight() / 10, null);

		gI.drawImage(player.getImage(this), 0, 0, null);

	}

	@Override
	public void run() {
		while (true) {
			long time = System.currentTimeMillis();
			if (!pause) {
				synchronized (entities) {
					spawn.update(this.getRefreshTime());
				}
				ArrayList<Rectangle> obstacles = gen.getObstacles(
						(int) Math.round(-player.getX() + gen.size * 128 - this.getWidth() / 2),
						(int) Math.round(-player.getY() + gen.size * 128 - this.getHeight() / 2), this.getWidth(),
						this.getHeight(), this);
				Rectangle playerRect = new Rectangle(
						(int) (player.getX() + player.getDX() - player.getImage().getWidth() / 2),
						(int) (player.getY() + player.getDY() - player.getImage().getHeight() / 2),
						player.getImage().getWidth(), player.getImage().getHeight());
				for (Rectangle r : obstacles) {
					if (r.intersects(playerRect)) {
						switch (player.getCollision()) {
						case Entity.NOTHING_ON_HIT:
							break;
						case Entity.REMOVE_ON_HIT:
							player.setCollided(true, r);
						case Entity.STOP_ON_HIT:
							if (!player.moveStop()) {
								player.setDX(0).setDY(0);
							}
							player.setCollided(true, r);
							break;
						case Entity.PATHING_NOTHING_ON_HIT:
							player.setCollided(true, r);
							break;
						}
					}
				}
				if (player.canMove()) {
					player.setX(player.getX() + player.getDX()).setY(player.getY() + player.getDY());
				}
				synchronized (entities) {
					for (Entity e : entities) {
						if (e.getCollision() != Entity.NOTHING_ON_HIT) {
							Rectangle entity = new Rectangle((int) (e.getX() + e.getDX() - e.getImage().getWidth() / 2),
									(int) (e.getY() + e.getDY() - e.getImage().getHeight() / 2),
									e.getImage().getWidth(), e.getImage().getHeight());
							for (Rectangle r : obstacles) {
								if (r.intersects(entity)) {
									switch (e.getCollision()) {
									case Entity.REMOVE_ON_HIT:
										e.setCollided(true, r);
										remove.add(e);
									case Entity.STOP_ON_HIT:
										e.setCollided(true, r);
										if (!e.moveStop()) {
											e.setDX(0).setDY(0);
										}
										break;
									case Entity.PATHING_NOTHING_ON_HIT:
										e.setCollided(true, r);
										break;
									}
								}
							}
						}
						if (e.canMove()) {
							e.setX(e.getX() + e.getDX()).setY(e.getY() + e.getDY());
						}
					}
				}
				player.update(this);
				synchronized (entities) {
					for (Entity e : entities) {
						e.update(this);
					}
					for (Entity e : add) {
						entities.add(e);
					}
					for (Entity e : remove) {
						entities.remove(e);
					}
				}
				for (Updatable u : updates) {
					u.update(this);
				}
				add.clear();
				remove.clear();
			}
			this.invalidate();
			this.repaint();
			if (debug) {
				System.out.println(System.currentTimeMillis() - time);
			}
			try {
				Thread.sleep((refreshTime - (System.currentTimeMillis() - time)) > 0
						? refreshTime - (System.currentTimeMillis() - time)
						: 0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Level setPause(boolean pause) {
		this.pause = pause;
		return this;
	}

	public boolean getPause() {
		return pause;
	}

	public ArrayList<Entity> getEntites() {
		return entities;
	}

	public Level addEntity(Entity e) {
		if (e != null) {
			add.add(e);
		}
		return this;
	}

	public Level addEntityMoveOut(Entity e) {
		if (e != null) {
			add.add(e);
			for (Rectangle r : gen.getObstacles((int) Math.round(-player.getX() + gen.size * 128 - this.getWidth() / 2),
					(int) Math.round(-player.getY() + gen.size * 128 - this.getHeight() / 2), this.getWidth(),
					this.getHeight(), this)) {
				if (r.intersects(new Rectangle((int) (e.getX() + e.getDX() - e.getImage().getWidth() / 2),
						(int) (e.getY() + e.getDY() - e.getImage().getHeight() / 2), e.getImage().getWidth(),
						e.getImage().getHeight()))) {
					Utils.moveOut(r, e);
				}
			}
		}
		return this;
	}

	public Level removeEntity(Entity e) {
		if (e != null) {
			remove.add(e);
		}
		return this;
	}

	public Level playerDead() {
		JOptionPane.showMessageDialog(null, "YOU LOST!");
		System.exit(1);
		return this;
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		if (!player.moveStop()) {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_D:
				x = player.getSpeed();
				break;
			case KeyEvent.VK_A:
				x = -player.getSpeed();
				break;
			case KeyEvent.VK_W:
				y = -player.getSpeed();
				break;
			case KeyEvent.VK_S:
				y = player.getSpeed();
				break;
			}
			player.setDX(y == 0 ? x : x / 2).setDY(x == 0 ? y : y / 2);
		}
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		if (!player.moveStop()) {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_D:
				x = x == player.getSpeed() ? 0 : x;
				break;
			case KeyEvent.VK_A:
				x = x == -player.getSpeed() ? 0 : x;
				break;
			case KeyEvent.VK_W:
				y = y == -player.getSpeed() ? 0 : y;
				break;
			case KeyEvent.VK_S:
				y = y == player.getSpeed() ? 0 : y;
				break;
			}
			player.setDX(y == 0 ? x : x / 2).setDY(x == 0 ? y : y / 2);
		}
	}

	@Override
	public void keyTyped(KeyEvent ke) {
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p, m);
		player.keyPress(ke.getKeyChar(), p.x + ((int) player.getX()) - this.getWidth() / 2,
				p.y + ((int) player.getY()) - this.getHeight() / 2, this);
	}

	public Point getMouse() {
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p, m);
		p.move(p.x + ((int) player.getX()) - this.getWidth() / 2, p.y + ((int) player.getY()) - this.getHeight() / 2);
		return p;
	}

	@Override
	public void mouseClicked(MouseEvent me) {
	}

	@Override
	public void mouseEntered(MouseEvent me) {
	}

	@Override
	public void mouseExited(MouseEvent me) {
	}

	@Override
	public void mousePressed(MouseEvent me) {
		player.mousePress(me.getX(), me.getY(), this);
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		player.mouseRelease(me.getX(), me.getY(), this);
	}

	public int getRefreshTime() {
		return refreshTime;
	}

	public Player getPlayer() {
		return player;
	}

	public Spawner getSpawner() {
		return spawn;
	}

	public Level addUpdatable(Updatable u) {
		updates.add(u);
		return this;
	}

	public Level removeUpdatable(Updatable u) {
		updates.remove(u);
		return this;
	}
}
