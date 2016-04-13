/*******************************************************************************
 * Copyright (c) 2015, 2016
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package jsettlers.graphics.map.draw.settlerimages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jsettlers.common.images.EImageLinkType;
import jsettlers.common.images.OriginalImageLink;
import jsettlers.common.material.EMaterialType;
import jsettlers.common.movable.EDirection;
import jsettlers.common.movable.EMovableAction;
import jsettlers.common.movable.EMovableType;
import jsettlers.common.movable.IMovable;
import jsettlers.graphics.image.Image;
import jsettlers.graphics.map.draw.ImageProvider;

/**
 * This is a settler image map that mapps the state of a settler to the sequence that is to be played.
 * <p>
 * The mapping is a function: (type, material, direction) => (file, sequence index, start, duration)
 * 
 * @author michael
 */
public class SettlerImageMap {

	private static final OriginalImageLink DEFAULT_ITEM = new OriginalImageLink(EImageLinkType.SETTLER, 10, 0, 0);

	private static SettlerImageMap instance;

	/**
	 * This is a map.
	 * Argument 0: Movable Type
	 * Argument 1: Movable Action
	 * Argument 2: Material type
	 * Argument 3: Direction
	 */
	private final OriginalImageLink[][][][] map;

	private final int types;

	private final int actions;

	private final int materials;

	private final int directions;

	/**
	 * Creates a new settler image map.
	 */
	SettlerImageMap() {
		this.types = EMovableType.NUMBER_OF_MOVABLETYPES;
		this.actions = EMovableAction.values().length;
		this.materials = EMaterialType.NUMBER_OF_MATERIALS;
		this.directions = EDirection.VALUES.length;
		this.map = new OriginalImageLink[this.types][this.actions][][];

		try {
			InputStream file = getClass().getResourceAsStream("movables.txt");
			ArrayList<ImageLine> lines = readFromFile(file);

			for (ImageLine line : lines) {
				addLine(line);
			}

		} catch (IOException e) {
			onIOException(e);
		}
	}

	protected void onIOException(IOException e) {
		System.err.println("Error reading image file: " + e.getMessage()
				+ "; Settler images might not work.");
	}

	private void addLine(final ImageLine line) {
		OriginalImageLink[][][][] typeArray = map;
		OriginalImageLink[][][] actionArray = getOrCreateArray(line.typeIndex, map, new EntryProducer<OriginalImageLink[][][]>() {
			@Override
			public OriginalImageLink[][][] newEntry() {
				return new OriginalImageLink[actions][][];
			}
		});
		OriginalImageLink[][] materialArray = getOrCreateArray(line.actionIndex, actionArray, new EntryProducer<OriginalImageLink[][]>() {
			@Override
			public OriginalImageLink[][] newEntry() {
				return new OriginalImageLink[materials][];
			}
		});
		OriginalImageLink[] directionArray = getOrCreateArray(line.materialIndex, materialArray, new EntryProducer<OriginalImageLink[]>() {
			@Override
			public OriginalImageLink[] newEntry() {
				return new OriginalImageLink[directions];
			}
		});
		getOrCreateArray(line.directionIndex, directionArray, new EntryProducer<OriginalImageLink>() {
			@Override
			public OriginalImageLink newEntry() {
				return line.image;
			}
		});
	}

	private <T> T getOrCreateArray(int lineIndex, T[] baseArray, EntryProducer<T> arrayProducer) {
		T result;
		if (lineIndex < 0) {
			// we can be sure that all actions before us had line.actionIndex < 0
			// all objects in typeArray are equal.
			result = baseArray[0];
			if (result == null) {
				result = arrayProducer.newEntry();
				Arrays.fill(baseArray, result);
			}
		} else {
			result = baseArray[lineIndex];
			if (result == null) {
				result = arrayProducer.newEntry();
				baseArray[lineIndex] = result;
			}
		}
		return result;
	}

	private interface EntryProducer<T> {
		T newEntry();
	}

