#!/bin/bash

# Usage: <protein train> <protein test> 
#                 <window size> <number of hidden layers> <hidden layer size> <iterations> <output directory> <baseline>

outputDirectory="/mnt/ws/home/sdewet/760-project/ProteinSSClassification/results/deep"
trainFilename="/mnt/ws/home/sdewet/760-project/ProteinSSClassification/data/data.train"
testFilename="/mnt/ws/home/sdewet/760-project/ProteinSSClassification/data/uci/uci_protein_data.train"

numHiddenLayers=6
hiddenLayerSize=26 # default value provided by baseline tests
iterations=$1 # increase the number of iterations as we go
windowSize=13
baseline="n"
decay=0
traditionalOutput="y"
ensemble="n"
numEnsemble=0

## Test the number of hidden layers
outputFile="${outputDirectory}/deep_${numHiddenLayers}nhl_${hiddenLayerSize}hls_${windowSize}ws_${iterations}it"
echo "Running Deep Network: hidden layer size $numHiddenLayers $hiddenLayerSize" >> "${outputDirectory}/deep.log"
java -jar deepNetwork_decayFix.jar $trainFilename $testFilename $windowSize $numHiddenLayers $hiddenLayerSize $iterations $outputDirectory $baseline $traditionalOutput $decay $ensemble $numEnsemble > $outputFile
echo "Done" >> "${outputDirectory}/deep.log"
