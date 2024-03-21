def dfs(v):
    global cnt

    for i in graph[v]:
        if not visited[i]:
            visited[i] = True
            cnt += 1
            dfs(i)

n, m = map(int, input().split())

graph = [[] for _ in range(n+1)]
visited = [False for _ in range(n + 1)]

for i in range(m):
    x, y = map(int, input().split())
    graph[x].append(y)
    graph[y].append(x)

cnt = 0
visited[1] = True
dfs(1)

print(cnt)