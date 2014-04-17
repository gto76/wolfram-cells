import si.gto76.funphototime.Utility

object WcUtil {
    
    def getImage(matrix: Array[Array[Boolean]]) = {
        var img = Utility.declareNewBufferedImage(matrix(0).size, matrix.size)
        var x, y = 0;
        for (row <- matrix) {
	        for (element <- row) {
	        	if (element == true)
	        		img.setRGB(x, y, 255)
	        	else
	        		img.setRGB(x, y, 0)
	        	x = x + 1
	        }
	        x = 0
	        y = y + 1
	    }
		img
    }
    
	def printMatrix(matrix: Array[Array[Boolean]]) {
	    print(matrixToString(matrix, "#", " "))
	}
	
	def matrixToString(matrix: Array[Array[Boolean]], t: String, f: String) {
		val sb = new StringBuilder
		for (row <- matrix) {
	        for (element <- row) {
	        	if (element == true)
	        		sb.append(t)
	        	else
	        	    sb.append(f)
	        }
	        sb.append('\n')
	    }
		sb.toString
	}
	
	def getAllCombos(len: Int): Array[String] = {
        val noOfCombos = Math.pow(2, len).asInstanceOf[Int]
        var out = Array.ofDim[String](noOfCombos)
		for (i <- 0 to noOfCombos-1 ) {
			var bin = Integer.toBinaryString(i);
		    while (bin.length() < len)
		        bin = "0" + bin;
		    out(i) = bin
		}
        out
    }
	
	def stringToBoolArray(combo: String): Array[Boolean] = {
	    var cArr = Array.ofDim[Boolean](combo.size)
	    var i = 0;
	    for (letter <- combo.toCharArray) {
            cArr(i) = WcUtil.letterToBool(letter)
            i = i + 1
	    }
	    cArr
	}
	
	def letterToBool(letter: Char): Boolean = {
        if (letter == '1')
            true
        else
            false
    }
	
	def getNewMatrix(matrixHeight: Int, matrixWidth: Int): Array[Array[Boolean]] = {
        var matrix = Array.ofDim[Boolean](matrixHeight, matrixWidth) 
	    matrix(0)(matrixWidth/2) = true
	    matrix
    }
	
}