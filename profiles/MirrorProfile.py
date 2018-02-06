import sys
import fnmatch


print sys.argv

if fnmatch.filter(sys.argv,"file=*") != []:
    profile = fnmatch.filter(sys.argv,"file=*")
    profile = str(profile[0].split("=")[1])
else:
    print "Optional command line argument:  file=<PROFILE_NAME>"
    profile = raw_input("Please specify a profile: ")

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
    for x in profileArray:
        profileArray[profileArray.index(x)] = map(float,x.split(","))
        profileArray[profileArray.index(map(float,x.split(",")))][1] *= -1
    with open(str(profile1) + "-inverted.csv","w") as invertedProfileFile:
        profileCsv = []
        for x in profileArray:
            profileCsv.append(",".join(map(str,x)))
        invertedProfileFile.write("\n".join(profileCsv))

