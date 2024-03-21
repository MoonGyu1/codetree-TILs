def dfs(v):
    if visited[v]:
        if v!=1:
            s.add(v)
        return
    visited[v] = True
    for i in graph[v]:
        dfs(i)

n, m = map(int, input().split())

graph = [[] for _ in range(n+1)]
visited = [False] * n

for i in range(m):
    x, y = map(int, input().split())
    graph[x].append(y)
    graph[y].append(x)

s = set()

dfs(1)

print(len(s))