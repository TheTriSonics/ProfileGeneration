# ProfileGeneration

Tools for generating profiles for the 2018 FRC game Power Up.

There are a couple of steps in the workflow:

## Step 1:  Create the trajectory

Begin with the Java application PlanTrajectory.  From the command
line, standard usage is
```
java PlanTrajectory
java PlanTrajectory -r
java PlanTrajectory filename.csv
java PlanTrajectory -r filename.csv
```

The `-r` switch adds the robot in its initial configuration along the
alliance wall.  You may edit an existing trajectory that was
previously saved in `filename.csv`.

Trajectories will be saved as a `.csv` file in the
directory `profiles`.

## Step 2:  Edit the output trajectory to include command tags.

A trajectory file simply consists of waypoints, such as
```
16.25, 70.0
40.0, 30.0
100.0, 10.0
200.0, 30.0
```

To trigger the start of a command at one of the waypoints, simply add
a tag for that command behind the waypoint:
```
16.25, 70.0
40.0, 30.0,raiselift
100.0, 10.0
200.0, 30.0
```

## Step 3:  Create a file to specify the velocity profiles

If the desired trajectory is stored in a file `trajectory.csv`, create
a file named `trajectory.velocities.csv`.  The format for this file
consists of one line for each leg of the trajectory.  Each line
specifies the initial speed, the cruising speed, and the final speed
for that leg expressed as a fraction of `vmax`.
```
0, 0.8, 0.8
0.8, 0.8, 0.8
0.8, 0.8, 0.8
0.8, 0.8, 0
```

## Step 4:  Create the profile

Use the python script
`
python profile.py filename.csv
`


