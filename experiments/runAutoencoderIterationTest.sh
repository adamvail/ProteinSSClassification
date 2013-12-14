#!/bin/bash

# Usage: <protein train> <protein test> 
#                 <window size> <number of hidden layers> <hidden layer size> <iterations> <output directory> <baseline>

trainFilename="/home/vail/Documents/school/cs760/ProteinSSClassification/data/data.train"
outputDirectory="/home/vail/Documents/school/cs760/ProteinSSClassification/results/deep/iterations"

numHiddenLayers=2
hiddenLayerSize=15 # default value provided by baseline tests
iterations=5 # increase the number of iterations as we go
windowSize=13
baseline="n"

# Test the number of hidden layers
for i in 1 2 5 10
do
    outputFile="${outputDirectory}/deep_${numHiddenLayers}nhl_${hiddenLayerSize}hls_${windowSize}ws_${i}iters"
    echo "Number of iterations -> $i" >> "${outputDirectory}/deep.log"
    java -jar deepNetwork.jar $trainFilename "none" $windowSize $numHiddenLayers $hiddenLayerSize $i $outputDirectory $baseline > $outputFile
    echo "Done" >> "${outputDirectory}/deep.log"
done
