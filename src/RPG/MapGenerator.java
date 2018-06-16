package RPG;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MapGenerator {

	public static final Rectangle[][] obstacles = new Rectangle[][] {
			{}, { new Rectangle(67, 68, 190 - 67, 187 - 68) }, {}, {}, { new Rectangle(141, 76, 167 - 141, 102 - 76),
					new Rectangle(41, 166, 67 - 41, 194 - 166), new Rectangle(214, 218, 240 - 214, 240 - 218) },
			{}, { new Rectangle(32, 37, 72 - 32, 141 - 37) },
			{ new Rectangle(21, 26, 61 - 21, 70 - 26), new Rectangle(179, 28, 223 - 179, 70 - 28),
					new Rectangle(189, 181, 236 - 189, 226 - 181), new Rectangle(12, 182, 56 - 12, 224 - 182) },
			{}, {}, {}, { new Rectangle(23, 18, 54 - 23, 96 - 18), new Rectangle(198, 188, 240 - 198, 232 - 188) },
			{ new Rectangle(135, 108, 211 - 135, 200 - 108) }, { new Rectangle(35, 45, 71 - 35, 85 - 45) },
			{ new Rectangle(62, 86, 85 - 62, 105 - 86), new Rectangle(156, 43, 179 - 156, 66 - 43) },
			{ new Rectangle(24, 25, 109 - 24, 110 - 25) } };
	private byte[][] quadrants;
	int size;
	// int finalBossCorner = (int) (Math.random() * 4);

	public MapGenerator() {
		size = 128;
		quadrants = new byte[128][128];
		for (int i = 0; i < 128; i++) {
			for (int j = 0; j < 128; j++) {
				quadrants[i][j] = (byte) (Math.random() * 16);
			}
		}
	}

	public MapGenerator(int sideLength) {
		size = sideLength;
		quadrants = new byte[sideLength][sideLength];
		for (int i = 0; i < sideLength; i++) {
			for (int j = 0; j < sideLength; j++) {
				quadrants[i][j] = (byte) (Math.random() * 16);
			}
		}
	}

	public BufferedImage getTempImage(int x, int y, int width, int height) {
		int offsetx = x % 256;
		int offsety = y % 256;
		int basequadx = x / 256;
		int basequady = y / 256;
		int drawx = width / 256 + 2;
		int drawy = height / 256 + 2;
		BufferedImage tempImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = tempImage.getGraphics();

		for (int i = -2; i < drawy; i++) {
			for (int j = -drawx; j < 2; j++) {
				if (basequadx - drawx / 2 + j < size && basequadx - drawx / 2 + j >= 0
						&& basequady + drawy / 2 - i < size && basequady + drawy / 2 - i >= 0) {
					g.drawImage(ImageLoader.images[quadrants[basequadx - drawx / 2 + j][basequady + drawy / 2 - i]],
							(-256 * j + offsetx), 256 * i + offsety, null);
				}
			}
		}
		return tempImage;
	}

	public ArrayList<Rectangle> getObstacles(int x, int y, int width, int height, Level l) {
		ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
		int basequadx = x / 256;
		int basequady = y / 256;
		int offsetx = x % 256;
		int offsety = y % 256;
		int drawx = width / 256 + 2;
		int drawy = height / 256 + 2;
		for (int i = -2; i < drawy; i++) {
			for (int j = -drawx; j < 2; j++) {
				if (basequadx - drawx / 2 + j < size && basequadx - drawx / 2 + j >= 0
						&& basequady + drawy / 2 - i < size && basequady + drawy / 2 - i >= 0) {
					for (Rectangle r : obstacles[quadrants[basequadx - drawx / 2 + j][basequady + drawy / 2 - i]]) {
						rects.add(
								new Rectangle((int) (r.x - 256 * j + offsetx + l.getPlayer().getX() - l.getWidth() / 2),
										(int) (r.y + 256 * i + offsety + l.getPlayer().getY() - l.getHeight() / 2),
										r.width, r.height));
					}
				}
			}
		}
		return rects;
	}
}