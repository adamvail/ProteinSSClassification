#!/bin/bash

# Usage: <protein train> <protein test> 
#                 <window size> <number of hidden layers> <hidden layer size> <iterations> <output directory> <baseline>

trainFilename="/home/vail/Documents/school/cs760/ProteinSSClassification/data/data.train"
testFilename="/home/vail/Documents/school/cs760/ProteinSSClassification/data/uci/uci_protein_data.test"
outputDirectory="/home/vail/Documents/school/cs760/ProteinSSClassification/results/ensemble"

numHiddenLayers=2
hiddenLayerSize=30 # default value provided by baseline tests
iterations=1 # increase the number of iterations as we go
windowSize=13
baseline="n"
connectedOutput="n"
decay=1
ensemble="y"
numEnsemble=5

for i in 7 8
do
    outputFile="${outputDirectory}/ensemble_${numEnsemble}nns_${i}"
    echo "Ensemble size of 5 run ${i}" >> "${outputDirectory}/ensemble.log"
    java -jar deepNetwork.jar $trainFilename $testFilename $windowSize $numHiddenLayers $hiddenLayerSize $iterations $outputDirectory $baseline $connectedOutput $decay $ensemble $numEnsemble > $outputFile
    echo "Done" >> "${outputDirectory}/ensemble.log"
done

#numEnsemble=5
#for i in 5
#do
#    outputFile="${outputDirectory}/ensemble_${numEnsemble}nns_${i}"
#    echo "Ensemble size of 11 run ${i}" >> "${outputDirectory}/ensemble.log"
#    java -jar deepNetwork.jar $trainFilename $testFilename $windowSize $numHiddenLayers $hiddenLayerSize $iterations $outputDirectory $baseline $connectedOutput $decay $ensemble $numEnsemble > $outputFile
#    echo "Done" >> "${outputDirectory}/ensemble.log"
#done
#
#numEnsemble=15
#for i in 1 2 3
#do
#    outputFile="${outputDirectory}/ensemble_${numEnsemble}nns_${i}"
#    echo "Ensemble size of 15 run ${i}" >> "${outputDirectory}/ensemble.log"
#    java -jar deepNetwork.jar $trainFilename $testFilename $windowSize $numHiddenLayers $hiddenLayerSize $iterations $outputDirectory $baseline $connectedOutput $decay $ensemble $numEnsemble > $outputFile
#    echo "Done" >> "${outputDirectory}/ensemble.log"
#done
