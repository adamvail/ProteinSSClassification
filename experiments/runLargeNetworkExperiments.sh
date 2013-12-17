#!/bin/bash

# Usage: <protein train> <protein test> 
#                 <window size> <number of hidden layers> <hidden layer size> <iterations> <output directory> <baseline>

outputDirectory="/mnt/ws/home/sdewet/760-project/ProteinSSClassification/results/deep"
trainFilename="/mnt/ws/home/sdewet/760-project/ProteinSSClassification/data/data.train"
testFilename="/mnt/ws/home/sdewet/760-project/ProteinSSClassification/data/uci/uci_protein_data.train"

numHiddenLayers=2
hiddenLayerSize=26 # default value provided by baseline tests
iterations=5 # increase the number of iterations as we go
windowSize=13
baseline="n"

# Test the number of hidden layers
for i in 3 4
do
    actualNetSize=$i+1
    outputFile="${outputDirectory}/deep_${actualNetSize}nhl_${hiddenLayerSize}hls_${windowSize}ws"
    echo "Running Deep Network: hidden layer size 30, layer number -> $actualNetSize" >> "${outputDirectory}/deep.log"
    java -jar deepNetwork.jar $trainFilename $testFilename $windowSize $i $hiddenLayerSize $iterations $outputDirectory $baseline > $outputFile
    echo "Done with large network run" >> "${outputDirectory}/deep.log"
done
