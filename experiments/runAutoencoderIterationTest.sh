#!/bin/bash

# Usage: <protein train> <protein test> 
#                 <window size> <number of hidden layers> <hidden layer size> <iterations> <output directory> <baseline>

trainFilename="/home/vail/Documents/school/cs760/ProteinSSClassification/data/data.train"
testFilename="/home/vail/Documents/school/cs760/ProteinSSClassification/data/uci/uci_protein_data.test"

outputDirectory="/home/vail/Documents/school/cs760/ProteinSSClassification/results/deep/multi_iteration"

numHiddenLayers=3
hiddenLayerSize=15 # default value provided by baseline tests
iterations=5 # increase the number of iterations as we go
windowSize=13
baseline="n"
connectedOutput="n"
decay=0
ensemble="n"
numEnsemble=0

# Test the number of hidden layers
for i in 1 2 3
do
    outputFile="${outputDirectory}/deep_${numHiddenLayers}nhl_${hiddenLayerSize}hls_${windowSize}ws_multi_iters_${i}"
    echo "Number of iterations -> $i" >> "${outputDirectory}/deep.log"
    java -jar deepNetwork.jar $trainFilename $testFilename $windowSize $numHiddenLayers $hiddenLayerSize $i $outputDirectory $baseline $connectedOutput $decay $ensemble $numEnsemble > $outputFile
    echo "Done" >> "${outputDirectory}/deep.log"
done
