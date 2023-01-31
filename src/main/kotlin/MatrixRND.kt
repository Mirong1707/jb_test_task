class MatrixRND(width: Int, height: Int) {
    // В данной задаче нам понадобится только рандомно сгенерированные матрицы определённого размера
    private var matrix = Array(height) { Array(width) { rand() } }

    //constructor from NetworkLayer
    constructor(layer: Array<Float>) : this(1, layer.size) {
        matrix.forEachIndexed { index, _ -> matrix[index][0] = layer[index] }
    }
    //constructor from Matrix
    constructor(matrix: Array<Array<Float>>) : this(matrix.size, if (matrix.isEmpty()) 0 else matrix[0].size) {
        this.matrix = matrix
    }

    fun width() = if (matrix.isEmpty()) 0 else matrix[0].size
    fun height() = matrix.size

    //Cast vector matrix to NetworkLayer
    fun getLayer(funType: ActivationFunctionType) =
        if (matrix.isEmpty() || matrix[0].size != 1) throw Exception("Only vectors") else NetworkLayer(matrix, funType)

    private fun getByInd(index1: Int, index2: Int): Float{
        return matrix[index1][index2]
    }

    //Matrix multiplication
    fun mul(m: MatrixRND): MatrixRND {
        if (width() != m.height()) throw Exception("wrong size of mul matrix")
        val n = width()
        val mulMatrix = Array(this.height()) { Array(m.width()) { 0.0f } }
        mulMatrix.forEachIndexed { index1, it1 ->
            it1.forEachIndexed { index2, _ ->
                for (midIndex in 0 until n) {
                    mulMatrix[index1][index2] += this.matrix[index1][midIndex] * m.matrix[midIndex][index2]
                }
            }
        }
        return MatrixRND(mulMatrix)
    }

    fun add(m: MatrixRND): MatrixRND {
        if (width() != m.width() || height() != m.height()) throw Exception("wrong size of mul matrix")
        matrix.forEachIndexed { index1, it1 ->
            it1.forEachIndexed { index2, _ ->
                matrix[index1][index2] += m.getByInd(index1, index2)
            }
        }
        return this
    }

    fun applyActivationFunction(funType: ActivationFunctionType): MatrixRND {
        matrix.forEachIndexed { index1, it1 ->
            it1.forEachIndexed { index2, _ ->
                matrix[index1][index2] = activationFunction(matrix[index1][index2], funType)
            }
        }
        return this
    }

}