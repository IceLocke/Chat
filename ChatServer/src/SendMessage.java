import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

//对socket客户端发送一个String
public class SendMessage {
	static public void send(String message, ArrayList<User> cOS, int channelNum){
		Iterator<User> iter = cOS.iterator();
		while(iter.hasNext()){
			try{
				User tmp = iter.next();
				if(tmp.getChannel() == channelNum){
					PrintWriter writer = (PrintWriter) tmp.getPW();
					writer.println(message);
					writer.flush();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
