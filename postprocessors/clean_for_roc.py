#!/usr/bin/env python
import sys
from matplotlib import *
from matplotlib.pyplot import *

output_folder="docs/results/"  # From base level of repo

result_file = open(sys.argv[1], "r")
plot_smoothed = True



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
   
all_curve_x = []
all_curve_y = []
for desired_class in ["ALPHA", "BETA", "LOOP"]:
	output_signs = [-1.0, -1.0, -1.0]
	if desired_class == "LOOP":
		output_signs[2] = 1.0
	elif desired_class == "BETA":
		output_signs[1] = 1.0
	else:
		output_signs[0] = 1.0
	# Create ROC for 1 vs all
	sorted_SET = []
	total_tp = 0
	total_fp = 0
	for item in raw:
		if item[3] == desired_class:  # If it's predicted to be a loop
			confidence = output_signs[0] * float(item[0]) + output_signs[1] * float(item[1]) + output_signs[2] * float(item[2])
			true_pos = False
			if item[4] == desired_class:  # If it's actually a loop
				true_pos = True
				total_tp = total_tp + 1
			else:
				total_fp = total_fp + 1
			new_vec = [confidence, true_pos]
			sorted_SET.append(new_vec)

	sorted_SET.sort(reverse=True, key=lambda tup: tup[0])
	count_tp = 0
	count_fp = 0
	prev_type = ""
	curve_x = []
	curve_y = []
	for item in sorted_SET:
		current_type = ""
		if item[1] == True:
			current_type = "TP"
		else:
			current_type = "FP"
		if prev_type != current_type and (plot_smoothed == False or prev_type == "TP"):
			curve_y.append(float(count_tp)/total_tp)
			curve_x.append(float(count_fp)/total_fp)
		prev_type = current_type
		if item[1] == True:
			count_tp = count_tp + 1
		else:
			count_fp = count_fp + 1
	all_curve_x.append(curve_x)
	all_curve_y.append(curve_y)

    
plot(all_curve_x[0], all_curve_y[0])
plot(all_curve_x[1], all_curve_y[1])
plot(all_curve_x[2], all_curve_y[2])
legend(("ALPHA", "BETA", "LOOP"), loc='lower right')
plot([0, .5, 1],[0, .5, 1], linestyle='--')
xlabel('FPR')
ylabel('TPR')
title('ROC Curve for ' + desired_class + ' Classification')
savefig(output_folder + 'ROC_all.png')
show()
