import java.io.File;


public class CopyOfGenerateAll {
	
	public void generator(File out) {
		long test = 0;
		for (int i = 1; i < 20; i++) {
			for (int j=0; j < i; j++) {
				test++;
			}
		}
		
		System.out.println(test);
	}
	
	public void main(String[] args) {
		generator(null);
	}

}
