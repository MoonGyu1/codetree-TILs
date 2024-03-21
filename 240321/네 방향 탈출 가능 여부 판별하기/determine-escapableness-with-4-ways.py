from collections import deque

# 변수 선언 및 입력
n, m = tuple(map(int, input().split()))

a = [
    list(map(int, input().split()))
    for _ in range(n)
]

visited = [
    [False for _ in range(m)]
    for _ in range(n)
]

q = deque()

# 주어진 위치가 격자를 벗어나는지 여부를 반환합니다.
def in_range(x, y):
    return 0 <= x and x < n and 0 <= y and y < m


# 주어진 위치로 이동할 수 있는지 여부를 확인합니다.
def can_go(x, y):
    return in_range(x, y) and a[x][y] and not visited[x][y]


def bfs():
    # queue에 남은 것이 없을때까지 반복합니다.
    while q:
        # queue에서 가장 먼저 들어온 원소를 뺍니다.
        x, y = q.popleft()
        
        # queue에서 뺀 원소의 위치를 기준으로 4방향을 확인해봅니다.
        dxs, dys = [0, 1, 0, -1], [1, 0, -1, 0]
        for dx, dy in zip(dxs, dys):
            new_x, new_y = x + dx, y + dy
        
            # 아직 방문한 적이 없으면서 갈 수 있는 곳이라면
            # 새로 queue에 넣어주고 방문 여부를 표시해줍니다.
            if can_go(new_x, new_y):
                q.append((new_x, new_y))
                visited[new_x][new_y] = True

                
# bfs를 이용해 최소 이동 횟수를 구합니다.
# 시작점을 queue에 넣고 시작합니다.
q.append((0, 0))
visited[0][0] = True

bfs()

# 우측 하단을 방문한 적이 있는지 여부를 출력합니다.
answer = 1 if visited[n - 1][m - 1] else 0
print(answer)