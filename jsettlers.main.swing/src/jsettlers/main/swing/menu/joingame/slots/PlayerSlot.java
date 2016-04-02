package jsettlers.main.swing.menu.joingame.slots;

import java.util.Arrays;

import jsettlers.common.ai.EPlayerType;
import jsettlers.main.swing.menu.joingame.CivilisationUiWrapper;

/**
 * A player slot. Contains information about the settings to display.
 * <p>
 * There are some flags that control which aspects of this slot are editable by the current user.
 * 
 * @author Michael Zangl
 */
public class PlayerSlot {

	private String playerName;
	private EPlayerType[] possibleTypes;
	private EPlayerType selectedType;
	private CivilisationUiWrapper civilisation = new CivilisationUiWrapper();
	private byte team;
	private boolean ready;
	private boolean readyButtonChangeable;
	private boolean civilisationAndTeamChangeable;
	private boolean typeChangeable;

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPossibleTypes(EPlayerType[] possibleTypes) {
		assert possibleTypes.length > 0;
		this.possibleTypes = possibleTypes;
		setSelectedType(possibleTypes[0]);
	}

	public EPlayerType[] getPossibleTypes() {
		return possibleTypes;
	}

	public EPlayerType getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(EPlayerType selectedType) {
		assert Arrays.asList(possibleTypes).contains(selectedType);
		this.selectedType = selectedType;
	}

	public CivilisationUiWrapper getCivilisation() {
		return civilisation;
	}

	public void setCivilisation(CivilisationUiWrapper civilisation) {
		this.civilisation = civilisation;
	}

	public byte getTeam() {
		return team;
	}

	public void setTeam(byte team) {
		this.team = team;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public boolean isReadyButtonChangeable() {
		return readyButtonChangeable;
	}

	public void setReadyButtonChangeable(boolean readyButtonChangeable) {
		this.readyButtonChangeable = readyButtonChangeable;
	}

	public boolean isCivilisationAndTeamChangeable() {
		return civilisationAndTeamChangeable;
	}

	public void setCivilisationAndTeamChangeable(boolean civilisationAndTeamChangeable) {
		this.civilisationAndTeamChangeable = civilisationAndTeamChangeable;
	}

	public boolean isTypeChangeable() {
		return typeChangeable;
	}

	public void setTypeChangeable(boolean typeChangeable) {
		this.typeChangeable = typeChangeable;
	}

}