	/**
	 * Reads the map from the given file.
	 * 
	 * @param file
	 *            The file to read from.
	 *            @return A list of lines.
	 */
	private ArrayList<ImageLine> readFromFile(InputStream file)
			throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(file));

		ArrayList<ImageLine> lines = new ArrayList<>();
		String line = reader.readLine();
		while (line != null) {
			if (!line.isEmpty() && !line.startsWith("#")) {
				try {
					lines.add(new ImageLine(line));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}

			line = reader.readLine();
		}
		Collections.sort(lines);

		return lines;
	}

	/**
	 * Gets an image for a given settler.
	 * 
	 * @param movable
	 *            The settler to get the image for
	 * @return The image or an null-image.
	 * @see SettlerImageMap#getImageForSettler(EMovableType, EMovableAction, EMaterialType, EDirection, float)
	 */
	public Image getImageForSettler(IMovable movable, float progress) {
		if (movable.getAction() == EMovableAction.WALKING) {
			progress = progress / 2;
			if (movable.isRightstep()) {
				progress += .5f;
			}
		}
		return getImageForSettler(movable.getMovableType(),
				movable.getAction(), movable.getMaterial(),
				movable.getDirection(), progress);
	}

	/**
	 * Gets an image for a given settler.
	 * 
	 * @param movableType
	 *            The type of the settler.
	 * @param action
	 *            The action the settler is doing.
	 * @param material
	 *            The material that is assigned to the settler.
	 * @param direction
	 *            Its direction.
	 * @param progress
	 *            The progress.
	 * @return The image.
	 */
	public Image getImageForSettler(EMovableType movableType, EMovableAction action,
			EMaterialType material, EDirection direction, float progress) {
		OriginalImageLink item = getMapItem(movableType, action, material, direction);

		int duration = item.getLength();
		int imageIndex;
		if (duration >= 0) {
			imageIndex = Math.min((int) (progress * duration), duration - 1);
		} else {
			imageIndex = Math.max((int) (progress * duration), duration + 1);
		}
		return ImageProvider.getInstance().getSequencedImage(item, imageIndex);
	}

	/**
	 * Gets a map item.
	 * 
	 * @param movableType
	 * @param action
	 * @param material
	 * @param direction
	 * @return The item of the map at the given position. Is not null.
	 */
	private OriginalImageLink getMapItem(EMovableType movableType,
			EMovableAction action, EMaterialType material, EDirection direction) {
		OriginalImageLink item = this.map[movableType.ordinal()][action.ordinal()][material
				.ordinal()][direction.ordinal()];
		if (item == null) {
			return DEFAULT_ITEM;
		} else {
			return item;
		}
	}

	public static SettlerImageMap getInstance() {
		if (instance == null) {
			instance = new SettlerImageMap();
		}
		return instance;
	}
	private static class ImageLine implements  Comparable<ImageLine> {
		private final int typeIndex;
		private final int actionIndex;
		private final int directionIndex;
		private final int materialIndex;
		private final OriginalImageLink image;

		ImageLine(String line) throws IOException {
			Matcher matcher = matchLine(line);
			typeIndex = convertEnum(matcher.group(1), EMovableType.class);
			if (typeIndex < 0) {
				throw new IOException("Cannot have generic type: " + line);
			}
			actionIndex = convertEnum(matcher.group(2), EMovableAction.class);
			materialIndex = convertEnum(matcher.group(3), EMaterialType.class);
			directionIndex = convertEnum(matcher.group(4), EDirection.class);

			final int fileIndex = Integer.parseInt(matcher.group(5));
			final int sequence = Integer.parseInt(matcher.group(6));
			final int start = Integer.parseInt(matcher.group(7));
			final int duration = Integer.parseInt(matcher.group(8));

			image = new OriginalImageLink(EImageLinkType.SETTLER, fileIndex, sequence, start, duration);
		}

		/**
		 * Parses a line.
		 *
		 * @param line
		 *            The line.
		 * @return The line matched against the line pattern.
		 * @throws IllegalArgumentException
		 *             if the line is not correct.
		 */
		private Matcher matchLine(String line) {
			Pattern linePattern = Pattern.compile("\\s*([\\w\\*]+)\\s*,"
					+ "\\s*([\\w\\*]+)\\s*," + "\\s*([\\w\\*]+)\\s*,"
					+ "\\s*([\\w\\*]+)\\s*" + "=\\s*(\\d+)\\s*," + "\\s*(\\d+)\\s*,"
					+ "\\s*(\\d+)\\s*," + "\\s*(-?\\d+)\\s*");
			final Matcher matcher = linePattern.matcher(line);
			final boolean matches = matcher.matches();
			if (!matches) {
				throw new IllegalArgumentException("Invalid line syntax: " + line); // ignore
			}
			return matcher;
		}

		private int convertEnum(String token, Class<? extends  Enum> clazz) {
			if ("*".equals(token)) {
				return -1;
			} else {
				return Enum.valueOf(clazz, token).ordinal();
			}
		}

		@Override
		public int compareTo(ImageLine o) {
			int c = Integer.compare(directionIndex, o.directionIndex);
			if (c == 0) {
				c = Integer.compare(materialIndex, o.materialIndex);
			}
			if (c == 0) {
				c = Integer.compare(actionIndex, o.actionIndex);
			}
			if (c == 0) {
				c = Integer.compare(typeIndex, o.typeIndex);
			}
			return c;
		}
	}
}
