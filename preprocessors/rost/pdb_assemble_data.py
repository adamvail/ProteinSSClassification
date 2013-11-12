#!/usr/bin/env python

import sys
import os

class AssembleData(object):

    protein_filename = None
    pdb_dir = None

    def __init__(self, protein_file, pdbfinderdirectory):
        self.protein_filename = protein_file
        self.pdb_dir = pdbfinderdirectory

    def get_protein_list(self):
        protein_file = open(self.protein_filename, "r")
        proteins = {}
        for protein in protein_file:
            if '#' in protein:
                continue

            if protein not in proteins:
                proteins[protein.strip()] = []

        return proteins

    def get_data(self, proteins):
        for protein in proteins:
            chain = None
            protein_data = open(os.path.join(self.pdb_dir, "%s.dat" % protein.split('_')[0]), "r")

            if '_' in protein:
                chain = protein.split('_')[1]

            seen_chain = False

            for line in protein_data:
                if chain and "Chain" in line and chain in line:
                    seen_chain = True

                if (seen_chain or not chain) and "Sequence" in line:
                    colon_index = line.index(':')
                    sequence = line[colon_index + 1:].strip()
                    proteins[protein].append(sequence)

                if (seen_chain or not chain) and "DSSP" in line:
                    colon_index = line.index(':')
                    dssp = line[colon_index + 1:].strip()
                    proteins[protein].append(dssp)

    def output_proteins(object, proteins):
        output = open("protein_data", "w")
        for protein in proteins:
            output.write("%s %s %s\n" % (protein, proteins[protein][0], proteins[protein][1]))

    def print_proteins(self, proteins):
        for protein in proteins:
            print protein
            print "\t%s" % proteins[protein][0]
            print "\t%s" % proteins[protein][1]

if __name__ == '__main__':

    if len(sys.argv) != 3:
        print "Usage: <protein_filename> <pdb directory>"
        sys.exit(1)

    ad = AssembleData(sys.argv[1], sys.argv[2])
    proteins = ad.get_protein_list()
    ad.get_data(proteins)
    ad.print_proteins(proteins)
    ad.output_proteins(proteins)

