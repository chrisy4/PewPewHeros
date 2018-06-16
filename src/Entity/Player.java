package Entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Item.Item;
import RPG.ImageLoader;
import RPG.Level;
import Skill.BasicSkill;
import Skill.Skill;
import Skill.Archer.Energize;
import Skill.Archer.Longshot;
import Skill.Archer.SwiftFeet;
import Skill.Archer.Teleport;
import Skill.Gunslinger.Bloodlust;
import Skill.Gunslinger.GroundPound;
import Skill.Gunslinger.TankUp;
import Skill.Gunslinger.UndyingRage;
import Skill.Warlock.DemonicOffering;
import Skill.Warlock.Drain;
import Skill.Warlock.MortalBlast;
import Skill.Warlock.SoulShackle;
import Skill.Wizard.BlindingFog;
import Skill.Wizard.DeathCloud;
import Skill.Wizard.DeathInABottle;
import Skill.Wizard.TimeCrasher;

public class Player extends Entity {

	public static final int GUNSLINGER = 0, ARCHER = 1, WIZARD = 2, WARLOCK = 3;

	public static final int GUNSLINGER_BASE_HP = 500, GUNSLINGER_BASE_MANA = 50, GUNSLINGER_BASE_HP_GROWTH = 190,
			GUNSLINGER_BASE_MANA_GROWTH = 10, ARCHER_BASE_HP = 350, ARCHER_BASE_MANA = 60, ARCHER_BASE_HP_GROWTH = 150,
			ARCHER_BASE_MANA_GROWTH = 12, WIZARD_BASE_HP = 400, WIZARD_BASE_MANA = 150, WIZARD_BASE_HP_GROWTH = 160,
			WIZARD_BASE_MANA_GROWTH = 30, WARLOCK_BASE_HP = 425, WARLOCK_BASE_MANA = 120, WARLOCK_BASE_HP_GROWTH = 155,
			WARLOCK_BASE_MANA_GROWTH = 24;

	public static final double BASE_EXP_REQ = 6.5, HEALTH_REGEN_PER_SECOND = 1.0, MANA_REGEN_PER_SECOND = 3.0,
			BASE_ATTACK = 10, BASE_ATTACK_GROWTH = 1.5, BASIC_ATTACK_SPEED = 5, BASIC_ATTACK_DISTANCE = 1200,
			BASE_SPEED = 4, EXP_EXP = 1.4;

	private int type;

	private double mDX, mDY, xp, attackBoostPercent, attackBoostFlat, speedBoostPercent, speedBoostFlat, defenseBoost;

	private Item[] items = new Item[48];
	private Item[] equippedItems = new Item[4];

	private ArrayList<Skill> skills = new ArrayList<Skill>();;

	private Skill basic;

	private boolean inven = false;

	private Item select;

	public static Player getDefualtPlayer(int type) {
		switch (type) {
		case Player.GUNSLINGER:
			return (Player) new Player().setType(0).setMaxDX(5).setMaxDY(5).setLevel(1)
					.setMaxHealth(Player.GUNSLINGER_BASE_HP).setMaxMana(Player.GUNSLINGER_BASE_MANA)
					.setHealth(Player.GUNSLINGER_BASE_HP).setMana(Player.GUNSLINGER_BASE_MANA);
		case Player.ARCHER:
			return (Player) new Player().setType(1).setMaxDX(5).setMaxDY(5).setLevel(1)
					.setMaxHealth(Player.ARCHER_BASE_HP).setMaxMana(Player.ARCHER_BASE_MANA)
					.setHealth(Player.ARCHER_BASE_HP).setMana(Player.ARCHER_BASE_MANA);
		case Player.WIZARD:
			return (Player) new Player().setType(2).setMaxDX(5).setMaxDY(5).setLevel(1)
					.setMaxHealth(Player.WIZARD_BASE_HP).setMaxMana(Player.WIZARD_BASE_MANA)
					.setHealth(Player.WIZARD_BASE_HP).setMana(Player.WIZARD_BASE_MANA);
		case Player.WARLOCK:
			return (Player) new Player().setType(3).setMaxDX(5).setMaxDY(5).setLevel(1)
					.setMaxHealth(Player.WARLOCK_BASE_HP).setMaxMana(Player.WARLOCK_BASE_MANA)
					.setHealth(Player.WARLOCK_BASE_HP).setMana(Player.WARLOCK_BASE_MANA);
		}
		return null;
	}

