#!/bin/bash

# Usage: <protein train> <protein test> 
#                 <window size> <number of hidden layers> <hidden layer size> <iterations> <output directory> <baseline>

#rostTrainFilename="/Users/vail/Documents/school/cs760/ProteinSSClassification/data/rost/protein_data"
#uciTrainFilename="/Users/vail/Documents/school/cs760/ProteinSSClassification/data/uci/uci_protein_data.train"
uciTestFilename="/Users/vail/Documents/school/cs760/ProteinSSClassification/data/uci/uci_protein_data.test"

trainFilename="/home/vail/Documents/school/cs760/ProteinSSClassification/data/data.train"
outputDirectory="/home/vail/Documents/school/cs760/ProteinSSClassification/results/baseline/hiddenLayerSize"

numHiddenLayers=1
hiddenLayerSize=50 
iterations=5 
windowSize=13
baseline="y"
connectedOutput="n"

# Baseline experiments
count=1
for i in {15..300..15}
do
    echo "Running Baseline: hidden layer size -> $i" >> "${outputDirectory}/baseline.log"
    outputFile="${outputDirectory}/baseline_${numHiddenLayers}nhl_${i}hls_${windowSize}ws"
    count=$(($count + 1))
    java -jar deepNetwork.jar $trainFilename "none" $windowSize $numHiddenLayers $i $iterations $outputDirectory $baseline $connectedOutput > $outputFile
done
