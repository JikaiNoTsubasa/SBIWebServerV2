package fr.triedge.web.run;

import fr.triedge.web.server.SBIServer;

public class RunWebServer {

	public static void main(String[] args) {
		SBIServer server = new SBIServer();
		server.start(args);
	}

}
