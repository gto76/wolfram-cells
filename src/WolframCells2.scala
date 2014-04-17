import si.gto76.funphototime.Utility
import si.gto76.funphototime.FunPhotoTime
import java.awt.image.BufferedImage

object WolframCells2 extends App {
    /*
     * Random ones:
     * 1111000 = 1101010 zrcalno -> no 20
     * 0111110 = 1110110 zrcalno -> polovičen z potjo
     */
    override def main(args: Array[String]) {
    	processSome()
    }
    
    def processSome() {
    	val HEIGHT: Int = 5000
	    val WIDTH: Int = 10000
	    
        FunPhotoTime.createAndShowGUI();

	    var i = 1
	    var allCombos = Array(/*"1111000",*/ "1110110")
	    for (combo <- allCombos) {
	    	var matrix = WcUtil.getNewMatrix(HEIGHT, WIDTH)
	        val ruleArray = WcUtil.stringToBoolArray("0"+combo)
		    val rule = new Rule(ruleArray)
	        processMatrix(matrix: Array[Array[Boolean]], rule: Rule)
	        val img = WcUtil.getImage(matrix)
	        FunPhotoTime.frame.createFrame(img, combo)
	    }
    }
    
    def processAll() {
    	val HEIGHT: Int = 50
	    val WIDTH: Int = 120

	    var i = 1
	    var allCombos = WcUtil.getAllCombos(7)
	    for (combo <- allCombos) {
	    	var matrix = WcUtil.getNewMatrix(HEIGHT, WIDTH)
	        val ruleArray = WcUtil.stringToBoolArray("0"+combo)
		    val rule = new Rule(ruleArray)
	        processMatrix(matrix: Array[Array[Boolean]], rule: Rule)
	        val trues = countTrues(matrix)
		    if (trues > HEIGHT*2) {
		    	println(i+":"+combo)
		    	i = i + 1
		        WcUtil.printMatrix(matrix)
		    } 
	    }
    }
    
    def processMatrix(matrix: Array[Array[Boolean]], rule: Rule) {
        for (y <- 0 to matrix.size-2) {
			for (x <- 0 to matrix(0).size-3) {
				val slice = matrix(y).slice(x, x+3)
			    val newCell = rule.getCell(slice)
			    matrix(y+1)(x+1) = newCell
 			}
		}
    }
 
    class Rule(rules: Array[Boolean]) {
        def getCell(neighbours: Array[Boolean]): Boolean = {
            val index = toInt(neighbours(0), neighbours(1), neighbours(2))
            rules(index)
        }   
    }
    
    def toInt(b1: Boolean, b2: Boolean, b3: Boolean): Int = { toInt(b1)*4 + toInt(b2)*2 + toInt(b3) }
    def toInt(b: Boolean): Int = { if (b) 1 else 0 }
    
    def countTrues(matrix: Array[Array[Boolean]]): Int = {
        var trues = 0
        for (row <- matrix) {
            for (cell <- row) {
                if (cell) trues = trues + 1
            }
        }
        trues
    }
    
}