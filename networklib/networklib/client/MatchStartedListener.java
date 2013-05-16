package networklib.client;

import java.io.IOException;

import networklib.NetworkConstants;
import networklib.channel.GenericDeserializer;
import networklib.channel.listeners.PacketChannelListener;
import networklib.client.receiver.IPacketReceiver;
import networklib.common.packets.MatchStartPacket;

/**
 * 
 * @author Andreas Eberle
 * 
 */
public class MatchStartedListener extends PacketChannelListener<MatchStartPacket> {

	private NetworkClient networkClient;
	private IPacketReceiver<MatchStartPacket> matchStartedListener;

	public MatchStartedListener(NetworkClient networkClient, IPacketReceiver<MatchStartPacket> matchStartedListener) {
		super(NetworkConstants.Keys.MATCH_STARTED, new GenericDeserializer<MatchStartPacket>(MatchStartPacket.class));
		this.networkClient = networkClient;
		this.matchStartedListener = matchStartedListener;
	}

	@Override
	protected void receivePacket(int key, MatchStartPacket packet) throws IOException {
		networkClient.matchStartedEvent();

		if (matchStartedListener != null)
			matchStartedListener.receivePacket(packet);
	}

}
