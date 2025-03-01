package commands.user.regular.pwi;

import commands.base.Category;
import commands.base.CommandWithoutSubCommands;
import events.MessageReceivedEvent;
import information.ownerconfiguration.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PWIServerStatus extends CommandWithoutSubCommands {
	private final String DA_SERVER_URL = "pwieu3.en.perfectworld.eu";
	private final String ET_SERVER_URL = "pwiwest4.perfectworld.com";
	private final String TT_SERVER_URL = "pwigc2.perfectworld.com";
	private final String TI_SERVER_URL = "pwieast2.perfectworld.com";
	private final int serverPort = 29000;

	public PWIServerStatus() {
		super(Category.PWI, "Check PWI servers' statuses and ping (from bot location)!", null, true);
		this.addName("PWIServersStatus");
		this.addName("PWIServerStatus");
		this.addName("ServersStatus");
		this.addName("ServerStatus");
		this.buildHelpMessage();
	}

	@Override
	protected void runCommandActions(MessageReceivedEvent event) {
		try {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Servers statuses").setDescription("Checking servers. . .");
			Embeds.configDefaultEmbedColor(eb);

			Message msg = event.getChannel().sendMessage(eb.build()).complete();

			String statusesString = getServersAvailabilityDisplay();
			eb.setDescription(statusesString);

			msg.editMessage(eb.build()).queue();
		} catch (Exception e) {
			/* Probably just no permission to send msg in the channel */}
	}

	public String getServersAvailabilityDisplay() {
		StringBuilder sb = new StringBuilder();
		sb.append("**Etherblade**: ").append(serverAvailabilityCheck(ET_SERVER_URL, serverPort))
				.append("\n\n**Twilight Temple**: ").append(serverAvailabilityCheck(TT_SERVER_URL, serverPort))
				.append("\n\n**Dawnglory**: ").append(serverAvailabilityCheck(DA_SERVER_URL, serverPort))
				.append("\n\n**Tideswell**: ").append(serverAvailabilityCheck(TI_SERVER_URL, serverPort));
		System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * Checks if a server is reachable or not and returns a string accordingly.
	 * 
	 * @param serverAddress the server's address to use for the connection.
	 * @param serverPort    the port to use for the connection.
	 * @return "down" or "up" according to whether the program fails to connect or
	 *         no and "not responding. Probably booting up." if the server does not
	 *         respond.
	 */
	public String serverAvailabilityCheck(String serverAddress, int serverPort) {
		Socket s = new Socket();
		try {
			long startTime = System.currentTimeMillis();
			s.connect(new InetSocketAddress(serverAddress, serverPort), 1000);
			long endTime = System.currentTimeMillis();
			return "🟢 " + (endTime - startTime) + "ms";
		} catch (Exception e) {
			return "🔴 down";
		} finally {
			if (!s.isClosed())
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
};
