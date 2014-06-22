//import si.gto76.funphototime.Utility

import java.awt.image.BufferedImage
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.ImageIcon

object WcUtil {
	
	def getNewMatrix(matrixHeight: Int, matrixWidth: Int): Array[Array[Boolean]] = {
	    var matrix = Array.ofDim[Boolean](matrixHeight, matrixWidth) 
	    matrix(0)(matrixWidth/2) = true
	    matrix
    }
	
	def getNewImage(x: Int, y: Int): BufferedImage = {
	    val imgOut = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB)
		imgOut;
	}
	
	def printMatrix(matrix: Array[Array[Boolean]]) {
	    print(matrixToString(matrix, "#", " "))
	}
		
	def stdinToString = {
		val sb = new StringBuilder
		val stdin = new BufferedReader(new InputStreamReader(System.in));
		var line: String = ""
		while ((line = stdin.readLine()) != null && line.length() != 0) {
			sb.append(line)
		}
		sb.toString
	}
	
	def magnifyImage(imgIn: BufferedImage, factor: Int): BufferedImage = {
		if (factor == 1) return imgIn
		var imgOut = getNewImage(imgIn.getWidth * factor, imgIn.getHeight * factor)
		for (i <- 0 to imgOut.getHeight()-1) {
	        for (j <- 0 to imgOut.getWidth()-1) {
	        	val rgb = imgIn.getRGB(j/factor, i/factor)
	        	imgOut.setRGB(j, i, rgb)
	        }
	    }
	    imgOut
	}
	
	/*
	 * CONVERSIONS:
	 */
    
    def matrixToImage(matrix: Array[Array[Boolean]], background: Int, foreground: Int) = {
        var img = getNewImage(matrix(0).size, matrix.size)
        var x, y = 0;
        for (row <- matrix) {
	        for (element <- row) {
	        	if (element == true)
	        		img.setRGB(x, y, foreground) 
	        	else
	        		img.setRGB(x, y, background)
	        	x = x + 1
	        }
	        x = 0
	        y = y + 1
	    }
		img
    }
    
    def imageToMatrix(image: BufferedImage) = {
    	val height = image.getHeight()
    	val width = image.getWidth()
    	val matrix = Array.ofDim[Boolean](height, width)
    	///
    	for (i <- 0 to height-1) {
	        for (j <- 0 to width-1) {
	        	if (image.getRGB(j, i) != 0) {
	        		matrix(i)(j) = true // TODO test!!
	        	}
	        }
	    }
	    matrix
    }

    def matrixToString(matrix: Array[Array[Boolean]], t: String, f: String) = {
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
	
	def stringToMatrix(matrixStr: String) = {
	    val lines = matrixStr.split("\n")
	    val sortedLines = lines.sortWith((A, B) => A.size > B.size)
	    val width = sortedLines(0).length
	    val height = lines.length
	    val matrix = Array.ofDim[Boolean](height, width)
	    ///
	    for (i <- 0 to height-1) {
	        val line = lines(i)
	        for (j <- 0 to line.length-1) {
	        	if (line.charAt(j) != ' ') {
	        		matrix(i)(j) = true
	        	}
	        }
	    }
	    matrix
	}
	
	/*
	 * RULES UTILS
	 */
	
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
        letter == '1'
    }
		
	def getBinary(ruleNo: Int, length: Int): String = {
		val binString = ruleNo.toBinaryString
		println(binString)
		if (binString.length() > length) {
			binString.substring(length)
		} else {
			val delta = length - binString.length()
			val sb = new StringBuilder
			for (i <- 1 to delta) {
				sb.append("0")
			}
			println(delta + " " + sb.toString + binString)
			sb.toString + binString
		}
	}
	
	//////
		
	def countTrues(matrix: Array[Array[Boolean]]): Int = {
		var trues = 0
		for (row <- matrix) {
			for (cell <- row) {
				if (cell) trues = trues + 1
			}
		}
		trues
	}
	
	def showImage(image: BufferedImage) {
		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(image)));
	}
	
}