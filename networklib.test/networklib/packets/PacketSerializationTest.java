package networklib.packets;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import networklib.NetworkConstants;
import networklib.TestUtils;
import networklib.channel.Channel;
import networklib.channel.GenericDeserializer;
import networklib.channel.IDeserializingable;
import networklib.channel.listeners.BufferingPacketListener;
import networklib.channel.packet.EmptyPacket;
import networklib.channel.packet.Packet;
import networklib.channel.reject.RejectPacket;
import networklib.client.packets.SyncTasksPacket;
import networklib.client.packets.TaskPacket;
import networklib.client.task.TestTaskPacket;
import networklib.common.packets.ArrayOfMatchInfosPacket;
import networklib.common.packets.MapInfoPacket;
import networklib.common.packets.MatchInfoPacket;
import networklib.common.packets.MatchInfoUpdatePacket;
import networklib.common.packets.MatchStartPacket;
import networklib.common.packets.OpenNewMatchPacket;
import networklib.common.packets.PlayerInfoPacket;
import networklib.common.packets.TimeSyncPacket;
import networklib.server.packets.ServersideSyncTasksPacket;
import networklib.server.packets.ServersideTaskPacket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * This class tests the serialization and deserialization of multiple subclasses of {@link Packet} when sent over a {@link Channel}.
 * 
 * @author Andreas Eberle
 * 
 */
@RunWith(value = Parameterized.class)
public class PacketSerializationTest {

	private Channel c1;
	private Channel c2;

	@Before
	public void setUp() throws IOException {
		Channel[] channels = TestUtils.setUpLoopbackChannels();
		c1 = channels[0];
		c2 = channels[1];
	}

	@After
	public void tearDown() {
		c1.close();
		c2.close();
	}

	private Packet packet;
	private BufferingPacketListener<? extends Packet> listener;

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ new EmptyPacket(), EmptyPacket.DEFAULT_DESERIALIZER },
				{ new PlayerInfoPacket("IDBLA82348-#�l�34r", "NameBKUIH893428())/\"�/"), d(PlayerInfoPacket.class) },
				{ new MapInfoPacket("id<30u9Hjdi w3", "Nameo8/(�\"(/!=�", "authorId8unsdkjfn8932", "authorName uHh89023u9h"), d(MapInfoPacket.class) },
				{ createMatchInfoPacket(), d(MatchInfoPacket.class) },
				{ new ArrayOfMatchInfosPacket(new MatchInfoPacket[0]), d(ArrayOfMatchInfosPacket.class) },
				{ new ArrayOfMatchInfosPacket(new MatchInfoPacket[] { createMatchInfoPacket(), createMatchInfoPacket() }),
						d(ArrayOfMatchInfosPacket.class) },
				{ new OpenNewMatchPacket("dfjosj", (byte) 5, new MapInfoPacket("id", "name", "authorid", "authorName")), d(OpenNewMatchPacket.class) },
				{ new RejectPacket(NetworkConstants.Messages.UNAUTHORIZED, NetworkConstants.Keys.IDENTIFY_USER), d(RejectPacket.class) },
				{ new MatchStartPacket(createMatchInfoPacket(), 23424L), d(MatchStartPacket.class) },
				{ new MatchInfoUpdatePacket(34, createMatchInfoPacket()), d(MatchInfoUpdatePacket.class) },
				{ new TimeSyncPacket(23424), d(TimeSyncPacket.class) },

				{ new ServersideTaskPacket("sdfsfsdf".getBytes()), d(ServersideTaskPacket.class) },
				{ new ServersideSyncTasksPacket(23, Arrays.asList(new ServersideTaskPacket("dsfjsfj".getBytes()),
						new ServersideTaskPacket("ehgdhd".getBytes()))), d(ServersideSyncTasksPacket.class) },

				{ new TestTaskPacket("tesdfk��l9/&%/%&\"\\u8u23jo", 23424, (byte) -2), TaskPacket.DEFAULT_DESERIALIZER },
				{ new SyncTasksPacket(234, Arrays.asList((TaskPacket) new TestTaskPacket("dsfdsdf", 23, (byte) -3),
						(TaskPacket) new TestTaskPacket("dsfs��#��dsdf", 4345, (byte) 5))), d(SyncTasksPacket.class) }
		};
		return Arrays.asList(data);
	}

	private static MatchInfoPacket createMatchInfoPacket() {
		MapInfoPacket mapInfo = new MapInfoPacket("sdjfij", "sdfsdflksjdlfk", "sdflnnp0928u30894", "sdlkfkjl�:�_�");
		PlayerInfoPacket[] players = new PlayerInfoPacket[] {
				new PlayerInfoPacket("1dddsfsfd", "787(/(hdsfjhk2"),
				new PlayerInfoPacket("2lkkjsdofij", "0sdfsddfsfgw32dsfjhk2")
		};
		return new MatchInfoPacket("id28948298fedkj", "KHDHifuh(&/%T", (byte) 3, mapInfo, players);
	}

	private static <T extends Packet> Object d(Class<T> classType) {
		return new GenericDeserializer<T>(classType);
	}

	/**
	 * Constructor to accept the parameters of the test.
	 * 
	 * @param packet
	 *            The packet to be sent over the channel and to be compared with the resulting packet.
	 * @param deserializer
	 *            The {@link IDeserializingable} used to deserialize the given packet.
	 */
	public <T extends Packet> PacketSerializationTest(T packet, IDeserializingable<T> deserializer) {
		this.packet = packet;
		this.listener = new BufferingPacketListener<T>(NetworkConstants.Keys.TEST_PACKET, deserializer);
	}

	@Test
	public void testSerializationAndDeserialization() throws InterruptedException {
		c2.registerListener(listener);
		c1.sendPacket(NetworkConstants.Keys.TEST_PACKET, packet);

		Thread.sleep(30);

		List<? extends Packet> bufferedPackets = listener.popBufferedPackets();
		assertEquals(1, bufferedPackets.size());
		assertEquals(packet, bufferedPackets.get(0));
	}
}
