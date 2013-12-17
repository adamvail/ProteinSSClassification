#!/bin/bash

# Usage: <protein train> <protein test> 
#                 <window size> <number of hidden layers> <hidden layer size> <iterations> <output directory> <baseline>

#rostTrainFilename="/Users/vail/Documents/school/cs760/ProteinSSClassification/data/rost/protein_data"
#uciTrainFilename="/Users/vail/Documents/school/cs760/ProteinSSClassification/data/uci/uci_protein_data.train"
uciTestFilename="/Users/vail/Documents/school/cs760/ProteinSSClassification/data/uci/uci_protein_data.test"

trainFilename="/home/vail/Documents/school/cs760/ProteinSSClassification/data/data.train"
outputDirectory="/home/vail/Documents/school/cs760/ProteinSSClassification/results/baseline/windowSize/30HiddenUnits"

numHiddenLayers=1
hiddenLayerSize=30
iterations=5 
windowSize=13
baseline="y"
connectedOutput="n"
decay=1
ensemble="n"
numEnsemble=5

# Baseline experiments
count=1
for i in 21 25 31
do
    echo "Running Baseline: window size -> $i" >> "${outputDirectory}/baseline.log"
    outputFile="${outputDirectory}/baseline_${numHiddenLayers}nhl_${hiddenLayerSize}hls_${i}ws"
    count=$(($count + 1))
    java -jar deepNetwork.jar $trainFilename "none" $i $numHiddenLayers $hiddenLayerSize $iterations $outputDirectory $baseline $connectedOutput $decay $ensemble $numEnsemble > $outputFile
done
