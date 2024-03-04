n = int(input())
d = dict()

def operator(command, k, v = None):
    if command == 'add':
        d[k] = v
    elif command == 'remove':
        d.pop(k)
    elif command == 'find':
        if k in d:
            print(d[k])
        else: 
            print(None)

for _ in range(n):
    command = input().split()
    # print(command)
    if len(command) > 2:
        operator(command[0], command[1], int(command[2]))
    else: 
        operator(command[0], command[1])


    # print(command, key,value)