n = int(input())

arr = []
for _ in range(n):
    arr.append(list(map(int, input().split())))

cnt=0
maxcnt=0
for i in range(0, n-2):
    for j in range(0, n-2):
        for k in range(3):
            for l in range(3):
                cnt+=arr[i+k][j+l]
        
        if maxcnt < cnt:
            maxcnt = cnt
        cnt = 0

print(maxcnt)