package moduleLibrary;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SnapshotManager {
	
	public static String print(byte[] bytes) {
	    StringBuilder sb = new StringBuilder();
	    for (byte b : bytes) {
	        sb.append(String.format("0x%02X1", b));
	    }
	    return sb.toString();
	}
	
	public static HashMap<String,String> makeSnapshot(String path ) throws IOException, NoSuchAlgorithmException{
		
		 try (Stream<Path> paths = Files.walk(Paths.get(path),1)) {
			 	HashMap<String,String> snapshot = new HashMap<String,String>();
	            List<String> result = paths.filter(Files::isRegularFile)
	                    .map(el -> el.toString()).collect(Collectors.toList());
	            
	            result.forEach(
	            		el -> {
	            			try {
	            				byte[] bytes = Files.readAllBytes(Paths.get(el));
	            				byte[] hash = MessageDigest.getInstance("MD5").digest(bytes);
	            				snapshot.put(el, print(hash));
	            				
							} catch (IOException | NoSuchAlgorithmException e) {
								e.printStackTrace();
							}
	            		}
	           	
	           );
	            
	           return snapshot;
		 }
	}
	
	public static HashMap<String,String> compareSnapshots(HashMap<String,String> snapshots, HashMap<String,String> newSnapshot) {
		if(!newSnapshot.isEmpty()) {
			newSnapshot.forEach((path,code) -> {
				File f = new File(path);
				if(snapshots.get(path)==null) {
					snapshots.put(path,code);
					System.out.println(f.getName() + " has been added to the directory");
					
				}else if(!code.equals(snapshots.get(path))){
					System.out.println(f.getName() + " has been modyfied");
					snapshots.replace(path,code);
				}else {
					System.out.println(f.getName() + " hasn't been modyfied");
				}
			});
			
		}
		return snapshots;
	}
}
