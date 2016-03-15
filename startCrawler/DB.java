package startCrawler;

import java.io.File;
import java.io.IOException;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

public class DB {

	private static RecordManager myDBRec;
	public static PrimaryHashMap<String, String> myTable;
	private long entryCnt;
	
	public DB(String fileName) {
		try {			
			// Make sure storage directory exists
			new File(new File(fileName).getParent()).mkdir();
			// Initialize a file-based hash map (using jdbm2 [https://code.google.com/p/jdbm2/]) to store the visited pages
			myDBRec = RecordManagerFactory.createRecordManager(fileName);
			myTable= myDBRec.hashMap("myTable");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public PrimaryHashMap<String, String> getMyTable() {
		return myTable;
	}

	public long getEntryCnt() {
		return entryCnt;
	}

	public void setEntryCnt(long entryCnt) {
		this.entryCnt = entryCnt;
	}

	public void storeTxt(String url, String text) {
		myTable.put(url, text);
		entryCnt++;
		
		// Commit after every 50 pages are added to release memory and save to disk (in case crawler is stopped)
		if (entryCnt % 100 == 0) {
			try {
				myDBRec.commit();
				System.out.println("Committed " + entryCnt);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void commit() throws IOException{
		myDBRec.commit();
	}
	
	public void close() {
		try {
			myDBRec.commit();
			myDBRec.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}






}
