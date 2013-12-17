#!/usr/bin/env python
import sys
from matplotlib import *

result_file = open(sys.argv[1], "r")
x_coordinate = sys.argv[2]
output_file = open(sys.argv[3], "w")
# Raw contains: [alpha_val beta_val loop_val predicted_class actual_class] 
raw = []

# Parse raw input into raw
for line in result_file:
    if "iters" in line:
        elements = line.split()
    elif "Too" in line:
        elements = line.split()
    elif "Accuracy" in line: 
        elements = line.split()
    elif "Percentage" in line:
        elements = line.split()
    elif "Prediction" in line:
        elements = line.split()
        item.append(elements[1])
        item.append(elements[3])
        raw.append(item)
    else:
        elements = line.split()
        item = elements
   
#for item in raw:
#    output_file.write("%s %s %s %s %s\n" % (item[0], item[1], item[2], item[3], item[4]))

# Create ROC for LOOP vs all
sorted_LOOP = []
total_tp = 0
total_fp = 0
for item in raw:
    if item[3] == "LOOP":  # If it's predicted to be a loop
        confidence = float(item[2]) - float(item[1]) - float(item[0])
        true_pos = False
        if item[4] == "LOOP":  # If it's actually a loop
            true_pos = True
            total_tp = total_tp + 1
        else:
            total_fp = total_fp + 1
        new_vec = [confidence, true_pos]
        sorted_LOOP.append(new_vec)

sorted_LOOP.sort(reverse=True, key=lambda tup: tup[0])
count_tp = 0
count_fp = 0
prev_type = ""
curve_x = []
curve_y = []
for item in sorted_LOOP:
    current_type = ""
    if item[1] == True:
        current_type = "TP"
    else:
        current_type = "FP"
    if prev_type != current_type:
        curve_y.append(float(count_tp)/total_tp)
        curve_x.append(float(count_fp)/total_fp)
    prev_type = current_type
    if item[1] == True:
        count_tp = count_tp + 1
    else:
        count_fp = count_fp + 1

    
#for item in curve:
#    output_file.write("%f %s\n" % (item[0], item[1]))

plot(curve_x, curve_y)
xlabel('FPR')
ylabel('TPR')
title('ROC curve for LOOP')
savefig('ROC_LOOP.png')
show()
