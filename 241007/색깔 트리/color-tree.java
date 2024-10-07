import java.util.*;
import java.io.*;

public class Main {
	static BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
	static HashMap<Integer, Node> nodes = new HashMap<>();
	
	public static void main(String[] args) throws Exception {
		// System.setIn(new FileInputStream("src/s202401_am_1/input2.txt"));

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
						
						if(p.pid != -1 || p.maxDepth < pDepth) { // 위배
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
					node.value.add(color);
								
					// 주의: id가 -1인 노드는 인스턴스 자체가 없음 -> 프로퍼티 호출 시 null 참조 에러 발생
					if(p_id != -1) {
						Node p = nodes.get(node.pid);
						int currentDepth = 2;
						while(p.pid != -1) {
							p.currentDepth = Math.max(p.currentDepth, currentDepth);
							p.value.add(color);
							
							p = nodes.get(p.pid);
							currentDepth++;
						}
						p.currentDepth = Math.max(p.currentDepth, currentDepth);
						p.value.add(color);
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
					nodes.get(rootId).value.clear();
					nodes.get(rootId).value.add(toBeColor);
					
					// 주의: 새로운 인스턴스 할당해야 별도의 리스트로 관리 가능
					Queue<Integer> children = new LinkedList<>(nodes.get(rootId).children);
					while(!children.isEmpty()) {
						Node child = nodes.get(children.poll());
						child.color = toBeColor;
						child.value.clear();
						child.value.add(toBeColor);
						for(int c : child.children) {
							children.add(c);
						}
					}
					
					int pid = nodes.get(rootId).pid;
					while(pid != -1) {
						Node p = nodes.get(pid);
						p.value.clear();
						for(int c : p.children) {
							for(int v : nodes.get(c).value) {
								p.value.add(v);
							}
						}
						p.value.add(p.color);
						pid = p.pid;
					}
					
					break;
				case "300": // 색깔 조회
					int id = Integer.parseInt(st.nextToken());
					bw.write(String.format("%d\n", nodes.get(id).color));
					
					break;
				case "400": // 점수 조회
					int sum = 0;
					for(Tree t : trees) {
						sum += getValueByInOrder(t.root);
					}
		
//					if(nodes.size() >= 5) System.out.printf("%s %s %s %s %s\n", nodes.get(1).value.toString(), nodes.get(2).value.toString(), nodes.get(4).value.toString(), nodes.get(5).value.toString(), nodes.get(6).value.toString());
					bw.write(String.format("%d\n", sum));
					
					break;
			}
		}
// 빨간색은 1, 주황색은 2, 노랑색은 3, 초록색은 4, 파란색은 5로
		bw.flush();
//		System.out.println("t: " + trees.size());
		bw.close(); // close 하면 System.out도 안됨
	}
	
	static int getValueByInOrder(int id) {
		if(nodes.get(id).children.size() == 0) {
//			HashSet<Integer> s = new HashSet<>();
//			s.add(nodes.get(id).color);
//			nodes.get(id).value = s;
			return 1;
		}
		
		int value = 0;
//		HashSet<Integer> s = new HashSet<>();
		for(int c : nodes.get(id).children) {
			value += getValueByInOrder(c);
//			for(int v : nodes.get(c).value) {
//				s.add(v);
//			}
//			s.add(nodes.get(id).color);
//			nodes.get(id).value = s;
		}
		
//		return value + (int) Math.pow(s.size(), 2);
		return value + (int) Math.pow(nodes.get(id).value.size(), 2);
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
	HashSet<Integer> value = new HashSet<>();
}

// 트리 만들기
class Tree {
	int root;
}

// 노드 생성 시 해시맵에 트리 노드 저장하기 [id, node]