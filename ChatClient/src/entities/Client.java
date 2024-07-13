package entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private static int port = 8080;
	private static String hostname = "localhost";
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		try {
			Socket socket = new Socket(hostname, port);
			
			System.out.println("Notice: Send '--exit' to exit chat room.");
			System.out.print("Enter username: ");
			String username = sc.nextLine();
			
			Thread retrieveMessage = new Thread(new Runnable() {
				@Override
				public void run() {
					try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
						String message = "";
						while(true) {
							if((message = reader.readLine()) != null) {
								System.out.println(message);
							}
						}
					} catch(IOException ignored) {
					}
				}
			});
			retrieveMessage.start();
			
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			String message = "";
			while(true) {
				message = sc.nextLine();
				if(message.trim().equals("--exit")) {
					break;
				}
				
				message = username + ": " + message;
				writer.println(message);
			}
			
			socket.close();
			sc.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
