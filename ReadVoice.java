import javax.sound.sampled.*;
import java.io.*;

public class ReadVoice extends Thread {
	byte[] bytesBuffer;
	SourceDataLine input_line;
	DataInputStream in;
	
	public ReadVoice(byte[] bytesBuffer, SourceDataLine input_line, DataInputStream in) {
		this.bytesBuffer = bytesBuffer;
		this.input_line = input_line;
		this.in = in;
	}
	public void run() {
		try {
			int bytesRead = -1;
			while ((bytesRead = in.read(bytesBuffer)) != -1) {
				input_line.write(bytesBuffer, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}