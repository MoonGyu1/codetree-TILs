import heapq

n = int(input())
hq = []
def operator(c, v = None):
    if c == 'push':
        heapq.heappush(hq, -v)
    elif c == 'pop':
        print(-heapq.heappop(hq))
    elif c == 'size':
        print(len(hq))
    elif c == 'empty':
        if len(hq) == 0:
            print(1)
        else:
            print(0)
    elif c == 'top':
        print(-hq[0])
for _ in range(n):
    command = input().split()
    if len(command) > 1:
        operator(command[0], int(command[1]))
    else:
        operator(command[0])