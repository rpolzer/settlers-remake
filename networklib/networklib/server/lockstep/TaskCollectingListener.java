package networklib.server.lockstep;

import java.util.LinkedList;
import java.util.List;

import networklib.NetworkConstants;
import networklib.channel.GenericDeserializer;
import networklib.channel.listeners.PacketChannelListener;
import networklib.channel.packet.Packet;
import networklib.server.packets.ServersideTaskPacket;

/**
 * This listener collects {@link Packet}s for the {@link NetworkConstants}.Keys.SYNCHRONOUS_TASK key and adds them to a list. The elements can then be
 * removed from the list to be send to the clients as batch.
 * 
 * @author Andreas Eberle
 * 
 */
public class TaskCollectingListener extends PacketChannelListener<ServersideTaskPacket> {
	private List<ServersideTaskPacket> currTasksList = new LinkedList<ServersideTaskPacket>();

	public TaskCollectingListener() {
		super(NetworkConstants.Keys.SYNCHRONOUS_TASK, new GenericDeserializer<ServersideTaskPacket>(ServersideTaskPacket.class));
	}

	/**
	 * 
	 * @return
	 */
	public List<ServersideTaskPacket> getAndResetTasks() {
		List<ServersideTaskPacket> temp = currTasksList;
		currTasksList = new LinkedList<ServersideTaskPacket>();
		return temp;
	}

	@Override
	protected void receivePacket(int key, ServersideTaskPacket deserialized) {
		currTasksList.add(deserialized);
	}
}
