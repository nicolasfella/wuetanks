package de.uniwuerzburg.battletanks.utility;

import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class FileChooser extends Dialog {

	public interface ResultListener {
		boolean result(boolean success, FileHandle result);
	}

	private List<FileListItem> fileList;
	private TextButton loadButton;
	private TextButton cancelButton;
	private ScrollPane scrollPane;
	private String result;
	private Skin skin;
	private FileHandle currentDir;
	private FileHandle baseDir;
	private Label currentDirLabel;
	private FileFilter filter = new FileFilter() {
		@Override
		public boolean accept(File pathname) {
			String path = pathname.getPath();
			return path.matches(".*\\.tmx");
		}
	};
	public ResultListener resultListener = null;

	// comparator für die korrekte sortierung der files

	private Comparator<FileListItem> fileListComp = new Comparator<FileListItem>() {
		@Override
		public int compare(FileListItem file1, FileListItem file2) {
			if (file1.file.isDirectory() && !file2.file.isDirectory()) {
				return -1;
			}
			if (file1.file.isDirectory() && file2.file.isDirectory()) {
				return 0;
			}
			if (!file1.file.isDirectory() && !file2.file.isDirectory()) {
				return 0;
			}
			return 1;
		}
	};

	public FileChooser(String title, Skin skin, FileHandle baseDir) {
		super(title, skin);

		this.skin = skin;
		this.baseDir = baseDir;

		Table table = getContentTable();
		table.top().left();

		currentDirLabel = new Label(baseDir.path(), skin);
		currentDirLabel.setAlignment(Align.left);

		fileList = new List<FileListItem>(skin);
		fileList.getSelection().setProgrammaticChangeEvents(false);

		loadButton = new TextButton("Load", skin);
		button(loadButton, true);

		cancelButton = new TextButton("Cancel", skin);
		button(cancelButton, false);
		key(Keys.ENTER, true);
		key(Keys.ESCAPE, false);

		fileList.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				FileListItem selected = fileList.getSelected();
				if (!selected.file.isDirectory()) {
					result = selected.file.name();
				}
			}
		});

	}

	/**
	 * wechselt auf das übergebene directory und aktualisiert die fileList
	 *
	 */

	private void changeDirectory(FileHandle directory) {

		currentDir = directory;
		currentDirLabel.setText(currentDir.path());

		Array<FileListItem> items = new Array<FileListItem>();

		// hinzufügen aller directories

		FileHandle[] list = directory.list();

		for (FileHandle handle : list) {
			if (handle.isDirectory()) {
				items.add(new FileListItem(handle));
			}
		}

		// hinzufügen aller dateien auf die der filter passt

		list = directory.list(filter);

		for (FileHandle handle : list) {
			items.add(new FileListItem(handle));
		}

		items.sort(fileListComp);

		if (directory.file().getParentFile() != null) {
			items.insert(0, new FileListItem("..", directory.parent()));
		}

		fileList.setSelected(null);
		fileList.setItems(items);
	}

	/**
	 * erstellt aus dem aktuellen directory pfad und der markierten datei ein
	 * FileHandle
	 * 
	 * @return
	 */

	public FileHandle getResult() {
		String path = currentDir.path() + "/";
		if (result != null && result.length() > 0) {
			path += result;
		}
		return new FileHandle(path);
	}

	@Override
	public Dialog show(Stage stage, Action action) {
		Table content = getContentTable();
		content.add(currentDirLabel).top().left().expandX().fillX().row();

		scrollPane = new ScrollPane(fileList, skin);
		scrollPane.setFadeScrollBars(false);
		content.add(scrollPane).size(500, 400).fill().expand().row();

		fileList.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				FileListItem selected = fileList.getSelected();
				if (selected.file.isDirectory()) {
					changeDirectory(selected.file);
				}
			}
		});

		changeDirectory(baseDir);

		stage.setScrollFocus(scrollPane);
		stage.setKeyboardFocus(scrollPane);

		return super.show(stage, action);
	}

	public static FileChooser createDialog(String title, final Skin skin, final FileHandle path) {
		FileChooser fileChooser = new FileChooser(title, skin, path) {
			@Override
			protected void result(Object object) {

				if (resultListener == null) {
					return;
				}

				final boolean success = (Boolean) object;
				resultListener.result(success, getResult());
			}
		};

		return fileChooser;

	}

	public FileChooser setResultListener(ResultListener result) {
		this.resultListener = result;
		return this;
	}

}
