import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Text {
	public static void main(String[] args) {
		if(args.length > 0) {
			for(String s : args) {
				render(s);
			}
		} else {
			Scanner s = new Scanner(System.in);
			render(s.nextLine());
		}
	}
	public static final void render(String s) {
		int length = s.length();
		int charsPerLine = (int) Math.ceil(Math.sqrt(length));
		int lines = (int) Math.ceil(((double) length)/charsPerLine);
		int FONT_SIZE = 160;
		Font font = new Font("Consolas", Font.BOLD, FONT_SIZE);
		render(s, charsPerLine, lines, font);
	}
	public static final void render(String s, int charsPerLine, int lines, Font font) {
		int fontSize = font.getSize();
		int width = fontSize * charsPerLine;
		int height = fontSize * lines;
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = result.getGraphics();
		g.setFont(font);
		
		renderText(g, charsPerLine, lines, s);
		
		int corruptionLevel = 20;
		for(int i = 0; i < corruptionLevel; i++) {
			double magnitude = 3;
			double magnitudeSquared = magnitude * magnitude;
			//double x = ((Math.pow(Math.random() * magnitude, 2) + 1) / magnitude);
			//double y = ((Math.pow(Math.random() * magnitude, 2) + 1) / magnitude);
			
			//double x = (Math.pow(Math.pow(Math.random(), 2) * magnitude, 2) + 1) / magnitude;
			//double y = (Math.pow(Math.pow(Math.random(), 2) * magnitude, 2) + 1) / magnitude;
			
			double x = (Math.pow(Math.random(), 2) * magnitudeSquared + 1) / magnitude;
			double y = (Math.pow(Math.random(), 2) * magnitudeSquared + 1) / magnitude;
			
			x = Math.min(x, 10000 / result.getWidth());
			y = Math.min(y, 10000 / result.getHeight());
			//System.out.println("X: " + x + "\nY: " + y);
			result = scale(result, x, y);
			result = noise(result, (int) (20 * x * y));
			//System.out.println("W: " + result.getWidth() + "\nH: " + result.getHeight());
		}
		result = scale(result, 1f * width / result.getWidth(), 1f * height / result.getHeight());
		result = noise(result, 100);
		try {
			ImageIO.write(result, "png", new File("./" + new Date().getTime() + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static final BufferedImage noise(BufferedImage image, int times) {
		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = result.getGraphics();
		g.drawImage(image, 0, 0, null);
		for(int i = 0; i < times; i++) {
			int x = (int) (Math.random() * image.getWidth());
			int y = (int) (Math.random() * image.getHeight());
			Color c = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), 51);
			g.setColor(c);
			g.fillRect(x, y, (int) (Math.random() * 16), (int) (Math.random() * 16));
		}
		return result;
	}
	public static final BufferedImage scale(BufferedImage image, double x, double y) {
		BufferedImage result = new BufferedImage((int) (image.getWidth() * x), (int) (image.getHeight() * y), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = result.createGraphics();
		g.scale(x, y);
		g.drawImage(image, 0, 0, null);
		return result;
	}
	public static final void renderText(Graphics g, int charsPerLine, int lines, String s) {
		/*
		int x = FONT_SIZE / 4;
		int y = FONT_SIZE * 4 / 5;
		*/
		int fontSize = g.getFont().getSize();
		int x = 0;
		int y = fontSize;
		for(int i = 0; i < lines - 1; i++) {
			int index_start = i * charsPerLine;
			drawRandomLine(g, s.substring(index_start, index_start + charsPerLine), x, y);
			y += fontSize;
		}
		drawRandomLine(g, s.substring((lines - 1) * charsPerLine), x, y);
	}
	public static final Color[] colors = {
			Color.BLACK, Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW
	};
	public static void drawRandomLine(Graphics g, String line, int x, int y) {
		int size = g.getFont().getSize();
		for(int i = 0; i < line.length(); i++) {
			int c = (int) (Math.random() * colors.length);
			ArrayList<Color> options = new ArrayList<Color>(Arrays.asList(colors));
			Color c1 = options.remove((int) (Math.random() * options.size()));
			Color c2 = options.remove((int) (Math.random() * options.size()));
			c1 = new Color(c1.getRed(), c1.getGreen(), c1.getBlue(), 153);
			c2 = new Color(c2.getRed(), c2.getGreen(), c2.getBlue(), 51);
			
			g.setColor(c2);
			g.fillRect(x, y - size, size, size);
			
			g.setColor(c1);
			g.drawString("" + line.charAt(i), x + size/4, y - size/5);
			x += size;
		}
	}
}
