#!/bin/bash

# Usage: <protein train> <protein test> 
#                 <window size> <number of hidden layers> <hidden layer size> <iterations> <output directory> <baseline>

outputDirectory="/mnt/ws/home/sdewet/760-project/ProteinSSClassification/results/deep"
trainFilename="/mnt/ws/home/sdewet/760-project/ProteinSSClassification/data/data.train"

numHiddenLayers=3
hiddenLayerSize=45 # default value provided by baseline tests
iterations=5 # increase the number of iterations as we go
windowSize=13
baseline="n"

## Test the number of hidden layers
for i in 0.33 0.25
do
    outputFile="${outputDirectory}/deep_${numHiddenLayers}nhl_${hiddenLayerSize}hls_${windowSize}ws_${i}decay"
    echo "Running Deep Network: hidden layer size 45, decay value $i" >> "${outputDirectory}/deep.log"
    java -jar deepNetwork.jar $trainFilename "none" $windowSize $numHiddenLayers $hiddenLayerSize $iterations $outputDirectory $baseline $i > $outputFile
    echo "Done" >> "${outputDirectory}/deep.log"
done

#iterations=10
## Test the number of hidden layers
#for i in 0.33 0.25
#do
#    outputFile="${outputDirectory}/deep_${numHiddenLayers}nhl_${hiddenLayerSize}hls_${windowSize}ws_${i}decay"
#    echo "Running Deep Network: hidden layer size 45, decay value $i, num iters $iterations" >> "${outputDirectory}/deep.log"
#    java -jar deepNetwork.jar $trainFilename "none" $windowSize $numHiddenLayers $hiddenLayerSize $iterations $outputDirectory $baseline $i > $outputFile
#    echo "Done" >> "${outputDirectory}/deep.log"
#done
