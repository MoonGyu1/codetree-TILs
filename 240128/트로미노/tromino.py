n, m = map(int, input().split())
grid = [list(map(int, input().split())) for _ in range(n)]

def get_first_block_max(row, col):
     # 상 우 하 좌
    rotate_row = [-1, 0, 1, 0]
    rotate_col = [0, 1, 0, -1]

    max_cnt = 0
    point = grid[row][col]
    for i in range(4):
        if 0 <= row + rotate_row[i] < n and 0 <= col + rotate_col[i] < m and 0 <= row + rotate_row[(i + 1) % 4] < n and 0 <= col + rotate_col[(i + 1) % 4] < m:
            cnt = point

            # 상우, 우하, 하좌, 좌상
            cnt += grid[row + rotate_row[i]][col + rotate_col[i]]
            cnt += grid[row + rotate_row[(i + 1) % 4]][col + rotate_col[(i + 1) % 4]]
            
            max_cnt = max(max_cnt, cnt)
    
    return max_cnt

def get_second_block_max(row, col):
    max_cnt = 0
    
    point = grid[row][col]
    if row + 2 < n:
        cnt = point + grid[row+1][col] + grid[row+2][col]
        max_cnt = max(max_cnt, cnt)
    if col + 2 < m:
        cnt = point + grid[row][col+1] + grid[row][col+2]
        max_cnt = max(max_cnt, cnt)
    
    return max_cnt

max_cnt = 0
for i in range(n):
    for j in range(m):
        first_max = get_first_block_max(i, j)
        max_cnt = max(max_cnt, first_max)

        second_max = get_second_block_max(i, j)
        max_cnt = max(max_cnt, second_max)

print(max_cnt)