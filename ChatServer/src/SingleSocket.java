import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class SingleSocket implements Runnable{
	BufferedReader reader;
	Socket socket;
	int channel;
	int index;
	
	public SingleSocket(Socket clientSocket, int channelNum, int tmp){
		channel = channelNum;
		index = tmp;
		try{
			socket = clientSocket;
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8" ));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void run(){
		String message;
		try{
			while((message = reader.readLine()) != null){
				
				String[] temp = message.split(":");
				
				if(temp[1].charAt(0) == '/'){
					String[] command = temp[1].split(" ");
					
					switch(command[0]){
						case "/changechannel":{
							channel = Integer.parseInt(command[1]);
							Vars.cOS.get(index).setChannel(channel);
							System.out.println("[Command Log]" + temp[0] +" used command /changechannel " + channel);
							SendMessage.send(temp[0] + " entered NO." + channel + " chatting-room", Vars.cOS, channel);
						}
					}
				}
				
				else{
					System.out.println("[Chat Log]" + message);
					SendMessage.send(message, Vars.cOS, channel);
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
