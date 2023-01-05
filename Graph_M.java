import java.util.*;
import java.io.*;

	
	public class Graph_M 
	{
		public class Vertex 
		{
			HashMap<String, Integer> nbrs = new HashMap<>();
		}

		static HashMap<String, Vertex> vtces;

		public Graph_M() 
		{
			vtces = new HashMap<>();
		}

		public int numVetex() 
		{
			return this.vtces.size();
		}

		public boolean containsVertex(String vname) 
		{
			return this.vtces.containsKey(vname);
		}

		public void addVertex(String vname) 
		{
			Vertex vtx = new Vertex();
			vtces.put(vname, vtx);
		}

		public void removeVertex(String vname) 
		{
			Vertex vtx = vtces.get(vname);
			ArrayList<String> keys = new ArrayList<>(vtx.nbrs.keySet());

			for (String key : keys) 
			{
				Vertex nbrVtx = vtces.get(key);
				nbrVtx.nbrs.remove(vname);
			}

			vtces.remove(vname);
		}

		public int numEdges() 
		{
			ArrayList<String> keys = new ArrayList<>(vtces.keySet());
			int count = 0;

			for (String key : keys) 
			{
				Vertex vtx = vtces.get(key);
				count = count + vtx.nbrs.size();
			}

			return count / 2;
		}

		public boolean containsEdge(String vname1, String vname2) 
		{
			Vertex vtx1 = vtces.get(vname1);
			Vertex vtx2 = vtces.get(vname2);
			
			if (vtx1 == null || vtx2 == null || !vtx1.nbrs.containsKey(vname2)) {
				return false;
			}

			return true;
		}

		public void addEdge(String vname1, String vname2, int value) 
		{
			Vertex vtx1 = vtces.get(vname1); 
			Vertex vtx2 = vtces.get(vname2); 

			if (vtx1 == null || vtx2 == null || vtx1.nbrs.containsKey(vname2)) {
				return;
			}

			vtx1.nbrs.put(vname2, value);
			vtx2.nbrs.put(vname1, value);
		}

		public void removeEdge(String vname1, String vname2) 
		{
			Vertex vtx1 = vtces.get(vname1);
			Vertex vtx2 = vtces.get(vname2);
			
			//check if the vertices given or the edge between these vertices exist or not
			if (vtx1 == null || vtx2 == null || !vtx1.nbrs.containsKey(vname2)) {
				return;
			}

			vtx1.nbrs.remove(vname2);
			vtx2.nbrs.remove(vname1);
		}

		public void display_Map() 
		{
			System.out.println("\t Mumbai Local Map");
			System.out.println("\t------------------");
			System.out.println("----------------------------------------------------\n");
			ArrayList<String> keys = new ArrayList<>(vtces.keySet());

			for (String key : keys) 
			{
				String str = key + " =>\n";
				Vertex vtx = vtces.get(key);
				ArrayList<String> vtxnbrs = new ArrayList<>(vtx.nbrs.keySet());
				
				for (String nbr : vtxnbrs)
				{
					str = str + "\t" + nbr + "\t";
                    			if (nbr.length()<16)
                    			str = str + "\t";
                    			if (nbr.length()<8)
                    			str = str + "\t";
                    			str = str + vtx.nbrs.get(nbr) + "\n";
				}
				System.out.println(str);
			}
			System.out.println("\t------------------");
			System.out.println("---------------------------------------------------\n");

		}
		
		public void display_Stations() 
		{
			System.out.println("\n***********************************************************************\n");
			ArrayList<String> keys = new ArrayList<>(vtces.keySet());
			int i=1;
			for(String key : keys) 
			{
				System.out.println(i + ". " + key);
				i++;
			}
			System.out.println("\n***********************************************************************\n");
		}
			
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public boolean hasPath(String vname1, String vname2, HashMap<String, Boolean> processed) 
		{
			// DIR EDGE
			if (containsEdge(vname1, vname2)) {
				return true;
			}

			//MARK AS DONE
			processed.put(vname1, true);

			Vertex vtx = vtces.get(vname1);
			ArrayList<String> nbrs = new ArrayList<>(vtx.nbrs.keySet());

			//TRAVERSE THE NBRS OF THE VERTEX
			for (String nbr : nbrs) 
			{

				if (!processed.containsKey(nbr))
					if (hasPath(nbr, vname2, processed)) {
						return true;
					}
			}

			return false;
		}
		
		
		private class DijkstraPair implements Comparable<DijkstraPair> 
		{
			String vname;
			String psf;
			int cost;

			/*
			The compareTo method is defined in Java.lang.Comparable.
			Here, we override the method because the conventional compareTo method
			is used to compare strings,integers and other primitive data types. But
			here in this case, we intend to compare two objects of DijkstraPair class.
			*/ 

			/*
			Removing the overriden method gives us this errror:
			The type Graph_M.DijkstraPair must implement the inherited abstract method Comparable<Graph_M.DijkstraPair>.compareTo(Graph_M.DijkstraPair)

			This is because DijkstraPair is not an abstract class and implements Comparable interface which has an abstract 
			method compareTo. In order to make our class concrete(a class which provides implementation for all its methods)
			we have to override the method compareTo
			 */
			@Override
			public int compareTo(DijkstraPair o) 
			{
				return o.cost - this.cost;
			}
		}
		
		public int dijkstra(String src, String des, boolean nan, boolean vk) 
		{
			int val = 0;
			ArrayList<String> ans = new ArrayList<>();
			HashMap<String, DijkstraPair> map = new HashMap<>();

			Heap<DijkstraPair> heap = new Heap<>();

			for (String key : vtces.keySet()) 
			{
				DijkstraPair np = new DijkstraPair();
				np.vname = key;
				//np.psf = "";
				np.cost = Integer.MAX_VALUE;

				if (key.equals(src)) 
				{
					np.cost = 0;
					np.psf = key;
				}

				heap.add(np);
				map.put(key, np);
				
			}

			//keep removing the pairs while heap is not empty
			while (!heap.isEmpty()) 
			{
				DijkstraPair rp = heap.remove();
				
				if(rp.vname.equals(des))
				{
					val = rp.cost;
					break;
				}
				
				map.remove(rp.vname);

				ans.add(rp.vname);
				
				Vertex v = vtces.get(rp.vname);
				for (String nbr : v.nbrs.keySet()) 
				{
//					System.out.println(nbr);
					if (map.containsKey(nbr)) 
					{
						int oc = map.get(nbr).cost;
						Vertex k = vtces.get(rp.vname);
						int nc;
						if(nan)
							nc = rp.cost + 60*k.nbrs.get(nbr);
						else
							nc = rp.cost + k.nbrs.get(nbr);

						if (nc < oc) 
						{
							DijkstraPair gp = map.get(nbr);
							gp.psf = rp.psf + nbr;
							gp.cost = nc;

							heap.updatePriority(gp);
//							System.out.println(gp.psf);
							if(vk && gp.psf.substring(0, src.length()).equals(src) && gp.psf.substring(gp.psf.length()-des.length()).equals(des)) {
								String s=gp.psf;
								path_min(s,src,des);
//								System.out.println(s+"            "+"JN IJNEI");
							}
//							System.out.println(gp.psf.substring(0, src.length())+" "+gp.psf.substring(gp.psf.length()-des.length()));
//							System.out.println(gp.psf);
						}
					}
				}
			}
			return val;
		}
		
		public void path_min(String s, String src, String des) {
			Graph_M g = new Graph_M();
			Create_Local_Map(g);
			s=s.substring(src.length(),s.length()-des.length());
			System.out.println("START  ==>  " + src);
			for(int i=0; i<s.length(); i++){
				for(int j=i+1;j<s.length();j++) {
					if(g.containsVertex(s.substring(i,j))) {
						String sb=s.substring(i,s.length());
						for(int k=sb.length();k>0;k--) {
							if(g.containsVertex(s.substring(i,k))) {
								System.out.println("       ==>  "+s.substring(i,k));
								s=s.substring(k);
								i=-1;
								break;
							}
						}
							break;
					}
				}
			}
			System.out.println("       ==>  "+s);

			System.out.println("       ==>  "+des + "   ==>    END");
		}
		
		private class Pair 
		{
			String vname;
			String psf;
			int min_dis;
			int min_time;
		}
		
		public String Get_Minimum_Distance(String src, String dst) 
		{
//			Graph_M g = new Graph_M();
//			String s=g.dijkstra1(src, dst, true);
//			System.out.println(s);
			int min = Integer.MAX_VALUE;
			//int time = 0;
			String ans = "";
			HashMap<String, Boolean> processed = new HashMap<>();
			LinkedList<Pair> stack = new LinkedList<>();

			// create a new pair
			Pair sp = new Pair();
			sp.vname = src;
			sp.psf = src + "  ";
			sp.min_dis = 0;
			sp.min_time = 0;
			
			// put the new pair in stack
			stack.addFirst(sp);

			// while stack is not empty keep on doing the work
			while (!stack.isEmpty()) 
			{
				// remove a pair from stack
				Pair rp = stack.removeFirst();

				if (processed.containsKey(rp.vname)) 
				{
					continue;
				}

				// processed put
				processed.put(rp.vname, true);
				
				//if there exists a direct edge b/w removed pair and destination vertex
				if (rp.vname.equals(dst)) 
				{
					int temp = rp.min_dis;
					if(temp<min) {
						ans = rp.psf;
						min = temp;
					}
					continue;
				}

				Vertex rpvtx = vtces.get(rp.vname);
				ArrayList<String> nbrs = new ArrayList<>(rpvtx.nbrs.keySet());

				for(String nbr : nbrs) 
				{
					// process only unprocessed nbrs
					if (!processed.containsKey(nbr)) {

						// make a new pair of nbr and put in queue
						Pair np = new Pair();
						np.vname = nbr;
						np.psf = rp.psf + nbr + "  ";
						np.min_dis = rp.min_dis + rpvtx.nbrs.get(nbr); 
						//np.min_time = rp.min_time + 120 + 40*rpvtx.nbrs.get(nbr); 
						stack.addFirst(np);
					}
				}
			}
			ans = ans + Integer.toString(min);
			return ans;
		}
		
		
		
		
		public String Get_Minimum_Time(String src, String dst) 
		{
			int min = Integer.MAX_VALUE;
			String ans = "";
			HashMap<String, Boolean> processed = new HashMap<>();
			LinkedList<Pair> stack = new LinkedList<>();

			// create a new pair
			Pair sp = new Pair();
			sp.vname = src;
			sp.psf = src + "  ";
			sp.min_dis = 0;
			sp.min_time = 0;
			
			// put the new pair in queue
			stack.addFirst(sp);

			// while queue is not empty keep on doing the work
			while (!stack.isEmpty()) {

				// remove a pair from queue
				Pair rp = stack.removeFirst();

				if (processed.containsKey(rp.vname)) 
				{
					continue;
				}

				// processed put
				processed.put(rp.vname, true);

				//if there exists a direct edge b/w removed pair and destination vertex
				if (rp.vname.equals(dst)) 
				{
					int temp = rp.min_time;
					if(temp<min) {
						ans = rp.psf;
						min = temp;
					}
					continue;
				}

				Vertex rpvtx = vtces.get(rp.vname);
				ArrayList<String> nbrs = new ArrayList<>(rpvtx.nbrs.keySet());

				for (String nbr : nbrs) 
				{
					// process only unprocessed nbrs
					if (!processed.containsKey(nbr)) {

						// make a new pair of nbr and put in queue
						Pair np = new Pair();
						np.vname = nbr;
						np.psf = rp.psf + nbr + "  ";
						//np.min_dis = rp.min_dis + rpvtx.nbrs.get(nbr);
						np.min_time = rp.min_time + 60*rpvtx.nbrs.get(nbr); 
						stack.addFirst(np);
					}
				}
			}
			Double minutes = Math.ceil((double)min / 60);
			ans = ans + Double.toString(minutes);
			return ans;
		}
		
		public ArrayList<String> get_Interchanges(String str)
		{
			ArrayList<String> arr = new ArrayList<>();
			String res[] = str.split("  ");
			arr.add(res[0]);
			int count = 0;
			for(int i=1;i<res.length-1;i++)
			{
				int index = res[i].indexOf('~');
				String s = res[i].substring(index+1);
				
				if(s.length()==2)
				{
					String prev = res[i-1].substring(res[i-1].indexOf('~')+1);
					String next = res[i+1].substring(res[i+1].indexOf('~')+1);
					
					if(prev.equals(next)) 
					{
						arr.add(res[i]);
					}
					else
					{
						arr.add(res[i]+" ==> "+res[i+1]);
						i++;
						count++;
					}
				}
				else
				{
					arr.add(res[i]);
				}
			}
			arr.add(Integer.toString(count));
			arr.add(res[res.length-1]);
			return arr;
		}
		
		public static void Create_Local_Map(Graph_M g)
		{
			g.addVertex("Chatrapati_Shivaji_Terminus");
			g.addVertex("Masjid_Bunder");
			g.addVertex("Sandhurst_Road");
			g.addVertex("Byculla");
			g.addVertex("Chinchpokli");
			g.addVertex("Currey_Road");
			g.addVertex("Parel");
			g.addVertex("Dadar");
			g.addVertex("Matunga");
			g.addVertex("Sion");
			g.addVertex("Kurla");
			g.addVertex("Vidyavihar");
			g.addVertex("Ghatkopar");
			g.addVertex("Vikhroli");
			g.addVertex("Kanjurmarg");
			g.addVertex("Bhandup");
			g.addVertex("Nahur");
			g.addVertex("Mulund");
			g.addVertex("Thane");
			g.addVertex("Kalwa");
			g.addVertex("Mumbra");
			g.addVertex("Diva");
			g.addVertex("Kopar");
			g.addVertex("Dombivli");
			g.addVertex("Thakurli");
			g.addVertex("Kalyan");
			g.addVertex("Shahad");
			g.addVertex("Ambivli");
			g.addVertex("Titwala");
			g.addVertex("Khadavali");
			g.addVertex("Vashind");
			g.addVertex("Asangaon");
			g.addVertex("Atgaon");
			g.addVertex("Thansit");
			g.addVertex("Kardi");
			g.addVertex("Umbermali");
			g.addVertex("Kasara");
			g.addVertex("Vitthalvadi");
			g.addVertex("Ulhasnagar");
			g.addVertex("Ambernath");
			g.addVertex("Badlapur");
			g.addVertex("Vangani");
			g.addVertex("Shelu");
			g.addVertex("Neral");
			g.addVertex("Bhivpuri_Road");
			g.addVertex("Karjat");
			g.addVertex("Palasdhari");
			g.addVertex("Kelavali");
			g.addVertex("Dolavali");
			g.addVertex("Lowjee");
			g.addVertex("Khopoli");
			g.addVertex("Airoli");
			g.addVertex("Rabale");
			g.addVertex("Ghansoli");
			g.addVertex("Kopar_Khairane");
			g.addVertex("Turbhe");
			g.addVertex("Dockyard_Road");
			g.addVertex("Reay_Road");
			g.addVertex("Cotton_Green");
			g.addVertex("Sewri");
			g.addVertex("Vadala");
			g.addVertex("Guru_Tegh_Bahadur_Nagar");
			g.addVertex("Chunabhatti");
			g.addVertex("Tilak_Nagar");
			g.addVertex("Chembur");
			g.addVertex("Govandi");
			g.addVertex("Mankhurd");
			g.addVertex("Vashi");
			g.addVertex("Sanpada");
			g.addVertex("Juinagar");
			g.addVertex("Nerul");
			g.addVertex("Seawoods-Darave");
			g.addVertex("CBD_Belapur");
			g.addVertex("Kharghar");
			g.addVertex("Manasarovar");
			g.addVertex("Khandeshwar");
			g.addVertex("Panvel");
			g.addVertex("Churchgate");
			g.addVertex("Marine_Lines");
			g.addVertex("Charni_Road");
			g.addVertex("Grant_Road");
			g.addVertex("Mumbai_Central");
			g.addVertex("Mahalaxmi");
			g.addVertex("Lower_Parel");
			g.addVertex("Prabhadevi");
			g.addVertex("Matunga-Road");
			g.addVertex("Mahim");
			g.addVertex("Bandra");
			g.addVertex("Khar_Road");
			g.addVertex("Santacruz");
			g.addVertex("Vile_Parle");
			g.addVertex("Andheri");
			g.addVertex("Jogeshwari");
			g.addVertex("Ram_Mandir");
			g.addVertex("Goregaon");
			g.addVertex("Malad");
			g.addVertex("Kandivali");
			g.addVertex("Borivali");
			g.addVertex("Dahisar");
			g.addVertex("Mira_Road");
			g.addVertex("Bhayandar");
			g.addVertex("Naigaon");
			g.addVertex("Vasai_Road");
			g.addVertex("Nala_Sopara");
			g.addVertex("Virar");
			g.addVertex("Vaitarna");
			g.addVertex("Saphale");
			g.addVertex("Kelve_Road");
			g.addVertex("Palghar");
			g.addVertex("Umroli");
			g.addVertex("Boisar");
			g.addVertex("Vangaon");
			g.addVertex("Dahanu_Road");
			g.addVertex("King's_Circle");
			
			
			g.addEdge("Chatrapati_Shivaji_Terminus", "Masjid_Bunder", 1);
			g.addEdge("Masjid_Bunder", "Sandhurst_Road", 1);
			g.addEdge("Sandhurst_Road", "Byculla", 2);
			g.addEdge("Byculla", "Chinchpokli", 1);
			g.addEdge("Chinchpokli", "Currey_Road", 1);
			g.addEdge("Currey_Road", "Parel", 2);
			g.addEdge("Parel", "Dadar", 1);
			g.addEdge("Dadar", "Matunga", 1);
			g.addEdge("Matunga", "Sion", 1);
			g.addEdge("Sion", "Kurla", 1);
			g.addEdge("Kurla", "Vidyavihar", 2);
			g.addEdge("Vidyavihar", "Ghatkopar", 2);
			g.addEdge("Ghatkopar", "Vikhroli", 4);
			g.addEdge("Vikhroli", "Kanjurmarg", 2);
			g.addEdge("Kanjurmarg", "Bhandup", 2);
			g.addEdge("Bhandup", "Nahur", 1);
			g.addEdge("Nahur", "Mulund", 2);
			g.addEdge("Mulund", "Thane", 2);
			g.addEdge("Thane", "Kalwa", 2);
			g.addEdge("Kalwa", "Mumbra", 3);
			g.addEdge("Mumbra", "Diva", 3);
			g.addEdge("Diva", "Kopar", 4);
			g.addEdge("Kopar", "Dombivli", 1);
			g.addEdge("Dombivli", "Thakurli", 1);
			g.addEdge("Thakurli", "Kalyan", 3);
			g.addEdge("Kalyan", "Shahad", 3);
			g.addEdge("Shahad", "Ambivli", 2);
			g.addEdge("Ambivli", "Titwala", 6);
			g.addEdge("Titwala", "Khadavali", 7);
			g.addEdge("Khadavali", "Vashind", 8);
			g.addEdge("Vashind", "Asangaon", 6);
			g.addEdge("Asangaon", "Atgaon", 9);
			g.addEdge("Atgaon", "Thansit", 7);
			g.addEdge("Thansit", "Kardi", 5);
			g.addEdge("Kardi", "Umbermali", 6);
			g.addEdge("Umbermali", "Kasara", 12);
			
			g.addEdge("Kalyan", "Vitthalvadi", 2);
			g.addEdge("Vitthalvadi", "Ulhasnagar", 1);
			g.addEdge("Ulhasnagar", "Ambernath", 3);
			g.addEdge("Ambernath", "Badlapur", 7);
			g.addEdge("Badlapur", "Vangani", 11);
			g.addEdge("Vangani", "Shelu", 4);
			g.addEdge("Shelu", "Neral", 4);
			g.addEdge("Neral", "Bhivpuri_Road", 7);
			g.addEdge("Bhivpuri_Road", "Karjat", 7);
			g.addEdge("Karjat", "Palasdhari", 3);
			g.addEdge("Palasdhari", "Kelavali", 5);
			g.addEdge("Kelavali", "Dolavali", 1);
			g.addEdge("Dolavali", "Lowjee", 3);
			g.addEdge("Lowjee", "Khopoli", 3);
			
			g.addEdge("Thane", "Airoli", 8);
			g.addEdge("Airoli", "Rabale", 3);
			g.addEdge("Rabale", "Ghansoli", 3);
			g.addEdge("Ghansoli", "Kopar_Khairane", 3);
			g.addEdge("Kopar_Khairane", "Turbhe", 4);
			g.addEdge("Turbhe", "Sanpada", 3);
			g.addEdge("Turbhe", "Juinagar", 5);
			g.addEdge("Juinagar", "Nerul", 3);
			g.addEdge("Nerul", "Seawoods-Darave", 3);
			g.addEdge("Seawoods-Darave", "CBD_Belapur", 4);
			g.addEdge("CBD_Belapur", "Kharghar", 4);
			g.addEdge("Kharghar", "Manasarovar", 3);
			g.addEdge("Manasarovar", "Khandeshwar", 3);
			g.addEdge("Khandeshwar", "Panvel", 3);
			
			g.addEdge("Sandhurst_Road", "Dockyard_Road", 2);
			g.addEdge("Dockyard_Road", "Reay_Road", 1);
			g.addEdge("Reay_Road", "Cotton_Green", 1);
			g.addEdge("Cotton_Green", "Sewri", 1);
			g.addEdge("Sewri", "Vadala", 2);
			g.addEdge("Vadala", "Guru_Tegh_Bahadur_Nagar", 2);
			g.addEdge("Guru_Tegh_Bahadur_Nagar", "Chunabhatti", 2);
			g.addEdge("Chunabhatti", "Kurla", 2);
			g.addEdge("Kurla", "Tilak_Nagar", 2);
			g.addEdge("Tilak_Nagar", "Chembur", 4);
			g.addEdge("Chembur", "Govandi", 3);
			g.addEdge("Govandi", "Mankhurd", 2);
			g.addEdge("Mankhurd", "Vashi", 8);
			g.addEdge("Vashi", "Sanpada", 3);
			g.addEdge("Sanpada", "Juinagar", 3);
			
			g.addEdge("Mahim", "King's_Circle", 4);
			g.addEdge("King's_Circle", "Vadala", 2);
			g.addEdge("Churchgate", "Marine_Lines", 1);
			g.addEdge("Marine_Lines", "Charni_Road", 1);
			g.addEdge("Charni_Road", "Grant_Road", 1);
			g.addEdge("Grant_Road", "Mumbai_Central", 1);
			g.addEdge("Mumbai_Central", "Mahalaxmi", 1);
			g.addEdge("Mahalaxmi", "Lower_Parel", 2);
			g.addEdge("Lower_Parel", "Prabhadevi", 1);
			g.addEdge("Prabhadevi", "Dadar", 1);
			g.addEdge("Dadar", "Matunga-Road", 2);
			g.addEdge("Matunga-Road", "Mahim", 1);
			g.addEdge("Mahim", "Bandra", 2);
			g.addEdge("Bandra", "Khar_Road", 2);
			g.addEdge("Khar_Road", "Santacruz", 1);
			g.addEdge("Santacruz", "Vile_Parle", 2);
			g.addEdge("Vile_Parle", "Andheri", 2);
			g.addEdge("Andheri", "Jogeshwari", 2);
			g.addEdge("Jogeshwari", "Goregaon", 2);
			g.addEdge("Goregaon", "Malad", 2);
			g.addEdge("Malad", "Kandivali", 1);
			g.addEdge("Kandivali", "Borivali", 3);
			g.addEdge("Borivali", "Dahisar", 2);
			g.addEdge("Dahisar", "Mira_Road", 3);
			g.addEdge("Mira_Road", "Bhayandar", 3);
			g.addEdge("Bhayandar", "Naigaon", 5);
			g.addEdge("Naigaon", "Vasai_Road", 4);
			g.addEdge("Vasai_Road", "Nala_Sopara", 4);
			g.addEdge("Nala_Sopara", "Virar", 4);
			g.addEdge("Virar", "Vaitarna", 8);
			g.addEdge("Vaitarna", "Saphale", 8);
			g.addEdge("Saphale", "Kelve_Road", 6);
			g.addEdge("Kelve_Road", "Palghar", 8);
			g.addEdge("Palghar", "Umroli", 6);
			g.addEdge("Umroli","Boisar", 6);
			g.addEdge("Boisar", "Vangaon", 9);
			g.addEdge("Vangaon", "Dahanu_Road", 12);
			
			
		}
		
		public static String[] printCodelist()
		{
			System.out.println("List of station along with their codes:\n");
			HashMap<String, String> code = new HashMap<>();
			code.put("Chatrapati_Shivaji_Terminus", "ST");
			code.put("Masjid_Bunder", "MSD");
			code.put("Sandhurst_Road", "SNRD");
			code.put("Byculla", "BY");
			code.put("Chinchpokli", "CHP");
			code.put("Currey_Road", "CRD");
			code.put("Parel", "PR");
			code.put("Dadar", "D");
			code.put("Matunga", "MTN");
			code.put("Sion", "SIN");
			code.put("Kurla", "C");
			code.put("Vidyavihar", "VVH");
			code.put("Ghatkopar", "GC");
			code.put("Vikhroli", "VK");
			code.put("Kanjurmarg", "KJRD");
			code.put("Bhandup", "BND");
			code.put("Nahur", "NHU");
			code.put("Mulund", "MLND");
			code.put("Thane", "T");
			code.put("Kalwa", "KLVA");
			code.put("Mumbra", "MBQ");
			code.put("Diva", "DIVA");
			code.put("Kopar", "KOPR");
			code.put("Dombivli", "DI");
			code.put("Thakurli", "THK");
			code.put("Kalyan", "K");
			code.put("Shahad", "SHAD");
			code.put("Ambivli", "ABY");
			code.put("Titwala", "TL");
			code.put("Khadavali", "KDV");
			code.put("Vashind", "VSD");
			code.put("Asangaon", "AN");
			code.put("Atgaon", "ATG");
			code.put("Thansit", "THS");
			code.put("Kardi", "KE");
			code.put("Umbermali", "OMB");
			code.put("Kasara", "KSRA");
			code.put("Vitthalvadi", "VLDI");
			code.put("Ulhasnagar", "ULNR");
			code.put("Ambernath", "A");
			code.put("Badlapur", "BL");
			code.put("Vangani", "VGI");
			code.put("Shelu", "SHLU");
			code.put("Neral", "NRL");
			code.put("Bhivpuri_Road", "BVS");
			code.put("Karjat", "KJT");
			code.put("Palasdhari", "PDI");
			code.put("Kelavali", "KLY");
			code.put("Dolavali", "DLV");
			code.put("Lowjee", "LWJ");
			code.put("Khopoli", "KHPL");
			code.put("Airoli", "AIRL");
			code.put("Rabale", "RABE");
			code.put("Ghansoli", "GNSL");
			code.put("Kopar_Khairane", "KPHN");
			code.put("Turbhe", "TUH");
			code.put("Dockyard_Road", "DKRD");
			code.put("Reay_Road", "RRD");
			code.put("Cotton_Green", "CTGN");
			code.put("Sewri", "SVE");
			code.put("Vadala", "VD");
			code.put("Guru_Tegh_Bahadur_Nagar", "GTBN");
			code.put("Chunabhatti", "CHF");
			code.put("Tilak_Nagar", "TKNG");
			code.put("Chembur", "CM");
			code.put("Govandi", "GV");
			code.put("Mankhurd", "MN");
			code.put("Vashi", "VA");
			code.put("Sanpada", "SNCR");
			code.put("Juinagar", "JNJ");
			code.put("Nerul", "NU");
			code.put("Seawoods-Darave", "SWDV");
			code.put("CBD_Belapur", "BR");
			code.put("Kharghar", "KHAG");
			code.put("Manasarovar", "MANR");
			code.put("Khandeshwar", "KNDS");
			code.put("Panvel", "PL");
			code.put("Churchgate", "CCG");
			code.put("Marine_Lines", "MEL");
			code.put("Charni_Road", "CYR");
			code.put("Grant_Road", "GTR");
			code.put("Mumbai_Central", "BCL");
			code.put("Mahalaxmi", "MX");
			code.put("Lower_Parel", "PL");
			code.put("Prabhadevi", "PBHD");
			code.put("Matunga-Road", "MRU");
			code.put("Mahim", "MM");
			code.put("Bandra", "BA");
			code.put("Khar_Road", "KHAR");
			code.put("Santacruz", "STC");
			code.put("Vile_Parle", "VLP");
			code.put("Andheri", "AD");
			code.put("Jogeshwari", "JOS");
			code.put("Ram_Mandir", "RMAR");
			code.put("Goregaon", "G");
			code.put("Malad", "MDD");
			code.put("Kandivali", "KILE");
			code.put("Borivali", "BO");
			code.put("Dahisar", "DIC");
			code.put("Mira_Road", "MIRA");
			code.put("Bhayandar", "BY");
			code.put("Naigaon", "NG");
			code.put("Vasai_Road", "BSR");
			code.put("Nala_Sopara", "NSP");
			code.put("Virar", "VR");
			code.put("Vaitarna", "VTN");
			code.put("Saphale", "SAH");
			code.put("Kelve_Road", "KLV");
			code.put("Palghar", "PLG");
			code.put("Umroli", "UOI");
			code.put("Boisar", "BOR");
			code.put("Vangaon", "VGN");
			code.put("Dahanu_Road", "DRD");
			code.put("King's_Circle", "KCE");
			
			
			ArrayList<String> keys = new ArrayList<>(vtces.keySet());
			int i=1,j=0,m=1;
			StringTokenizer stname;
			String temp="";
			String codes[] = new String[keys.size()];
//			for(String key : keys) {
//				for(int z=0;i<keys.size();i++) {
//					codes[z]=code.get(key);
//					System.out.println(code.get(key));
//				}
//			}
			char c;
			for(String key : keys) 
			{
				codes[i-1]=code.get(key);
//				System.out.println(code.get(key));
//				stname = new StringTokenizer(key);
//				codes[i-1] = "";
//				j=0;
//				while (stname.hasMoreTokens())
//				{
//				        temp = stname.nextToken();
//				        c = temp.charAt(0);
//				        while (c>47 && c<58)
//				        {
//				                codes[i-1]+= c;
//				                j++;
//				                c = temp.charAt(j);
//				        }
//				        if ((c<48 || c>57) && c<123)
//				                codes[i-1]+= c;
//				}
//				if (codes[i-1].length() < 2)
//					codes[i-1]+= Character.toUpperCase(temp.charAt(1));
				            
				System.out.print(i + ". " + key + "\t");
				if (key.length()<(22-m))
                    			System.out.print("\t");
				if (key.length()<(14-m))
                    			System.out.print("\t");
                    		if (key.length()<(6-m))
                    			System.out.print("\t");
                    		System.out.println(code.get(key));
				i++;
				if (i == (int)Math.pow(10,m))
				        m++;
			}
			return codes;
		}
		
		public static void main(String[] args) throws IOException
		{
			Graph_M g = new Graph_M();
			Create_Local_Map(g);
//			System.out.println(vtces.keySet().size());
			System.out.println("\n\t\t\t****WELCOME TO THE MUMBAI LOCAL*****");
			// System.out.println("\t\t\t\t~~LIST OF ACTIONS~~\n\n");
			// System.out.println("1. LIST ALL THE STATIONS IN THE MAP");
			// System.out.println("2. SHOW THE Local MAP");
			// System.out.println("3. GET SHORTEST DISTANCE FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
			// System.out.println("4. GET SHORTEST TIME TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
			// System.out.println("5. GET SHORTEST PATH (DISTANCE WISE) TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
			// System.out.println("6. GET SHORTEST PATH (TIME WISE) TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
			// System.out.print("\nENTER YOUR CHOICE FROM THE ABOVE LIST : ");
			BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
			// int choice = Integer.parseInt(inp.readLine());
			//STARTING SWITCH CASE
			while(true)
			{
				System.out.println("\t\t\t\t~~LIST OF ACTIONS~~\n\n");
				System.out.println("1. LIST ALL THE STATIONS IN THE MAP");
				System.out.println("2. SHOW THE Local MAP");
				System.out.println("3. GET SHORTEST DISTANCE FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
				System.out.println("4. GET SHORTEST TIME TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
				System.out.println("5. GET SHORTEST PATH (DISTANCE WISE) TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
				System.out.println("6. GET SHORTEST PATH (TIME WISE) TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
				System.out.println("7. EXIT THE MENU");
				System.out.print("\nENTER YOUR CHOICE FROM THE ABOVE LIST (1 to 7) : ");
				int choice = -1;
				try {
					choice = Integer.parseInt(inp.readLine());
				} catch(Exception e) {
					// default will handle
				}
				System.out.print("\n***********************************************************\n");
				if(choice == 7)
				{
					System.exit(0);
				}
				switch(choice)
				{
				case 1:
					g.display_Stations();
					break;
			
				case 2:
					g.display_Map();
					break;
				
				case 3:
					ArrayList<String> keys = new ArrayList<>(vtces.keySet());
					String codes[] = printCodelist();
					System.out.println("\n1. TO ENTER SERIAL NO. OF STATIONS\n2. TO ENTER CODE OF STATIONS\n3. TO ENTER NAME OF STATIONS\n");
					System.out.println("ENTER YOUR CHOICE:");
				        int ch = Integer.parseInt(inp.readLine());
					int j;
						
					String st1 = "", st2 = "";
//					System.out.println("ENTER THE SOURCE AND DESTINATION STATIONS");
					if (ch == 1)
					{
						System.out.print("ENTER THE SOURCE STATION: ");
					    st1 = keys.get(Integer.parseInt(inp.readLine())-1);
					    System.out.print("ENTER THE DESTINATION STATION: ");
					    st2 = keys.get(Integer.parseInt(inp.readLine())-1);
					}
					else if (ch == 2)
					{
//						for(int i=0;i<codes.length;i++) {
//							System.out.println(codes[i]);
//						}
						System.out.print("ENTER THE SOURCE STATION: ");
					    String a,b;
					    a = (inp.readLine()).toUpperCase();
					    for (j=0;j<keys.size();j++)
					       if (a.equals(codes[j]))
					           break;
					    st1 = keys.get(j);
					    System.out.print("ENTER THE DESTINATION STATION: ");
					    b = (inp.readLine()).toUpperCase();
					    for (j=0;j<keys.size();j++)
					       if (b.equals(codes[j]))
					           break;
					    st2 = keys.get(j);
					}
					else if (ch == 3)
					{
						System.out.print("ENTER THE SOURCE STATION: ");
						st1 = inp.readLine();
						System.out.print("ENTER THE DESTINATION STATION: ");
						st2 = inp.readLine();
//					    st1 = inp.readLine();
//					    st2 = inp.readLine();
					}
					else
					{
					    System.out.println("Invalid choice");
					    System.exit(0);
					}
		
					HashMap<String, Boolean> processed = new HashMap<>();
//					System.out.println(g.hasPath(st1, st2, processed));
					if(!g.containsVertex(st1) || !g.containsVertex(st2) || !g.hasPath(st1, st2, processed))
						System.out.println("THE INPUTS ARE INVALID");
					else
					System.out.println("SHORTEST DISTANCE FROM "+st1+" TO "+st2+" IS "+g.dijkstra(st1, st2, false, false)+"KM\n");
					break;
				
				case 4:
					System.out.print("ENTER THE SOURCE STATION: ");
					String sat1 = inp.readLine();
					System.out.print("ENTER THE DESTINATION STATION: ");
					String sat2 = inp.readLine();
				
					HashMap<String, Boolean> processed1= new HashMap<>();				
					System.out.println("SHORTEST TIME FROM ("+sat1+") TO ("+sat2+") IS "+g.dijkstra(sat1, sat2, true, false)/60+" MINUTES\n\n");
					break;
				
				case 5:
					System.out.print("ENTER THE SOURCE STATION: ");
					String s1 = inp.readLine();
					System.out.print("ENTER THE DESTINATION STATION: ");
					String s2 = inp.readLine();
				
					HashMap<String, Boolean> processed2 = new HashMap<>();
					if(!g.containsVertex(s1) || !g.containsVertex(s2) || !g.hasPath(s1, s2, processed2))
						System.out.println("THE INPUTS ARE INVALID");
					else 
					{	
//						String s=g.dijkstra(s1, s2, true, "");
						ArrayList<String> str = g.get_Interchanges(g.Get_Minimum_Distance(s1, s2));
						int len = str.size();
						System.out.println("SOURCE STATION : " + s1);
						System.out.println("SOURCE STATION : " + s2);
						System.out.println("DISTANCE : " + g.dijkstra(s1, s2, false, false));
//						System.out.println("NUMBER OF INTERCHANGES : " + str.get(len-2));
//						System.out.println(s);
						System.out.println("~~~~~~~~~~~~~");
						g.dijkstra(s1, s2, false, true);
//						System.out.println("START  ==>  " + str.get(0));
//						for(int i=1; i<len-3; i++)
//						{
//							System.out.println(str.get(i));
//						}
//						System.out.print(str.get(len-3) + "   ==>    END");
						System.out.println("\n~~~~~~~~~~~~~");
					}
					break;
				
				case 6:
					System.out.print("ENTER THE SOURCE STATION: ");
					String ss1 = inp.readLine();
					System.out.print("ENTER THE DESTINATION STATION: ");
					String ss2 = inp.readLine();
				
					HashMap<String, Boolean> processed3 = new HashMap<>();
					if(!g.containsVertex(ss1) || !g.containsVertex(ss2) || !g.hasPath(ss1, ss2, processed3))
						System.out.println("THE INPUTS ARE INVALID");
					else
					{
						ArrayList<String> str = g.get_Interchanges(g.Get_Minimum_Time(ss1, ss2));
						int len = str.size();
						System.out.println("SOURCE STATION : " + ss1);
						System.out.println("DESTINATION STATION : " + ss2);
						System.out.println("TIME : " + g.dijkstra(ss1, ss2, true, false)/60+" MINUTES");
//						System.out.println("NUMBER OF INTERCHANGES : " + str.get(len-2));
						//System.out.println(str);
						System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//						System.out.print("START  ==>  " + str.get(0) + " ==>  ");
						g.dijkstra(ss1, ss2, false, true);
//						for(int i=1; i<len-3; i++)
//						{
//							System.out.println(str.get(i));
//						}
//						System.out.print(str.get(len-3) + "   ==>    END");
						System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					}
					break;	
               	         default:  //If switch expression does not match with any case, 
                	        	//default statements are executed by the program.
                            	//No break is needed in the default case
                    	        System.out.println("Please enter a valid option! ");
                        	    System.out.println("The options you can choose are from 1 to 6. ");
                            
				}
			}
			
		}	
	}
