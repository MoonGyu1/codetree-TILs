from collections import deque

def can_go(x, y):
    if not in_range(x, y):
        return False
    if not visited[x][y] and graph[x][y] != 0:
        return True
    return False

def in_range(x, y):
    if 0 <= x < n and 0 <= y < m:
        return True
    return False

n, m = map(int, input().split())
graph = [[] for _ in range(n)]
visited = [[False for _ in range(m)] for _ in range(m)]
for i in range(n):
    graph[i] = list(map(int, input().split()))
    
x, y = 0, 0
dxs, dys = [-1, 1, 0, 0], [0, 0, -1, 1]
success = 0

q = deque()
visited[x][y] = True
q.append((x, y))

while q and success != 1:
    v = q.popleft()
    for dx, dy in zip(dxs, dys):
        next_x = v[0] + dx
        next_y = v[1] + dy
        if can_go(next_x, next_y):
            if next_x == n - 1 and next_y == m - 1:
                success = 1
                break
            visited[next_x][next_y] = True
            q.append((next_x, next_y))


print(success)