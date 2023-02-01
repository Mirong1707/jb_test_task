class MatrixRND(width: Int, height: Int) {
    // In this task we only need randomly generated matrix
    private var matrix = Array(height) { Array(width) { rand() } }

    //constructor from NetworkLayer
    constructor(layer: Array<Float>) : this(1, layer.size) {
        matrix.forEachIndexed { index, _ -> matrix[index][0] = layer[index] }
    }
    //constructor from Matrix
    constructor(matrix: Array<Array<Float>>) : this(matrix.size, if (matrix.isEmpty()) 0 else matrix[0].size) {
        this.matrix = matrix
    }

    constructor(matrix: Array<Array<Float>>, beg1: Int, beg2: Int, sz: Int) : this(sz, sz) {
        this.matrix.forEachIndexed { index1, it1 ->
            it1.forEachIndexed { index2, _ ->
                this.matrix[index1][index2] = matrix[beg1 + index1][beg2 + index2]
            }
        }
    }

    private constructor(l: Array<Array<MatrixRND>>) : this(l[0][0].matrix.size * 2, l[0][0].matrix.size * 2) {
        val n = l[0][0].matrix.size * 2
        for(i in 0..1){
            for(j in 0..1){
                l[i][j].matrix.forEachIndexed{ index1, it1 ->
                    it1.forEachIndexed { index2, _ ->
                        this.matrix[index1 + i * n][index2 + j * n] = l[i][j].matrix[index1][index2]
                    }
                }
            }
        }
    }

    private constructor(matrix: MatrixRND, height:Int, width:Int) : this(height,width) {
        this.matrix.forEachIndexed { index1, it1 ->
            it1.forEachIndexed { index2, _ ->
                this.matrix[index1][index2] = 0.0f
            }
        }
        matrix.matrix.forEachIndexed { index1, it1 ->
            it1.forEachIndexed { index2, _ ->
                if(this.matrix.size > index1 && this.matrix[0].size > index2)
                    this.matrix[index1][index2] = matrix.matrix[index1][index2]
            }
        }
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

    fun split2x2blocks(m: MatrixRND): Array<Array<MatrixRND>> {
        val n = m.matrix.size / 2
        return arrayOf(arrayOf(MatrixRND(m.matrix, 0, 0, n), MatrixRND(m.matrix, 0, n, n)), arrayOf(MatrixRND(m.matrix, n, 0, n), MatrixRND(m.matrix, n, n, n)))
    }


    fun strassenMul2x2(lb: Array<Array<MatrixRND>>, rb : Array<Array<MatrixRND>>): Array<Array<MatrixRND>> {
        val d = strassenMul(lb[0][0] + lb[1][1], rb[0][0] + rb[1][1])
        val d_1 = strassenMul(lb[0][1] - lb[1][1], rb[1][0] + rb[1][1])
        val d_2 = strassenMul(lb[1][0] - lb[0][0], rb[0][0] + rb[0][1])

        val left = strassenMul(lb[1][1], rb[1][0] - rb[0][0])
        val right = strassenMul(lb[0][0], rb[0][1] - rb[1][1])
        val top = strassenMul(lb[0][0] + lb[0][1], rb[1][1])
        val bottom = strassenMul(lb[1][0] + lb[1][1], rb[0][0])

        return arrayOf(arrayOf(d + d_1 + left - top, right + top), arrayOf(left + bottom, d + d_2 + right - bottom))
    }
    fun strassenMul(m1: MatrixRND, m2: MatrixRND): MatrixRND {
        return MatrixRND(strassenMul2x2(split2x2blocks(m1),split2x2blocks(m2)))
    }

    fun getPowOf2BiggerMatrixSZ(m1: MatrixRND, m2: MatrixRND) : Int{
        val mx = arrayOf(m1.matrix.size, m2.matrix.size, m1.matrix[0].size, m2.matrix[0].size).max()
        var k = 1
        while(k < mx) k *= 2
        return k
    }

    fun fastMul(m: MatrixRND): MatrixRND {
        if (width() != m.height()) throw Exception("wrong size of mul matrix")
        if (this.matrix.isEmpty() || m.matrix.isEmpty()) throw Exception("wrong size of mul matrix")
        val k = getPowOf2BiggerMatrixSZ(this, m)
        val m1 = MatrixRND(this, k, k);
        val m2 = MatrixRND(m, k, k);
        return MatrixRND(strassenMul(m1, m2), height(), m.width())
    }

    operator fun plus(m: MatrixRND): MatrixRND {
        if (width() != m.width() || height() != m.height()) throw Exception("wrong size of mul matrix")
        matrix.forEachIndexed { index1, it1 ->
            it1.forEachIndexed { index2, _ ->
                matrix[index1][index2] += m.getByInd(index1, index2)
            }
        }
        return this
    }

    operator fun minus(m: MatrixRND): MatrixRND {
        if (width() != m.width() || height() != m.height()) throw Exception("wrong size of mul matrix")
        matrix.forEachIndexed { index1, it1 ->
            it1.forEachIndexed { index2, _ ->
                matrix[index1][index2] -= m.getByInd(index1, index2)
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