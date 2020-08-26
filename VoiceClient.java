import java.net.*;
import javax.sound.sampled.*;
import java.io.*;

public class VoiceClient {
	private ServerSocket server_socket;
	Socket socket;
	private TargetDataLine output_line;
	private SourceDataLine input_line;
	DataInputStream in;
	DataOutputStream out;
	
	public VoiceClient(AudioFormat format, String ip, int port) {
		try {	
			//Audio device setup
			DataLine.Info output_info = new DataLine.Info(TargetDataLine.class, format);
			DataLine.Info input_info = new DataLine.Info(SourceDataLine.class, format);
			output_line = (TargetDataLine) AudioSystem.getLine(output_info);
			input_line = (SourceDataLine) AudioSystem.getLine(input_info);
			output_line.open(format, (int)output_line.getBufferSize());	
			input_line.open(format, (int)input_line.getBufferSize());
			output_line.start();
			input_line.start();
			AudioInputStream output = new AudioInputStream(output_line);
			//Starts the server
			this.socket = new Socket(ip, port);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			//Send audio to client through a thread
			System.out.println(input_line.getBufferSize());
			byte[] input_bytesBuffer = new byte[(int)input_line.getBufferSize()];
			SendVoice sv = new SendVoice(input_bytesBuffer, output, out); 
			sv.start();
			//Listen to audio from client through a thread
			byte[] output_bytesBuffer = new byte[(int)output_line.getBufferSize()];
			ReadVoice rv = new ReadVoice(output_bytesBuffer, input_line, in);
			rv.start();
		} catch (Exception e) {
			System.out.println("Oopsy Woopsy (O_o)");
		}
	}
	public static void main(String args[]) {
		try {
			System.out.println("Connecting...");
			AudioFormat format = new AudioFormat(22000, 16, 2, true, true);
			VoiceClient vc = new VoiceClient(format, args[0], Integer.parseInt(args[1]));
			System.out.println("Connection established!");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}