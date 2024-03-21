def dfs(x, y):
    if x == n - 1 and y == m - 1:
        success = 1
        return
    for next_x, next_y in zip(move_x, move_y):
        if can_go(next_x, next_y):
            visited[next_x][next_y] = True
            dfs(next_x, next_y)

def can_go(x, y):
    if graph[x][y] == 0:
        return False
    if in_range(x, y) and not visited[x][y]:
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

dfs(x, y)
print(success)