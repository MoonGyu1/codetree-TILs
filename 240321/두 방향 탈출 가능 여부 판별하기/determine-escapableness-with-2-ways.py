def dfs(x, y):
    if x == n - 1 and y == m - 1:
        global success
        success = 1
        return
    for i, j in zip(move_x, move_y):
        next_x, next_y = x + i, y + j
        if can_go(next_x, next_y):
            visited[next_x][next_y] = True
            dfs(next_x, next_y)

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
move_x, move_y = [1, 0], [0, 1]
success = 0

visited[x][y] = True
dfs(x, y)

print(success)