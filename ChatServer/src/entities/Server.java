package entities;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private static Set<Socket> clientGroup = new HashSet<Socket>();
	private static int port = 8080;
	private static ExecutorService executor = Executors.newFixedThreadPool(50);
	
	public static void main(String[] args) {
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(port);
			System.out.println("Server started at port " + port);
			
			while(true) {
				Socket client = server.accept();
				System.out.println("New client(" + client.getInetAddress().getHostAddress() + ") connected to server.");
				
				synchronized(clientGroup) {
					clientGroup.add(client);
				}
				
				executor.submit(new ClientHandler(client));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void broadcastMessage(Socket sender, String message) {
		synchronized (clientGroup) {
			for(Socket client : clientGroup) {
				if(client != sender) {
					try {
						PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
						writer.println(message);
					} catch(IOException ignored) {}
				}
			}
		}
	}
}