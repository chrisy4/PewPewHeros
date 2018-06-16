package RPG;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ImageLoader {

	public static final int GUNSLINGER_IMAGE = 0, ARCHER_IMAGE = 1, WIZARD_IMAGE = 2, WARLOCK_IMAGE = 3;

	public static final int DRAIN = 0, MORTAL_BLAST = 1, DEMONIC_OFFERING = 2, SOUL_SHACKLE = 3, DeathInABottle = 4,
			TimeCrasher = 5, DEATH_CLOUD = 6, BlindingFog = 7, BLOODLUST = 8, UNDYING_RAGE = 9, TANK_UP = 10,
			GROUND_POUND = 11, TELEPORT = 12, ENERGIZE = 13, LONGSHOT = 14, SWIFT_FEET = 15;

	public static final int BASIC_DROP = 0, BAILEY_DROP = 1, TRASH = 2;

	private static ArrayList<BufferedImage> playerImages = new ArrayList<BufferedImage>(),
			enemyImages = new ArrayList<BufferedImage>(), skillImages = new ArrayList<BufferedImage>(),
			bossImages = new ArrayList<BufferedImage>(), itemImages = new ArrayList<BufferedImage>();

	public static BufferedImage[] images = new BufferedImage[16];

	private static BufferedImage title;

	public static void loadImages() throws IOException {
		title = ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Title.png"));

		playerImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Player0.png")));
		playerImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Player1.png")));
		playerImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Player2.png")));
		playerImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Player3.png")));

		enemyImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Enemy0.png")));
		enemyImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Enemy1.png")));
		
		bossImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Boss0.png")));

		skillImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Drain.png")));
		skillImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/MortalBlast.png")));
		skillImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/DemonicOffering.png")));
		skillImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/SoulShackle.png")));
		skillImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/DeathInABottle.png")));
		skillImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/TimeCrasher.png")));
		skillImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/DeathCloud.png")));
		skillImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/BlindingFog.png")));
		skillImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Bloodlust.png")));
		skillImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/UndyingRage.png")));
		skillImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/TankUp.png")));
		skillImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/GroundPound.png")));
		skillImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Teleport.png")));
		skillImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Energize.png")));
		skillImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Longshot.png")));
		skillImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/SwiftFeet.png")));

		itemImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Item0.png")));
		itemImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Item1.png")));
		itemImages.add(ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Trash.png")));

		images[0] = ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Map1.png"));
		images[1] = ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Map2.png"));
		images[2] = ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Map3.png"));
		images[3] = ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Map4.png"));
		images[4] = ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Map5.png"));
		images[5] = ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Map6.png"));
		images[6] = ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Map7.png"));
		images[7] = ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Map8.png"));
		images[8] = ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Map9.png"));
		images[9] = ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Map10.png"));
		images[10] = ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Map11.png"));
		images[11] = ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Map12.png"));
		images[12] = ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Map13.png"));
		images[13] = ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Map14.png"));
		images[14] = ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Map15.png"));
		images[15] = ImageIO.read(ImageLoader.class.getResourceAsStream("/Images/Map16.png"));
	}

	public static BufferedImage getPlayerImage(int i) {
		return playerImages.get(i);
	}

	public static BufferedImage getEnemyImage(int i) {
		return enemyImages.get(i);
	}

	public static BufferedImage getSkillImage(int i) {
		return skillImages.get(i);
	}

	public static BufferedImage getBossImage(int i) {
		return bossImages.get(i);
	}

	public static BufferedImage getItemImage(int i) {
		return itemImages.get(i);
	}

	public static BufferedImage getTitle() {
		return title;
	}
}
