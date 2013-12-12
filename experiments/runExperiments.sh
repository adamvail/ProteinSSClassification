#!/bin/bash

# Usage: <protein train> <protein test> 
#                 <window size> <number of hidden layers> <hidden layer size> <iterations> <output directory> <baseline>

rostTrainFilename="/Users/vail/Documents/school/cs760/ProteinSSClassification/data/rost/protein_data"
uciTrainFilename="/Users/vail/Documents/school/cs760/ProteinSSClassification/data/uci/uci_protein_data.train"
uciTestFilename="/Users/vail/Documents/school/cs760/ProteinSSClassification/data/uci/uci_protein_data.test"
outputDirectory="/Users/vail/Desktop/DeepNetworkData"

numHiddenLayers=2
hiddenLayerSize=26 # default value provided by baseline tests
iterations=5 # increase the number of iterations as we go
windowSize=13
baseline="n"

# Test the number of hidden layers
for i in 1 2 3 4 5 
do
    echo "Running Deep Network: hidden layer number -> $i" >> "${outputDirectory}/log"
    java -jar deepnetwork.jar $rostTrainFilename "none" $windowSize $i $hiddenLayerSize $iterations $outputDirectory $baseline
done

# Test the number of iterations
#for i in 1 2 5 10 15
#do
#    echo "Running Deep Network: iteration -> $i" >> "${outputDirectory}/log"
#    java -jar deepnetwork.jar $rostTrainFilename "none" $windowSize $numHiddenLayers $i $iterations $outputDirectory $baseline
#done
#
## Test the size of the hidden layer
#for i in {5..50..5}
#do
#    echo "Running Deep Network: hidden layer size -> $i" >> "${outputDirectory}/log"
#    java -jar deepnetwork.jar $rostTrainFilename "none" $windowSize $numHiddenLayers $i $iterations $outputDirectory $baseline
#done
#
#for i in 7 11 13 17
#do
#    echo "Running Deep Network: window size -> $i" >> "${outputDirectory}/log"
#    java -jar deepnetwork.jar $rostTrainFilename "none" $i $numHiddenLayers $hiddenLayerSize $iterations $outputDirectory $baseline
#done
