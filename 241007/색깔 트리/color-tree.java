import java.util.*;
import java.io.*;

public class Main {
	static BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
	static HashMap<Integer, Node> nodes = new HashMap<>();
	
	public static void main(String[] args) throws Exception {
		// System.setIn(new FileInputStream("src/s202401_am_1/input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		ArrayList<Tree> trees = new ArrayList<>();

		int q = Integer.parseInt(br.readLine());
		while (q-- > 0) {
			st = new StringTokenizer(br.readLine());
			String command = st.nextToken();
			
			switch(command) {
				case "100": // 노드 추가
					int m_id = Integer.parseInt(st.nextToken());
					int p_id = Integer.parseInt(st.nextToken());
					int color = Integer.parseInt(st.nextToken());
					int max_depth = Integer.parseInt(st.nextToken());
					
					// 부모 노드의 maxDepth에 위배되는 경우 추가 X
					if(p_id != -1) {
						Node p = nodes.get(p_id);
						
						int pDepth = Math.max(p.currentDepth, 2);
						while(pDepth <= p.maxDepth && p.pid != -1) {
							p = nodes.get(p.pid);
							pDepth = Math.max(p.currentDepth, pDepth + 1);
						}
						
						if(p.pid != -1) { // 위배
							continue;
						} else {
							nodes.get(p_id).children.add(m_id);
						}
					} else {
						Tree t = new Tree();
						t.root = m_id;
						trees.add(t);
					}

					Node node = new Node();
					node.id = m_id;
					node.pid = p_id;
					node.color = color;
					node.maxDepth = max_depth;
					node.currentDepth = 1;
								
					// 주의: id가 -1인 노드는 인스턴스 자체가 없음 -> 프로퍼티 호출 시 null 참조 에러 발생
					if(p_id != -1) {
						Node p = nodes.get(node.pid);
						int currentDepth = 2;
						while(p.pid != -1) {
							p.currentDepth = Math.max(p.currentDepth, currentDepth);
							p = nodes.get(p.pid);
							currentDepth++;
						}
						p.currentDepth = Math.max(p.currentDepth, currentDepth);
					}
			
					nodes.put(m_id, node);
					
//					for(Map.Entry<Integer, Node> e : nodes.entrySet()) {
//						System.out.println("id: " + e.getKey() + " node: " + e.getValue().pid);
//					}
					
					break;
				case "200": // 색깔 변경
					int rootId = Integer.parseInt(st.nextToken());
					int toBeColor = Integer.parseInt(st.nextToken());
					
					nodes.get(rootId).color = toBeColor;
					
					// 주의: 새로운 인스턴스 할당해야 별도의 리스트로 관리 가능
					Queue<Integer> children = new LinkedList<>(nodes.get(rootId).children);
					while(!children.isEmpty()) {
						Node child = nodes.get(children.poll());
						child.color = toBeColor;
						for(int c : child.children) {
							children.add(c);
						}
					}
					
					break;
				case "300": // 색깔 조회
					int id = Integer.parseInt(st.nextToken());
					bw.write(String.format("%d\n", nodes.get(id).color));
					
					break;
				case "400": // 점수 조회
					int sum = 0;
					for(Tree t : trees) {
						HashSet<Integer> s = new HashSet<>();
						sum += getValueByInOrder(t.root);
					}

					bw.write(String.format("%d\n", sum));
					
					break;
			}
		}

		bw.flush();
		bw.close();
	}
	
	static int getValueByInOrder(int id) {
		if(nodes.get(id).children.size() == 0) {
			HashSet<Integer> s = new HashSet<>();
			s.add(nodes.get(id).color);
			nodes.get(id).value = s;
			return 1;
		}
		
		int value = 0;
		HashSet<Integer> s = new HashSet<>();
		for(int c : nodes.get(id).children) {
			value += getValueByInOrder(c);
			for(int v : nodes.get(c).value) {
				s.add(v);
			}
			s.add(nodes.get(id).color);
			nodes.get(id).value = s;
		}
		
		return value + (int) Math.pow(s.size(), 2);
	}

}

// 트리 노드 만들기
// id, color, maxDepth, currentDepth, pid, children[]
class Node {
	int id;
	int pid;
	ArrayList<Integer> children = new ArrayList<>();
	int color;
	int maxDepth;
	int currentDepth;
	HashSet<Integer> value;
}

// 트리 만들기
class Tree {
	int root;
}

// 노드 생성 시 해시맵에 트리 노드 저장하기 [id, node]