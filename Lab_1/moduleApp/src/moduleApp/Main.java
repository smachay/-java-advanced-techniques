package moduleApp;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;
import moduleLibrary.SnapshotManager;

public class Main {

		
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		int option = 0;
		Scanner sc= new Scanner(System.in);
		HashMap<String,String> snapshots = new HashMap<String,String>();
		HashMap<String,String> lastSnapshot = new HashMap<String,String>();
		
		while(option != 2) {
			System.out.println("Press:");
			System.out.println("1 - to make a snapshot");
			System.out.println("2 - to close the program");
			option = sc.nextInt();
			
			switch(option) {
			case 1:
				System.out.println("");
				System.out.println("Write catalog location:");
				String path = new Scanner(System.in).nextLine();
				lastSnapshot = SnapshotManager.makeSnapshot(path);
				snapshots = SnapshotManager.compareSnapshots(snapshots, lastSnapshot);
				System.out.println("");
				break;
			case 2:
				
				System.out.println();
			}
			System.out.println("--------------------------------------------------------");
			
		}
		
		
	}

}
