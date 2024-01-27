n, m = map(int, input().split())

grid = [list(map(int, input().split())) for _ in range(n)]

cnt = 0

for i in range(n):
    same_cnt = 1
    before = -1
    for j in range(n):
        if grid[i][j] == before:
            same_cnt += 1
        before = grid[i][j]
    if same_cnt >= m:
        cnt+=1
    
for i in range(n):
    same_cnt = 1
    before = -1
    for j in range(n):
        if grid[j][i] == before:
            same_cnt += 1
        before = grid[j][i]
    if same_cnt >= m:
        cnt+=1

print(cnt)