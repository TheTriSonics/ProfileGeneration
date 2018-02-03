import os
import time
dir_path = os.path.dirname(os.path.realpath(__file__))

speed = input("Please specify a base speed: ")
csvPath = raw_input("Please specify the waypoint file: ")
if "\\" in csvPath:
    print("Waypoint file needs to be in loal folder")
    time.sleep(2)
    raise ValueError("Waypoint file needs to be in loal folder")
csvPath = "\\".join([dir_path,csvPath])
csvFolder = csvPath.rsplit("\\",1)[0]
csvFile = csvPath.rsplit("\\",1)[1]
csvFile = csvFile.rsplit(".",1)[0]
print(csvFolder)
with open(csvFolder+"\\%s.velocities.csv"%csvFile,"w") as file:
    with open(csvPath,"r") as Originalfile:
        fileread = Originalfile.read()
        linesplit = str(fileread).splitlines()
        coords = []
        for x in linesplit:
            coords.append(x.split(", "))
        print(len(coords))
    velocities = []
    velocities.append([0,float(speed),float(speed)])
    for x in range(1,len(coords)-2):
        velocities.append([float(speed),float(speed),float(speed)])
    velocities.append([float(speed),float(speed),0])
    velocities = ', '.join(str(x) for x in velocities)
    velocities = (velocities[1:-1]).split("], [")
    file.write("\n".join(velocities))
