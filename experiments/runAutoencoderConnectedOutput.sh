#!/bin/bash

# Usage: <protein train> <protein test> 
#                 <window size> <number of hidden layers> <hidden layer size> <iterations> <output directory> <baseline>

trainFilename="/home/vail/Documents/school/cs760/ProteinSSClassification/data/data.train"
outputDirectory="/home/vail/Documents/school/cs760/ProteinSSClassification/results/deep/connectedOutput"

numHiddenLayers=2
hiddenLayerSize=26 # default value provided by baseline tests
iterations=5 # increase the number of iterations as we go
windowSize=13
baseline="n"
connectedOutput="y"


for i in 1 2 3 4 5
do
    outputFile="${outputDirectory}/deep_${i}nhl_${hiddenLayerSize}hls_${windowSize}ws"
    echo "Running Deep Network: hidden layer size 26, layer number -> $i" >> "${outputDirectory}/deep.log"
    java -jar deepNetwork.jar $trainFilename "none" $windowSize $i $hiddenLayerSize $iterations $outputDirectory $baseline $connectedOutput > $outputFile
    echo "Done" >> "${outputDirectory}/deep.log"
done
