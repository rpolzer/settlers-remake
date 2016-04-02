package jsettlers.main.swing.menu.joingame.slots;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import jsettlers.graphics.localization.Labels;
import jsettlers.logic.map.MapLoader;
import jsettlers.logic.player.PlayerSetting;
import jsettlers.main.swing.lookandfeel.ELFStyle;
import jsettlers.main.swing.lookandfeel.ui.ScrollbarUiButton;
import jsettlers.main.swing.lookandfeel.ui.UIDefaults;

/**
 * This class is the model backing the player table.
 * 
 * @author Michael Zangl
 */
public class PlayerTable extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static final PlayerTableColumn[] COLUMNS = new PlayerTableColumn[] {
			new PlayerTableColumn("") {
				@Override
				public Object getFor(PlayerSlot playerSlot) {
					return playerSlot.isReady();
				}

				@Override
				public boolean isEditable(PlayerSlot playerSlot) {
					return playerSlot.isReadyButtonChangeable();
				};
			},
			new PlayerTableColumn("join-game-panel-player-name") {
				@Override
				public Object getFor(PlayerSlot playerSlot) {
					return playerSlot.getPlayerName();
				}
			}, new PlayerTableColumn("join-game-panel-civilisation") {
				@Override
				public Object getFor(PlayerSlot playerSlot) {
					return playerSlot.getCivilisation();
				}
			},
			new PlayerTableColumn("join-game-panel-player-type") {
				@Override
				public Object getFor(PlayerSlot playerSlot) {
					return playerSlot.getSelectedType();
				}
			},
			new PlayerTableColumn("join-game-panel-map-slot"),
			new PlayerTableColumn("join-game-panel-team") {
				@Override
				public Object getFor(PlayerSlot playerSlot) {
					return playerSlot.getTeam();
				}
			} };
	public static final TableCellRenderer TEXT_CELL_RENDERER = new SimpleTextRenderer();
	public static final TableCellRenderer DROPDOWN_CELL_RENDERER = new DropdownTextRenderer();

	private static class PlayerTableColumn extends TableColumn {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String columnName;

		public PlayerTableColumn(String name) {
			columnName = Labels.getString(name);
		}

		public Object getFor(PlayerSlot playerSlot) {
			return null;
		}

		public boolean isEditable(PlayerSlot playerSlot) {
			return false;
		}

		@Override
		public TableCellRenderer getCellRenderer() {
			return DROPDOWN_CELL_RENDERER;
		}
	}

	private final class PlayerTableModel extends AbstractTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private List<PlayerSlot> slots;

		public PlayerTableModel(List<PlayerSlot> slots) {
			this.slots = slots;
		}

		public void setSlots(List<PlayerSlot> slots) {
			this.slots = slots;
			fireTableDataChanged();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return COLUMNS[columnIndex].getFor(slots.get(rowIndex));
		}

		@Override
		public int getRowCount() {
			return slots.size();
		}

		@Override
		public int getColumnCount() {
			return COLUMNS.length;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return COLUMNS[columnIndex].isEditable(slots.get(rowIndex));
		}

		@Override
		public String getColumnName(int columnIndex) {
			return COLUMNS[columnIndex].columnName;
		}
	}

	private static class SimpleTextRenderer extends JLabel implements TableCellRenderer {

		SimpleTextRenderer() {
			putClientProperty(ELFStyle.KEY, ELFStyle.LABEL_DYNAMIC);
			setOpaque(false);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			setText(value + "");
			return this;
		}
	}

	private static class DropdownTextRenderer extends JPanel implements TableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Font color
		 */
		private final Color FOREGROUND = UIManager.getColor("MapListCellRenderer.foregroundColor");
		private final ScrollbarUiButton dropdown = new ScrollbarUiButton(BasicArrowButton.SOUTH, UIDefaults.ARROW_COLOR);;
		private final JLabel label = new JLabel();

		DropdownTextRenderer() {
			super(new BorderLayout());
			add(label);
			add(dropdown, BorderLayout.LINE_END);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			dropdown.setVisible(table.getModel().isCellEditable(row, column));
			label.setText(value + "");
			label.setForeground(FOREGROUND);
			invalidate();
			return this;
		}
	}

	private final List<PlayerSlot> allSlots;
	private final PlayerTableModel model;

	public PlayerTable(List<PlayerSlot> allSlots) {
		this.allSlots = allSlots;
		model = new PlayerTableModel(allSlots);
		setModel(model);
		for (int i = 0; i < COLUMNS.length; i++) {
			PlayerTableColumn c = COLUMNS[i];
			c.setModelIndex(i);
			addColumn(c);
		}

		// Layout properties
		setFillsViewportHeight(true);
		setRowSelectionAllowed(false);
		setOpaque(false);
		for (int i = 0; i < getColumnCount(); i++) {
			TableColumn column = getColumnModel().getColumn(i);
			column.setPreferredWidth(100);
		}
		setRowHeight(25);
		getTableHeader().setReorderingAllowed(false);
	}

	@Override
	public void createDefaultColumnsFromModel() {
	}

	public void setPlayerCount(int count) {
		model.setSlots(allSlots.subList(0, count));
	}

	public PlayerSetting[] getPlayerSettings() {
		int playersMissing = allSlots.size() - model.slots.size();
		return Stream.concat(model.slots.stream()
				.map(playerSlot -> new PlayerSetting(true, playerSlot.getSelectedType(), playerSlot.getCivilisation().getCivilisation(),
						playerSlot.getTeam())),
				Stream.of(new Object[playersMissing]).map(o -> new PlayerSetting(false, (byte) 0)))
				.toArray(size -> new PlayerSetting[size]);
	}

	public byte getActivePlayerIndex() {
		return 0;
	}

	public static PlayerTable create(MapLoader mapLoader, PlayerSlotFactory playerSlotFactory) {
		ArrayList<PlayerSlot> allSlots = new ArrayList<>();
		for (int i = 0; i < mapLoader.getMaxPlayers(); i++) {
			allSlots.add(playerSlotFactory.createPlayerSlot((byte) i, mapLoader));
		}
		return new PlayerTable(allSlots);
	}
}
