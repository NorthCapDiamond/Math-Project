import os
import time
import matplotlib.pyplot as plot
stats = os.stat('../MathProj/dots.txt')
while (stats.st_size==0):
	time.sleep(5)
	stats = os.stat('../MathProj/dots.txt')
time.sleep(5)
f = open("../MathProj/dots.txt", "r")
file = f.read()
f.close()
try:
    with open("../MathProj/dots.txt", 'r+') as f:
        f.truncate()
except IOError:
    print('Failure')





splited = file.split("EOI")

allN = [int(n) for n in splited[0].split()]
allValues = [float(error) for error in splited[1].split()]
print(*allValues)
print(*allN)
plot.plot(allN, allValues)
plot.show()



