package sw.pos;

import java.io.IOException;

public class ScreenClear {
	

	public void ScreenClear() throws InterruptedException, IOException
	{
		final String os = System.getProperty("os.name");
		if(os.contains("Windows"))
		{
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		}
		else
		{
			Runtime.getRuntime().exec("clear");
		}
	}

	
}
