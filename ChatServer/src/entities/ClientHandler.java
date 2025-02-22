package entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler implements Runnable {
	private Socket socket;
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			String message = "";
			while(true) {
				if((message = reader.readLine()) != null)
					Server.broadcastMessage(socket, message);
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