	public int getType() {
		return type;
	}

	public Player setType(int type) {
		this.type = type;
		switch (type) {
		case Player.GUNSLINGER:
			basic = new BasicSkill(this, ' ');
			skills.add(new Bloodlust(this, '1'));
			skills.add(new TankUp(this, '2'));
			skills.add(new UndyingRage(this, '3'));
			skills.add(new GroundPound(this, '4'));
			break;
		case Player.ARCHER:
			basic = new BasicSkill(this, ' ');
			skills.add(new Longshot(this, '1'));
			skills.add(new Energize(this, '2'));
			skills.add(new SwiftFeet(this, '3'));
			skills.add(new Teleport(this, '4'));
			break;
		case Player.WIZARD:
			basic = new BasicSkill(this, ' ');
			skills.add(new BlindingFog(this, '1'));
			skills.add(new DeathCloud(this, '2'));
			skills.add(new DeathInABottle(this, '3'));
			skills.add(new TimeCrasher(this, '4'));
			break;
		case Player.WARLOCK:
			basic = new BasicSkill(this, ' ');
			skills.add(new Drain(this, '1'));
			skills.add(new DemonicOffering(this, '2'));
			skills.add(new SoulShackle(this, '3'));
			skills.add(new MortalBlast(this, '4'));
			break;
		}
		return this;
	}

	public Player levelUp() {
		switch (this.getType()) {
		case Player.GUNSLINGER:
			return (Player) this.setXP(this.getXP() - Player.getExpReq(this.getLevel())).setLevel(this.getLevel() + 1)
					.setMaxHealth(Math.pow(Player.GUNSLINGER_BASE_HP_GROWTH, 1 + 0.05 * (this.getLevel() - 1))
							+ this.getMaxHealth())
					.setMaxMana(
							(this.getLevel() - 1) * Player.GUNSLINGER_BASE_MANA_GROWTH + Player.GUNSLINGER_BASE_MANA)
					.setHealth(
							Math.pow(Player.GUNSLINGER_BASE_HP_GROWTH, 1 + 0.05 * this.getLevel()) + this.getHealth())
					.setMana(Player.GUNSLINGER_BASE_MANA_GROWTH + this.getMana());
		case Player.ARCHER:
			return (Player) this.setXP(this.getXP() - Player.getExpReq(this.getLevel())).setLevel(this.getLevel() + 1)
					.setMaxHealth(Math.pow(Player.ARCHER_BASE_HP_GROWTH, 1 + 0.05 * (this.getLevel() - 1))
							+ this.getMaxHealth())
					.setMaxMana((this.getLevel() - 1) * Player.ARCHER_BASE_MANA_GROWTH + Player.ARCHER_BASE_MANA)
					.setHealth(Math.pow(Player.ARCHER_BASE_HP_GROWTH, 1 + 0.05 * this.getLevel()) + this.getHealth())
					.setMana(Player.ARCHER_BASE_MANA_GROWTH + this.getMana());
		case Player.WIZARD:
			return (Player) this.setXP(this.getXP() - Player.getExpReq(this.getLevel())).setLevel(this.getLevel() + 1)
					.setMaxHealth(Math.pow(Player.WIZARD_BASE_HP_GROWTH, 1 + 0.05 * (this.getLevel() - 1))
							+ this.getMaxHealth())
					.setMaxMana((this.getLevel() - 1) * Player.WIZARD_BASE_MANA_GROWTH + Player.WIZARD_BASE_MANA)
					.setHealth(Math.pow(Player.WIZARD_BASE_HP_GROWTH, 1 + 0.05 * this.getLevel()) + this.getHealth())
					.setMana(Player.WIZARD_BASE_MANA_GROWTH + this.getMana());
		case Player.WARLOCK:
			return (Player) this.setXP(this.getXP() - Player.getExpReq(this.getLevel())).setLevel(this.getLevel() + 1)
					.setMaxHealth(Math.pow(Player.WARLOCK_BASE_HP_GROWTH, 1 + 0.05 * (this.getLevel() - 1))
							+ this.getMaxHealth())
					.setMaxMana((this.getLevel() - 1) * Player.WARLOCK_BASE_MANA_GROWTH + Player.WARLOCK_BASE_MANA)
					.setHealth(Math.pow(Player.WARLOCK_BASE_HP_GROWTH, 1 + 0.05 * this.getLevel()) + this.getHealth())
					.setMana(Player.WARLOCK_BASE_MANA_GROWTH + this.getMana());
		}
		return null;
	}

