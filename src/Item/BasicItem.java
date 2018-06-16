package Item;

import java.awt.image.BufferedImage;

import RPG.ImageLoader;
import RPG.Utils;

public class BasicItem extends Item {

	private int level, image;

	private double attackRoll, defRoll;

	public BasicItem(int level, int image, double baseAttackMax, double attackGrowthMax, double baseDefenseMax,
			double defenseGrowthMax) {
		this.level = level;
		this.image = image;
		this.attackRoll = Utils.calcItemValue(level, baseAttackMax, attackGrowthMax);
		this.defRoll = Utils.calcItemValue(level, baseDefenseMax, defenseGrowthMax);
	}

	@Override
	public BufferedImage getImage() {
		return ImageLoader.getItemImage(image);
	}

	@Override
	public int getNum() {
		return 1;
	}

	@Override
	public int getValue() {
		return 0;
	}

	@Override
	public double getAttack() {
		return attackRoll;
	}

	@Override
	public double getDefense() {
		return defRoll;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public String getDescription() {
		return String.format("Level %d item, Grants %.1f attack, and %.1f defense.", this.getLevel(), this.getAttack(),
				this.getDefense());
	}

}
