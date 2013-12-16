#!/bin/bash

# Usage: <protein train> <protein test> 
#                 <window size> <number of hidden layers> <hidden layer size> <iterations> <output directory> <baseline>

outputDirectory="/mnt/ws/home/sdewet/760-project/ProteinSSClassification/results/deep"
trainFilename="/mnt/ws/home/sdewet/760-project/ProteinSSClassification/data/data.train"

numHiddenLayers=2
hiddenLayerSize=15 # default value provided by baseline tests
iterations=5 # increase the number of iterations as we go
windowSize=13
baseline="n"

## Test the number of hidden layers
#for i in 1 2 3
#do
#    outputFile="${outputDirectory}/deep_${i}nhl_${hiddenLayerSize}hls_${windowSize}ws"
#    echo "Running Deep Network: hidden layer size 15, layer number -> $i" >> "${outputDirectory}/deep.log"
#    java -jar deepNetwork.jar $trainFilename "none" $windowSize $i $hiddenLayerSize $iterations $outputDirectory $baseline > $outputFile
#    echo "Done" >> "${outputDirectory}/deep.log"
#done
#
#hiddenLayerSize=26
#for i in 1 2 3
#do
#    outputFile="${outputDirectory}/deep_${i}nhl_${hiddenLayerSize}hls_${windowSize}ws"
#    echo "Running Deep Network: hidden layer size 26, layer number -> $i" >> "${outputDirectory}/deep.log"
#    java -jar deepNetwork.jar $trainFilename "none" $windowSize $i $hiddenLayerSize $iterations $outputDirectory $baseline > $outputFile
#    echo "Done" >> "${outputDirectory}/deep.log"
#done

iterations=10
# Test the number of hidden layers
for i in 1 2 3
do
    outputFile="${outputDirectory}/deep_${i}nhl_${hiddenLayerSize}hls_${windowSize}ws_${iterations}it"
    echo "Running Deep Network: hidden layer size 15, layer number -> $i, ${iterations} iter" >> "${outputDirectory}/deep.log"
    java -jar deepNetwork.jar $trainFilename "none" $windowSize $i $hiddenLayerSize $iterations $outputDirectory $baseline > $outputFile
    echo "Done" >> "${outputDirectory}/deep.log"
done

hiddenLayerSize=26
for i in 1 2 3
do
    outputFile="${outputDirectory}/deep_${i}nhl_${hiddenLayerSize}hls_${windowSize}ws_${iterations}it"
    echo "Running Deep Network: hidden layer size 26, layer number -> $i, ${iterations} iter" >> "${outputDirectory}/deep.log"
    java -jar deepNetwork.jar $trainFilename "none" $windowSize $i $hiddenLayerSize $iterations $outputDirectory $baseline > $outputFile
    echo "Done" >> "${outputDirectory}/deep.log"
done
