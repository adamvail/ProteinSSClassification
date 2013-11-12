#!/usr/bin/env python

import sys
import os

class AssembleData(object):

    protein_filename = None

    def __init__(self, protein_file):
        self.protein_filename = protein_file

    def get_data(self):
        proteins = {}

        in_protein = False
        protein_fragment = ""
        ss_fragment = ""

        raw_data = open(self.protein_filename, "r")
        for line in raw_data:
            if line.startswith('#'):
                continue

            elif not in_protein and '<>' in line:
                in_protein = True
            elif in_protein and 'end' in line:
                in_protein = False
                proteins[protein_fragment] = ss_fragment
                protein_fragment = ""
                ss_fragment = ""
            elif in_protein and '<>' in line:
                # not sure what this means but it shows up
                # maybe it designates different chains?
                continue
            elif in_protein:
                protein_fragment += line.split()[0]
                ss_fragment += line.split()[1]

        return proteins

    def output_proteins(self, proteins):
        output = open("uci_protein_data.%s" % self.protein_filename.split('.')[1], "w")
        for protein in proteins:
            output.write("%s %s\n" % (protein, proteins[protein]))

    def print_proteins(self, proteins):
        for protein in proteins:
            print protein
            print "\t%s" % proteins[protein][0]
            print "\t%s" % proteins[protein][1]

if __name__ == '__main__':

    if len(sys.argv) != 2:
        print "Usage: <UCI test file>"
        sys.exit(1)

    ad = AssembleData(sys.argv[1])
    proteins = ad.get_data()
    ad.output_proteins(proteins)
