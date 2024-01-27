n, m = map(int, input().split())

grid = [list(map(int, input().split())) for _ in range(n)]

def is_happy():
    same_cnt, max_cnt = 1, 1
    for i in range(1, n):
        if(seq[i - 1] == seq[i]):
            same_cnt += 1
        else:
            same_cnt = 1
        
        max_cnt = max(max_cnt, same_cnt)
    return max_cnt >= m

cnt = 0

for i in range(n):
    seq = grid[i][:]
    
    if is_happy():
        cnt += 1

for j in range(n):
    for i in range(n):
        seq[i] = grid[i][j]
    if is_happy():
        cnt += 1

print(cnt)