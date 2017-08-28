import java.io.OutputStreamWriter;
import java.io.PrintWriter;

//�����û���
public class User {
	private PrintWriter writer = null;
	private int channelID;
	private String channelName;
	
	public User(OutputStreamWriter os){
		writer = new PrintWriter(os);
		channelID = 1;
		//����Ƶ�� ID=1;
	}
	
	public User(){
		;
	}
	
	public PrintWriter getPW(){
		return writer;
	}
	
	public void setPW(PrintWriter pw){
		writer = pw;
	}
	
	public int getChannel(){
		return channelID;
	}
	
	public void setChannel(int num){
		channelID = num;
	}
	
	public String getChannelName(){
		return channelName;
	}
	
	public void setChannelName(String name){
		channelName = name;
	}
}
