#!/usr/bin/env python
import sys
from matplotlib import pyplot

if __name__ == '__main__':

    input_file = sys.argv[1]
    output_file = sys.argv[2]

    m = []
    accuracies = []

    values = open(input_file, "r")
    for line in values:
        elements = line.split()
        m.append(int(elements[0]))
        accuracies.append(float(elements[1]))

#    n_neg_y = []
#    n_pos_y = []
   
    
    pyplot.figure()
#    pyplot.errorbar(m_s, n_accuracies, yerr=[n_neg_y, n_pos_y], linestyle='-', marker='o', label="Naive")
#    pyplot.errorbar(m_s, t_accuracies, yerr=[t_neg_y, t_pos_y], linestyle='-', marker='o',  label="TAN")
    pyplot.plot(m, accuracies, linestyle='-', marker='o')
#    pyplot.plot(m_s, t_accuracies, linestyle='-', marker='o', label="TAN")
    
#    for i in range(len(m_s)):
#        pyplot.text(m_s[i] - 3, n_accuracies[i] + 2, n_accuracies[i])
#        pyplot.text(m_s[i] - 3, t_accuracies[i] + 2, t_accuracies[i])

    pyplot.legend(loc="upper left")

    pyplot.xlabel("Training Set Size")
    pyplot.xlim(0, 125)
    pyplot.ylim(50, 100)
    pyplot.ylabel("Accuracy (%)")
    pyplot.title("Naive Bayes Training Set Size vs Accuracy")
    pyplot.savefig("%s.png" % output_file, bbox_inches='tight')
    pyplot.show()
