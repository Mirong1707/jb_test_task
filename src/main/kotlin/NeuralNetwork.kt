class NeuralNetwork(private val layers: Array<NetworkLayer>) {
    // Function count index of neuron from output layer, with the biggest value
    fun countResult(): Int {
        layers.forEachIndexed { index, _ ->
            if (index == layers.size - 1) {
                return layers[index].getIndexOfMax()
            }

            val width = layers[index].getNeuronsCount()
            val height = layers[index + 1].getNeuronsCount()
            val weightsMatrix = MatrixRND(width, height)
            val prevLayerNeuronsMatrix = layers[index].getMatrix()
            val bias = MatrixRND(prevLayerNeuronsMatrix.width(), height)

            // Here we multiply matrix of weights with matrix of neurons, add bias, and use  activation function, if needed
            layers[index + 1] = weightsMatrix.mul(prevLayerNeuronsMatrix)
                .add(bias).applyActivationFunction(layers[index + 1].funType)
                .getLayer(layers[index + 1].funType)
        }
        throw Exception("Should not be here")
    }
}