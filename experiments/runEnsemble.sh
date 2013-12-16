#!/bin/bash

# Usage: <protein train> <protein test> 
#                 <window size> <number of hidden layers> <hidden layer size> <iterations> <output directory> <baseline>

trainFilename="/home/vail/Documents/school/cs760/ProteinSSClassification/data/data.train"
testFilename="/home/vail/Documents/school/cs760/ProteinSSClassification/data/uci/uci_protein_data.test"
outputDirectory="/home/vail/Documents/school/cs760/ProteinSSClassification/results/ensemble/5"

numHiddenLayers=2
hiddenLayerSize=30 # default value provided by baseline tests
iterations=1 # increase the number of iterations as we go
windowSize=13
baseline="n"
connectedOutput="n"
decay=1
ensemble="y"


outputFile="${outputDirectory}/ensemble_5nns"
echo "Ensemble size of 5" >> "${outputDirectory}/ensemble.log"
java -jar deepNetwork.jar $trainFilename $testFilename $windowSize $numHiddenLayers $hiddenLayerSize $iterations $outputDirectory $baseline $connectedOutput $decay $ensemble > $outputFile
echo "Done" >> "${outputDirectory}/ensemble.log"
