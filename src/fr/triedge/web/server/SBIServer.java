package fr.triedge.web.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class SBIServer {
	
	public static final int DEFAULT_PORT								= 8080;
	public static final File WEB_ROOT 									= new File(".");
	public static final String DEFAULT_FILE 							= "index.html";
	public static final String FILE_NOT_FOUND 							= "404.html";
	public static final String METHOD_NOT_SUPPORTED 					= "not_supported.html";
	
	private boolean running = true;
	private CommandLine cmd;

	public void start(String[] args) {
		int port = DEFAULT_PORT;
		// Init server
		parseOptions(args);
		if (cmd.hasOption("port")) {
			port = Integer.parseInt(cmd.getOptionValue("port"));
		}
		
		try {
			ServerSocket serverConnect = new ServerSocket(port);
			System.out.println("Listening on port: "+port);
			while (isRunning()) {
				WebWorker worker = new WebWorker(serverConnect.accept());
				
				// create dedicated thread to manage the client connection
				Thread thread = new Thread(worker);
				thread.start();
			}
			serverConnect.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void parseOptions(String[] args) {
		Options options = new Options();

        Option input = new Option("p", "port", true, "Server listener port");
        input.setRequired(false);
        options.addOption(input);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
