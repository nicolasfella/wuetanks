package de.uniwuerzburg.battletanks.utility;

import com.badlogic.gdx.files.FileHandle;

public class FileListItem {
	final FileHandle file;

	final String name;

	/**
	 * Creates a FileListItem which can be used for listing files. The name is
	 * taken from the FileHandle.
	 * 
	 * @param file
	 */

	public FileListItem(FileHandle file) {
		this(file.name(), file);
	}

	/**
	 * Creates a FileListItem which can be used for listing files.
	 * 
	 * @param name
	 *            of the file
	 * @param file
	 */

	public FileListItem(String name, FileHandle file) {
		if (file.isDirectory()) {
			name += "/";
		}
		this.name = name;
		this.file = file;
	}

	@Override
	public String toString() {
		return name;
	}
}
