#!/usr/bin/env python
import sys

result_file = open(sys.argv[1], "r")
x_coordinate = sys.argv[2]
output_file = open(sys.argv[3], "w")

for line in result_file:
    if "Accuracy" in line: 
        elements = line.split()
        output_file.write("%s %s\n" % (x_coordinate, elements[1]))
    elif "Percentage" in line:
        elements = line.split()
        output_file.write("%s %s\n" % (x_coordinate, elements[2]))

