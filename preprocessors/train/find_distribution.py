#!/usr/bin/env python

train_data = open("/home/vail/Documents/school/cs760/ProteinSSClassification/data/data.train", "r")

alpha = 0
beta = 0
loop = 0

for line in train_data:
    elements = line.split()
    if len(elements) == 3:
        classification = elements[2]
    elif len(elements) == 2:
        classification = elements[1]
    else:
        print "COULDN'T READ LINE ",
        print line

    for i in range(len(classification)):
        if classification[i] == 'h':
            alpha += 1
        elif classification[i] == 'e':
            beta += 1
        elif classification[i] == '-':
            loop += 1
        else:
            print "COULD'T RECOGNIZE CLASSIFICATION",
            print classification

print "Alpha: %d" % alpha
print "Beta: %d" % beta
print "Loop: %d" % loop
