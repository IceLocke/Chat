import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

class ServerMain {
	
	ServerSocket server;
	
	public static void main(String[] args){
		new ServerMain().go();
	}
	
	public void go(){
		try{
			server = new ServerSocket(6666);
			while(true){
				Socket socket = server.accept();
				OutputStreamWriter o = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
				User u = new User(o);
				Vars.cOS.add(u);
				
				Thread t = new Thread(new SingleSocket( socket, 
														Vars.cOS.get(Vars.cOS.size()-1).getChannel(),
														Vars.cOS.size()-1
														)
									);
				t.start();
				//��ȡ���һ��������socket���ӣ�����size()-1�Ϳ���ȷ�������һ�����������ӡ�
				System.out.println("[System Log]Got a connection from " + socket.getInetAddress() + "." + "Channel: " + Vars.cOS.get(Vars.cOS.size()-1).getChannel());
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
