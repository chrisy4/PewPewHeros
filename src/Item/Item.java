package Item;

import java.awt.image.BufferedImage;

public abstract class Item {
	public abstract BufferedImage getImage();

	public abstract int getNum();

	public abstract int getValue();

	public abstract double getAttack();

	public abstract double getDefense();
	
	public abstract int getLevel();
	
	public abstract String getDescription();
}