	public BufferedImage getPlayerSkills(int x, int y) {
		BufferedImage image = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(2));
		g.drawRect(0, 0, x, y);
		for (int i = 0; i < skills.size(); i++) {
			g.drawImage(skills.get(i).getImage(x / skills.size() - 4, y - 4), i * x / skills.size() + 2, 2, null);
		}
		return image;
	}

	public BufferedImage getPlayerInfoBar(int x, int y) {
		// creates buffered image and gets G2D from it
		BufferedImage image = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();

		// fills image with transparent pixels
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, x, y);

		// draws player level
		g.setColor(Color.WHITE);
		g.setFont(new Font(Font.SERIF, Font.BOLD, 16));
		g.drawString(this.getLevel() + "", (y + g.getFontMetrics().stringWidth(this.getLevel() + "")) / 2,
				(y + g.getFontMetrics().getHeight()) / 2);

		// draws border for health and mana bar
		g.setColor(Color.BLACK);
		g.drawLine(2 * x / 5, y / 5, 4 * x / 5, y / 5);
		g.drawLine(2 * x / 5, 2 * y / 5, 4 * x / 5, 2 * y / 5);
		g.drawLine(2 * x / 5, 3 * y / 5, 4 * x / 5, 3 * y / 5);
		g.drawLine(2 * x / 5, 4 * y / 5, 4 * x / 5, 4 * y / 5);

		g.drawArc(2 * x / 5 - y / 10, y / 5, y / 5, y / 5, -90, -180);
		g.drawArc(4 * x / 5 - y / 10, y / 5, y / 5, y / 5, -90, 180);

		g.drawArc(2 * x / 5 - y / 10, 3 * y / 5, y / 5, y / 5, -90, -180);
		g.drawArc(4 * x / 5 - y / 10, 3 * y / 5, y / 5, y / 5, -90, 180);

		double healthBarXLen = (2 * y / 5 + 2 * x / 5) * this.getHealth() / this.getMaxHealth(),
				manaBarXLen = (2 * y / 5 + 2 * x / 5) * this.getMana() / this.getMaxMana();

		g.setColor(Color.BLUE);

		// fills mana bar
		g.fillRect(2 * x / 5, 3 * y / 5 + 1,
				(int) Math.round(
						manaBarXLen > y / 5 ? manaBarXLen < (y / 5 + 2 * x / 5) ? manaBarXLen - y / 5 : 2 * x / 5 : 0),
				y / 5 - 2);
		g.fillArc(2 * x / 5 - y / 10, 3 * y / 5 + 1, (int) Math.round((manaBarXLen > y / 5 ? y / 5 : manaBarXLen)),
				y / 5 - 2, -90, -180);
		g.fillArc(4 * x / 5 - y / 10, 3 * y / 5 + 1,
				(int) Math.round((manaBarXLen > y / 5 + 2 * x / 5 ? manaBarXLen - y / 5 - 2 * x / 5 : 0)), y / 5 - 2,
				-90, 180);

		g.setColor(Color.RED);

		// fills health bar
		g.fillRect(2 * x / 5, y / 5 + 1, (int) Math.round(
				healthBarXLen > y / 5 ? healthBarXLen < (y / 5 + 2 * x / 5) ? healthBarXLen - y / 5 : 2 * x / 5 : 0),
				y / 5 - 2);
		g.fillArc(2 * x / 5 - y / 10, y / 5 + 1, (int) Math.round((healthBarXLen > y / 5 ? y / 5 : healthBarXLen)),
				y / 5 - 2, -90, -180);
		g.fillArc(4 * x / 5 - y / 10, y / 5 + 1,
				(int) Math.round((healthBarXLen > y / 5 + 2 * x / 5 ? healthBarXLen - y / 5 - 2 * x / 5 : 0)),
				y / 5 - 2, -90, 180);

		// Level up progress arc
		g.setColor(Color.GREEN);
		g.setStroke(new BasicStroke(4));
		g.drawArc(0, 0, y - 2, y - 2, 90, (int) -Math.round(360 * (this.getXP() / Player.getExpReq(this.getLevel()))));
		g.setStroke(new BasicStroke(2));

		g.setColor(Color.BLACK);

		g.setFont(new Font(Font.SERIF, Font.ITALIC,
				(int) Math.round((y / 5 - 2) * Toolkit.getDefaultToolkit().getScreenResolution() / 72.0)));

		g.drawString((int) Math.round(this.getHealth()) + "/" + (int) Math.round(this.getMaxHealth()),
				3 * x / 5 - g.getFontMetrics().stringWidth(
						(int) Math.round(this.getHealth()) + "/" + (int) Math.round(this.getMaxHealth())) / 2,
				2 * y / 5 - 1);

		g.drawString((int) Math.round(this.getMana()) + "/" + (int) Math.round(this.getMaxMana()),
				3 * x / 5 - g.getFontMetrics()
						.stringWidth((int) Math.round(this.getMana()) + "/" + (int) Math.round(this.getMaxMana())) / 2,
				4 * y / 5 - 1);

		// Semi-transparent grey
		g.setColor(new Color(105f / 255, 105f / 255, 105f / 255, 0.6f));

		// lines for health bar at 1/4, 1/2 and 3/4
		g.drawLine(2 * x / 5 - y / 5 + (2 * y / 5 + 2 * x / 5) / 4, y / 5 + 1,
				2 * x / 5 - y / 5 + (2 * y / 5 + 2 * x / 5) / 4, 2 * y / 5 - 1);
		g.drawLine(2 * x / 5 - y / 5 + (2 * y / 5 + 2 * x / 5) / 2, y / 5 + 1,
				2 * x / 5 - y / 5 + (2 * y / 5 + 2 * x / 5) / 2, 2 * y / 5 - 1);
		g.drawLine(2 * x / 5 - y / 5 + (3 * (2 * y / 5 + 2 * x / 5) / 4), y / 5 + 1,
				2 * x / 5 - y / 5 + 3 * (2 * y / 5 + 2 * x / 5) / 4, 2 * y / 5 - 1);

		// lines for mana bar at 1/4, 1/2 and 3/4
		g.drawLine(2 * x / 5 - y / 5 + (2 * y / 5 + 2 * x / 5) / 4, 3 * y / 5 + 1,
				2 * x / 5 - y / 5 + (2 * y / 5 + 2 * x / 5) / 4, 4 * y / 5 - 1);
		g.drawLine(2 * x / 5 - y / 5 + (2 * y / 5 + 2 * x / 5) / 2, 3 * y / 5 + 1,
				2 * x / 5 - y / 5 + (2 * y / 5 + 2 * x / 5) / 2, 4 * y / 5 - 1);
		g.drawLine(2 * x / 5 - y / 5 + (3 * (2 * y / 5 + 2 * x / 5) / 4), 3 * y / 5 + 1,
				2 * x / 5 - y / 5 + 3 * (2 * y / 5 + 2 * x / 5) / 4, 4 * y / 5 - 1);

		return image;
	}

	public Player addItem(Item item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				items[i] = item;
				break;
			}
		}
		return this;
	}

	public Player removeItem(Item item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == item) {
				items[i] = null;
				return this;
			}
		}
		for (int i = 0; i < equippedItems.length; i++) {
			if (equippedItems[i] == item) {
				equippedItems[i] = null;
				return this;
			}
		}
		return this;
	}

	public Item[] getItems() {
		return items;
	}

	public Item[] getEquippedItems() {
		return equippedItems;
	}

	@Override
	public BufferedImage getImage() {
		return ImageLoader.getPlayerImage(this.getType());
	}

	public BufferedImage getImage(Level l) {
		BufferedImage image = new BufferedImage((int) l.getWidth(), (int) l.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		if (inven) {
			Graphics2D g = (Graphics2D) image.getGraphics();

			// fills image with transparent pixels
			g.setColor(new Color(0f, 0f, 0f, 0f));
			g.fillRect(0, 0, (int) l.getWidth(), (int) l.getHeight());

			g.drawImage(Player.getInventoryImage(l.getWidth(), l.getHeight(), items, getEquippedItems(), 8, 6, l, this,
					select), 0, 0, null);
		}
		return image;
	}

	@Override
	public int getSide() {
		return Entity.PLAYER;
	}

	@SuppressWarnings("unused")
	@Override
	public void update(Level l) {
		if (this.getXP() > Player.getExpReq(this.getLevel())) {
			this.levelUp();
		}
		if (this.getHealth() < 0 && !l.debug) {
			l.playerDead();
		}
		this.reduceCCDuration(l.getRefreshTime());
		if (this.canMove()) {
			if (this.getMana() < this.getMaxMana()) {
				this.increaseMana(Player.MANA_REGEN_PER_SECOND * this.getMaxMana() / (100000 / l.getRefreshTime()));
			} else {
				this.setMana(this.getMaxMana());
			}
			if (this.getHealth() < this.getMaxHealth()) {
				this.increaseHP(Player.HEALTH_REGEN_PER_SECOND * this.getMaxHealth() / (100000 / l.getRefreshTime()));
			} else {
				this.setHealth(this.getMaxHealth());
			}
			for (Skill s : skills) {
				s.update(l);
			}
			basic.update(l);
		}
	}

	public double getMaxDX() {
		return mDX;
	}

	public double getMaxDY() {
		return mDY;
	}

	public Player setMaxDX(double dx) {
		this.mDX = dx;
		return this;
	}

	public Player setMaxDY(double dy) {
		this.mDY = dy;
		return this;
	}

	public Player increaseXP(double xp) {
		this.xp += xp;
		return this;
	}

	public Player setXP(double xp) {
		this.xp = xp;
		return this;
	}

	public Player decreaseHP(double hp) {
		return (Player) this.setHealth(this.getHealth() - hp);
	}

	public Player increaseHP(double hp) {
		return (Player) this
				.setHealth(this.getHealth() + hp > this.getMaxHealth() ? this.getMaxHealth() : this.getHealth() + hp);
	}

	public Player decreaseMana(double mana) {
		return (Player) this.setMana(this.getMana() - mana);
	}

	public Player increaseMana(double mana) {
		return (Player) this
				.setMana(this.getMana() + mana > this.getMaxMana() ? this.getMaxMana() : this.getMana() + mana);
	}

	public double getXP() {
		return xp;
	}

	public static double getExpReq(int level) {
		return Math.pow(Player.BASE_EXP_REQ, Player.EXP_EXP * level);
	}

	public double getAttack() {
		double attack = 0;
		for (Item i : equippedItems) {
			if (i != null) {
				attack += i.getAttack();
			}
		}
		return Player.BASE_ATTACK * Player.BASE_ATTACK_GROWTH * this.getLevel() * (1 + this.attackBoostPercent / 100)
				+ this.attackBoostFlat + attack * (1 + this.attackBoostPercent / 100);
	}

	public double getDefense() {
		double defense = 0;
		for (Item i : equippedItems) {
			if (i != null) {
				defense += i.getDefense();
			}
		}
		return defense + defenseBoost;
	}

	public double getPenetration() {
		return 0;
	}

	public double getCDR() {
		return 0;
	}

	public Player giveAttackBoostPercent(double percent) {
		this.attackBoostPercent += percent;
		return this;
	}

	public Player giveAttackBoostFlat(double attack) {
		this.attackBoostFlat += attack;
		return this;
	}

	public Player reduceAttackBoostPercent(double percent) {
		this.attackBoostPercent -= percent;
		return this;
	}

	public Player reduceAttackBoostFlat(double attack) {
		this.attackBoostFlat -= attack;
		return this;
	}

	public void keyPress(char c, int x, int y, Level l) {
		for (Skill s : skills) {
			if (s.getKey() == c) {
				if (s.getManaCost() < this.getMana() && s.getCount() == 0) {
					l.addEntity(s.onUse(l).getProjectile(x, y, l));
					this.decreaseMana(s.getManaCost());
				}
			}
		}
		if (basic.getKey() == c) {
			((BasicSkill) basic).toggle();
		}
		if (c == 'e') {
			inven = !inven;
			l.setPause(inven);
		}
	}

	public static BufferedImage getInventoryImage(double x, double y, Item[] items, Item[] equipped, int xS, int yS,
			Level l, Player p, Item select) {
		// creates buffered image and gets G2D from it
		BufferedImage image = new BufferedImage((int) x, (int) y, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();

		// fills image with transparent pixels
		g.setColor(new Color(1f, 1f, 1f, 0.8f));
		g.fillRect(0, 0, (int) x, (int) y);

		int q = 0;
		for (Item i : items) {
			if (i != null) {
				g.drawImage(i.getImage(), (int) Math.round((q % xS) * (x / xS)),
						(int) Math.round((1 + q / xS) * (y / yS)), (int) (x / xS), (int) (y / yS), null);
				if (select != null && i == select) {
					g.setFont(new Font(Font.SERIF, Font.BOLD, (int) Math
							.round((y / (yS * 6)) * Toolkit.getDefaultToolkit().getScreenResolution() / 72.0)));

					ArrayList<String> strings = new ArrayList<String>();

					int start = 0;

					for (int ii = 0; ii < i.getDescription().length(); ii++) {
						if (g.getFontMetrics().stringWidth(i.getDescription().substring(start, ii)) > (x / xS)) {
							strings.add(i.getDescription().substring(start, ii - 1));
							start = ii - 1;
						}
					}

					strings.add(i.getDescription().substring(start));

					g.setColor(Color.BLACK);

					for (int j = 0; j < strings.size(); j++) {
						g.drawString(strings.get(j), (int) Math.round((q % xS) * (x / xS)),
								(int) (Math.round((1 + q / xS) * (y / yS)) + j * (y / (yS * 6))));
					}
				}
			}
			q++;
		}

		q = 0;
		for (Item i : equipped) {
			if (i != null) {
				g.drawImage(i.getImage(), (int) Math.round((q % xS) * (x / xS)), 5, (int) (x / xS), (int) (y / yS),
						null);
				if (select != null && i == select) {
					g.setFont(new Font(Font.SERIF, Font.BOLD, (int) Math
							.round((y / (yS * 6)) * Toolkit.getDefaultToolkit().getScreenResolution() / 72.0)));

					ArrayList<String> strings = new ArrayList<String>();

					int start = 0;

					for (int ii = 0; ii < i.getDescription().length(); ii++) {
						if (g.getFontMetrics().stringWidth(i.getDescription().substring(start, ii)) > (x / xS)) {
							strings.add(i.getDescription().substring(start, ii - 1));
							start = ii - 1;
						}
					}

					strings.add(i.getDescription().substring(start));

					g.setColor(Color.BLACK);

					for (int j = 0; j < strings.size(); j++) {
						g.drawString(strings.get(j), (int) Math.round((q % xS) * (x / xS)),
								(int) (Math.round((1 + q / xS) * (y / yS)) + j * (y / (yS * 6))));
					}
				}
			}
			q++;
		}

		g.drawImage(ImageLoader.getItemImage(ImageLoader.TRASH), (int) ((xS - 1) * (x / xS)), 0, (int) x / xS,
				(int) y / yS, null);

		g.setColor(new Color(0, 0, 0));

		g.setFont(new Font(Font.SERIF, Font.ITALIC,
				(int) Math.round(((y / (4 * yS)) * Toolkit.getDefaultToolkit().getScreenResolution() / 72.0))));

		g.drawString(String.format("Attack: %.1f", p.getAttack()), (int) (4 * (x / xS)), (int) y / (2 * yS));
		g.drawString(String.format("Defense: %.1f", p.getDefense()), (int) (4 * (x / xS)), (int) y / yS);

		for (int i = 1; i < xS; i++) {
			g.drawLine((int) (i * (x / xS)), (int) (i > 4 ? (y / yS) : 0), (int) (i * (x / xS)), l.getHeight());
		}

		for (int i = 1; i < yS; i++) {
			g.drawLine(0, (int) (i * (y / yS)), l.getWidth(), (int) (i * (y / yS)));
		}

		return image;
	}

	public void mousePress(int x, int y, Level l) {
		if (this.inven) {
			if (y / (l.getHeight() / 6) == 0 && x / (l.getWidth() / 8) < equippedItems.length
					&& x / (l.getWidth() / 8) >= 0) {
				select = equippedItems[x / (l.getWidth() / 8)];
			} else if ((y / (l.getHeight() / 6) - 1) * 8 + x / (l.getWidth() / 8) < items.length
					&& (y / (l.getHeight() / 6) - 1) * 8 + x / (l.getWidth() / 8) >= 0) {
				select = items[(y / (l.getHeight() / 6) - 1) * 8 + x / (l.getWidth() / 8)];
			}
		} else if ((x - (int) (1.5 * l.getWidth() / 4) < 4 * l.getHeight() / 10)
				&& (x - (int) (1.5 * l.getWidth() / 4) > 0)
				&& ((y - (int) 8.5 * l.getHeight() / 10) < l.getHeight() / 10)
				&& ((y - (int) 8.5 * l.getHeight() / 10) > 0)) {
			l.addEntity(new SkillDisplayEntity(
					skills.get((x - (int) (1.5 * l.getWidth() / 4)) / (l.getHeight() / 10)).getInfo(),
					l.getHeight() / 50, l.getHeight() / 10,
					(1.5 * l.getWidth() / 4)
							+ l.getHeight() / 10 * ((int) (x - (int) (1.5 * l.getWidth() / 4)) / (l.getHeight() / 10)),
					(8.5 * l.getHeight() / 10), Color.BLUE,
					skills.get((x - (int) (1.5 * l.getWidth() / 4)) / (l.getHeight() / 10)), l));
			skills.get((x - (int) (1.5 * l.getWidth() / 4)) / (l.getHeight() / 10)).setDisplay(true);
			try {
				Thread.sleep(l.getRefreshTime() * 2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			l.setPause(true);
		}
	}

	public void mouseRelease(int x, int y, Level l) {
		if (this.inven && select != null) {
			if (y / (l.getHeight() / 6) == 0 && x / (l.getWidth() / 8) < equippedItems.length
					&& x / (l.getWidth() / 8) >= 0) {
				if (equippedItems[x / (l.getWidth() / 8)] != select) {
					this.addItem(equippedItems[x / (l.getWidth() / 8)]);
					equippedItems[x / (l.getWidth() / 8)] = select;
					this.removeItem(select);
				}
			} else if ((y / (l.getHeight() / 6) - 1) * 8 + x / (l.getWidth() / 8) < items.length
					&& (y / (l.getHeight() / 6) - 1) * 8 + x / (l.getWidth() / 8) >= 0) {
				if (items[(y / (l.getHeight() / 6) - 1) * 8 + x / (l.getWidth() / 8)] != select) {
					Item i = items[(y / (l.getHeight() / 6) - 1) * 8 + x / (l.getWidth() / 8)];
					this.removeItem(select);
					items[(y / (l.getHeight() / 6) - 1) * 8 + x / (l.getWidth() / 8)] = select;
					this.addItem(i);
				}
			} else if (y / (l.getHeight() / 6) == 0 && x / (l.getWidth() / 8) == 7) {
				this.removeItem(select);
			}
			select = null;
		} else if (!this.inven) {
			l.setPause(false);
			for (Skill s : skills) {
				s.setDisplay(false);
			}
		}
	}

	public double getSpeed() {
		return Player.BASE_SPEED * (1 + speedBoostPercent / 100) + speedBoostFlat;
	}

	public Player increaseSpeedPercent(double percent) {
		speedBoostPercent += percent;
		return this;
	}

	public Player decreaseSpeedPercent(double percent) {
		speedBoostPercent -= percent;
		return this;
	}

	public Player increaseSpeedFlat(double speed) {
		speedBoostFlat += speed;
		return this;
	}

	public Player decreaseSpeedFlat(double speed) {
		speedBoostFlat -= speed;
		return this;
	}

	public Player increaseDefense(double percent) {
		defenseBoost += percent;
		return this;
	}

	public Player decreaseDefense(double percent) {
		defenseBoost -= percent;
		return this;
	}

	public ArrayList<Skill> getSkills() {
		return skills;
	}

	public Skill getBasicSkill() {
		return basic;
	}

	@Override
	public int getCollision() {
		return Entity.STOP_ON_HIT;
	}

	public Item[] getEquipped() {
		return equippedItems;
	}

	public Item[] getInventory() {
		return items;
	}
}
