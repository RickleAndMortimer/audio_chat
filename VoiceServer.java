import java.net.*;
import javax.sound.sampled.*;
import java.io.*;

public class VoiceServer {
	private ServerSocket server_socket;
	Socket socket;
	private TargetDataLine output_line;
	private SourceDataLine input_line;
	DataInputStream in;
	DataOutputStream out;
	
	public VoiceServer (int port, AudioFormat format) {
		try {	
			//Audio device setup
			DataLine.Info output_info = new DataLine.Info(TargetDataLine.class, format);
			DataLine.Info input_info = new DataLine.Info(SourceDataLine.class, format);
			output_line = (TargetDataLine) AudioSystem.getLine(output_info);
			input_line = (SourceDataLine) AudioSystem.getLine(input_info);
			output_line.open(format, output_line.getBufferSize());	
			input_line.open(format);
			output_line.start();
			input_line.start();
			AudioInputStream output = new AudioInputStream(output_line);
			//Starts the server
			this.server_socket = new ServerSocket(port);
			socket = server_socket.accept();
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			//Send audio to client through a thread
			byte[] bytesBuffer = new byte[4];
			SendVoice sv = new SendVoice(bytesBuffer, output, out); 
			sv.start();
			//Listen to audio from client through a thread
			ReadVoice rv = new ReadVoice(bytesBuffer, input_line, in);
			rv.start();
		} catch (Exception e) {
			System.out.println("Oopsy Woopsy (O_o)");
		}
	}
	public static void main(String args[]) {		
		AudioFormat format = new AudioFormat(22000, 16, 2, true, true);
		VoiceServer vs = new VoiceServer(Integer.parseInt(args[0]), format);
		System.out.println("Server started at port: " + args[0]);
	}
}