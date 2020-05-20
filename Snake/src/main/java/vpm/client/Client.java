package vpm.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import vpm.helper.Constants;


public class Client {
	
	
	
//	public static void main(String[] args) throws Exception {
//		new Client();
//	}
//	
//	public Client() throws UnknownHostException, IOException, ClassNotFoundException {
//		Socket socket = new Socket(Constants.SERVER_IP , Constants.PORT);
//		
//		ServerConnection serverConnection = new ServerConnection(socket);
//		
//		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
//		PrintWriter sender = new PrintWriter(socket.getOutputStream(),true);
//		
//		new Thread(serverConnection).start();		
//		
//		
//		while(true) {
//			System.out.println(">");
//			String command = keyboard.readLine();
//			
//			sender.println(command);
//
//			if(command.equals("quit")) {
//				break;
//			}
//		}
//		
//		socket.close();
//		
//	}
	
}
