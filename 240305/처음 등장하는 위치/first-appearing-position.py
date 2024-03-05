from sortedcontainers import SortedDict

n = int(input())
sd = SortedDict()
num = map(int, input().split())

for i, n in enumerate(num):
    if n not in sd:
        sd[n] = i + 1

for k, v in sd.items():
    print(k, v)