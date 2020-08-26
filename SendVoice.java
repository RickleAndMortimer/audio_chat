import javax.sound.sampled.*;
import java.io.*;

public class SendVoice extends Thread{
	byte[] bytesBuffer;
	AudioInputStream output;
	DataOutputStream out;
	
	public SendVoice(byte[] bytesBuffer, AudioInputStream output, DataOutputStream out) {
		this.bytesBuffer = bytesBuffer;
		this.output = output;
		this.out = out;
	}
	public void run() {
		try {
			int bytesRead = -1;
			while ((bytesRead = output.read(bytesBuffer)) != -1) {
				out.write(bytesBuffer, 0, bytesRead);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}