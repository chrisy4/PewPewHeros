package RPG;

import java.awt.Image;

import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Entity.Player;

public class Main extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1436105148555236606L;

	public Main() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		ImageLoader.loadImages();
		AudioInputStream audioInputStream = AudioSystem
				.getAudioInputStream(getClass().getResource("/Sound/background.wav"));
		Clip clip = AudioSystem.getClip();
		clip.open(audioInputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		ImageIcon[] buttons = new ImageIcon[] {
				new ImageIcon(ImageLoader.getPlayerImage(0).getScaledInstance(60, 60, Image.SCALE_SMOOTH)),
				new ImageIcon(ImageLoader.getPlayerImage(1).getScaledInstance(60, 60, Image.SCALE_SMOOTH)),
				new ImageIcon(ImageLoader.getPlayerImage(2).getScaledInstance(60, 60, Image.SCALE_SMOOTH)),
				new ImageIcon(ImageLoader.getPlayerImage(3).getScaledInstance(60, 60, Image.SCALE_SMOOTH)) };
		Spawner spawns = new Spawner();
		spawns.registerSpawn(Spawner.BASIC, 6, 50000, 500, 500, 50000, Spawner.LEVEL_PER_SPAWN);
		spawns.registerSpawn(Spawner.POUNCE, 6, 50000, 500, 500, 25000, Spawner.LEVEL_PER_SPAWN);
		spawns.registerSpawn(Spawner.BAILEY, 1, 110000, 500, 500, 0, Spawner.LEVEL_PER_SPAWN);
		Level l = new Level(this, Player.getDefualtPlayer(JOptionPane.showOptionDialog(null, "Please select character",
				"Character Select", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0])),
				20, spawns);
		this.setContentPane(l);
		this.addKeyListener(l);
		this.addMouseListener(l);
		Thread t = new Thread(l);
		t.start();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 800);
		setVisible(true);
		this.setIconImage(ImageLoader.getTitle());
	}

	public static void main(String[] args) {
		try {
			new Main();
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}
