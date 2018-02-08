import sys
import fnmatch
import csv

print sys.argv

TopToBottom = 301.72 + 50.38 - 32.5

if fnmatch.filter(sys.argv,"file=*") != []:
    profile = fnmatch.filter(sys.argv,"file=*")
    profile = str(profile[0].split("=")[1])
else:
    print "Optional command line argument:  file=<PROFILE_NAME>"
    profile = raw_input("Please specify a profile: ")

side = raw_input("0: top\n1: middle\n2: bottom\n")

profile1 = str(profile.rsplit(".")[0])
profile = str(profile + ".csv")

if ".csv" not in profile:
    profile = profile + ".csv"
elif "\\" in profile:
    raise ValueError("Requires a local profile.")

with open(profile,"r") as profileFile:
    profileFileData = profileFile.read()
    profileArray = []
    profileArray = profileFileData.splitlines()
    index = []
    for x in profileArray:
        index = profileArray.index(x)
        profileArray[index] = map(float,x.split(","))
        profileArray[index][1] *= -1
        print (int(side)==0)
        if int(side) == 0:
            profileArray[index][1] -= TopToBottom
        elif int(side) == 2:
            profileArray[index][1] += TopToBottom
    with open(str(profile1) + "-inverted.csv","w") as invertedProfileFile:
        profileCsv = []
        for x in profileArray:
            profileCsv.append(",".join(map(str,x)))
        invertedProfileFile.write("\n".join(profileCsv))

