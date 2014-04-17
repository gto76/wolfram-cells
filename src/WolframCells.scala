object WolframCells extends App {

    override def main(args: Array[String]) {
        val HEIGHT: Int = 20
	    val WIDTH: Int = 80
	    val TABLE_HEIGHT: Int = 2
		val TABLE_WIDTH: Int = 3
	    val PROCESS_SINGLE = true
    
	    if (PROCESS_SINGLE)
	        processSingle(TABLE_HEIGHT, TABLE_WIDTH, HEIGHT, WIDTH)
	    else
	    	processAll(TABLE_HEIGHT, TABLE_WIDTH, HEIGHT, WIDTH)
    }
    
    def processSingle(height: Int , width: Int, 
            matrixHeight: Int, matrixWidth: Int ) {
    	var matrix = WcUtil.getNewMatrix(matrixHeight, matrixWidth)
        //var table = Array.ofDim[Boolean](height, width)
        val table = createTable("101111", height, width)
	    /*table(0)(0) = true
	    table(0)(1) = true
	    table(0)(2) = true
	    table(1)(0) = false
	    table(1)(1) = false
	    table(1)(2) = false*/
	    process(table, matrix)
    }
    
    def processAll(height: Int , width: Int, 
            matrixHeight: Int, matrixWidth: Int ) {
        for (combo <- WcUtil.getAllCombos(height * width)) {
        	var matrix = WcUtil.getNewMatrix(matrixHeight, matrixWidth)
            println(combo)
	        val table = createTable(combo, height, width)
	        process(table, matrix)
	    }
    }
    

    
    def process(table: Array[Array[Boolean]], matrix: Array[Array[Boolean]]) {
        val matrixOut = runAlgorithm(table, matrix)
	    println("+++"+table.size+"/"+table(0).size+"/"+table(1).size)
        WcUtil.printMatrix(table)
        println("+++")
	    WcUtil.printMatrix(matrixOut) 
    }
    
    def createTable(combo: String, y: Int, x: Int) = {
        var table = Array.ofDim[Boolean](y, x)
        var i = 0
        var j = 0
        for (letter <- combo.toCharArray) {
            table(i)(j) = WcUtil.letterToBool(letter)
            if (j < x-1) {
                j = j + 1
            }
            else {
            	j = 0
            	i = i + 1
            }
        }
        table
    }
    
	def runAlgorithm(table: Array[Array[Boolean]], matrixIn: Array[Array[Boolean]]) = {
	    var matrix = matrixIn.clone()
		for (y <- 0 to matrix.size-2) {
			for (x <- 0 to matrix(0).size-1) {
				if (matrix(y)(x) == true) {

					for (xx <- 0 to table(0).size-1) {
						if (table(0)(xx) == true) {
						    matrix = writeLine(y+1, x-xx, table(1), matrix)
						}
					}
				
				}
 			}
		}
		matrix
	}
	
	def writeLine(y: Int, x: Int, tableRow: Array[Boolean],  matrixIn: Array[Array[Boolean]]) = {
		var matrix = matrixIn.clone()
	    for (xx <- 0 to tableRow.size-1) { 
			val xxx=x+xx
		    if ( (xxx >= 0) && (xxx < matrix(0).size) && (tableRow(xx)==true) ) {
		    	//matrix(y)(xxx) = tableRow(xx)
		        if (matrix(y)(xxx))
		            matrix(y)(xxx) = false
		            else
		        matrix(y)(xxx) = true
		    }
		}
		matrix
	}

	
}