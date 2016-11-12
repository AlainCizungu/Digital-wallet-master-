import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Antifraud {
	
	public static void main(String[] args) throws IOException {
		
		String infile1, infile2, outfile1, outfile2, outfile3;

		if (args.length < 0) {
			System.out.println("Not correct number of inputs");
			return;
		} else {
			infile1 = args[0];
			infile2 = args[1];
			outfile1 = args[2];
			outfile2 = args[3];
			outfile3 = args[4];	
		}


		// open file input stream
		BufferedReader reader = new BufferedReader(new FileReader(infile1));
		// ignore first line
		reader.readLine();
		// read file line by line
		String line = null;
		Scanner scanner = null;
		int key = 0, id2 = 0, temp = 0;

		// List<Transaction> transList = new ArrayList<>();
		HashMap<Integer, LinkedList<Integer>> hm = new HashMap<>();

		while ((line = reader.readLine()) != null) {
			scanner = new Scanner(line);
			scanner.useDelimiter(",");
			scanner.next(); // Time
			String data = scanner.next(); // id1
			key = Integer.parseInt(data.trim());
			data = scanner.next(); // id2
			id2 = Integer.parseInt(data.trim());
			LinkedList<Integer> ids = hm.get(key);
			if (ids == null) {
				ids = new LinkedList<>();
				ids.add(id2);
				hm.put(key, ids);
			} else {
				ids.add(id2);
			}
			temp = id2;
			id2 = key;
			key = temp;
			ids = hm.get(key);
			if (ids == null) {
				ids = new LinkedList<>();
				ids.add(id2);
				hm.put(key, ids);
			} else {
				ids.add(id2);
			}
		}
		// close reader
		reader.close();
		// System.out.println(hm);
		reader = new BufferedReader(new FileReader(infile2));
		File file = new File(outfile1);
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw1 = new BufferedWriter(fw);
		file = new File(outfile2);
		FileWriter fw2 = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw2 = new BufferedWriter(fw2);
		file = new File(outfile3);
		FileWriter fw3 = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw3 = new BufferedWriter(fw3);
		// ignore first line
		reader.readLine();
		boolean found = false;
		int source =0;
		Queue<Integer> queue = new LinkedList<Integer>();
		HashMap<Integer, Boolean> visitedList = new HashMap<>();
		LinkedList<Integer> children;
		Iterator<Integer> i;
		int numEdge = 1, nextNumEdge = 0, numChild = 0, depth = 1;
//		Iterator<Integer> i;
		while ((line = reader.readLine()) != null) {
			scanner = new Scanner(line);
			scanner.useDelimiter(",");
			scanner.next(); // Time
			String data = scanner.next(); // id1
			source = Integer.parseInt(data.trim());
			data = scanner.next(); // id2
			id2 = Integer.parseInt(data.trim());
			queue.add(source);
			visitedList.put(source, true);
			numEdge = 1; nextNumEdge = 0; numChild = 0; depth = 1;
			while (!queue.isEmpty() && depth <= 4) {
				if(found)
					break;
				int node = (int) queue.remove();
				numEdge--;
				children = hm.get(node);
				if (children != null) {
					i = children.iterator();
					while (i.hasNext()) {
						int child = i.next();
						if (visitedList.get(child) == null) {
							visitedList.put(child, true);
							//System.out.println("depth: " + depth + ", " + child);
							if(child  == id2 && depth == 1){
								found = true;
								bw1.write("trusted\n");
								bw2.write("trusted\n");
								bw3.write("trusted\n");
								break;
							}
							if(child  == id2 && depth == 2){
								found = true;
								bw1.write("unverified\n");
								bw2.write("trusted\n");
								bw3.write("trusted\n");
								break;
							}
							if(child  == id2 && (depth == 3 || depth == 4)){
								found = true;
								bw1.write("unverified\n");
								bw2.write("unverified\n");
								bw3.write("trusted\n");
								break;
							}
							queue.add(child);
							numChild++;
						}
					}
					nextNumEdge += numChild;
					numChild = 0;
				}
				if (numEdge == 0) {
					numEdge = nextNumEdge;
					nextNumEdge = 0;
					depth++;
				}

			}
			if(!found){
				bw1.write("unverified\n");
				bw2.write("unverified\n");
				bw3.write("unverified\n");
			}
			found = false;
			queue.clear();
			visitedList.clear();
		}		
		bw1.close();
		bw2.close();
		bw3.close();
	}
}
