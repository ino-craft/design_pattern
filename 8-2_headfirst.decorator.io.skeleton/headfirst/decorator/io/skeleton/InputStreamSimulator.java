package headfirst.decorator.io.skeleton;
import java.util.Scanner;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamSimulator {

	public static void main(String[] args) {
	  while (true) {
		System.out.println("Input String :");		
		try {
			int c;
			Scanner scanInput = new Scanner(System.in);
			String exampleString = scanInput.nextLine();
			InputStream in =  new ByteArrayInputStream(exampleString.getBytes("UTF-8"));
			
			// The following code block should work after your extension			// 
			// 1 using more decorators :
			// InputStream inputStream = new ShiftInputStream(new LowerCaseInputStream(new BufferedInputStream(in)), 1);
			// 2 even more !: 
			// InputStream inputStream = new ShiftInputStream(new ShiftInputStream(new LowerCaseInputStream(new BufferedInputStream(in)), 4),-4);

			// InputStream inputStream = 
			// 		new ShiftInputStream(
			// 				new ShiftInputStream(
			// 						new LowerCaseInputStream(
			// 								new BufferedInputStream(in)), 
			// 						4),
			// 				-4);


			InputStream inputStream = new LowerCaseInputStream(in) ;

			while ( (c = inputStream.read()) >= 0)				
				System.out.print((char)c);
		
			System.out.println();
			inputStream.close();					
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
	}
}
