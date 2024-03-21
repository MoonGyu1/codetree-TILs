from collections import deque

def can_go(x, y):
    return in_range(x, y) and graph[x][y] != 0 and not visited[x][y]

def in_range(x, y):
    return 0 <= x < n and 0 <= y < m

n, m = map(int, input().split())
graph = [[] for _ in range(n)]
visited = [[False for _ in range(m)] for _ in range(m)]
for i in range(n):
    graph[i] = list(map(int, input().split()))
    
x, y = 0, 0
dxs, dys = [-1, 1, 0, 0], [0, 0, -1, 1]
success = 0

q = deque()

def bfs():
    while q:
        v = q.popleft()
        for dx, dy in zip(dxs, dys):
            next_x = v[0] + dx
            next_y = v[1] + dy
            if can_go(next_x, next_y):
                visited[next_x][next_y] = True
                q.append((next_x, next_y))

visited[0][0] = True
q.append((0, 0))

bfs()

success = 1 if visited[n-1][m-1] else 0
print(success)