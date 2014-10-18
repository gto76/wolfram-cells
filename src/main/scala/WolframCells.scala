import java.io.{File, PrintWriter}
import javax.imageio.ImageIO

object WolframCells {

  val TEXT_WIDTH = 120
  val TEXT_HEIGHT = 50
  val BITMAP_WIDTH = 1000
  val BITMAP_HEIGHT = 500
  val INPUT = ""
  val OUTPUT = "-"
  val RULE_NO = 118
  val RULE_LENGTH = 7
  val PIXEL_SIZE = 1
  val BACKGROUND = 0
  val FOREGROUND = 255
  val INPUT_BACKGROUND = 0

  /*
 * Random rules:
 * 1111000 = 1101010 mirrored -> no 20
 * 0111110 = 1110110 mirrored -> halved with a path
 */

	def main(args: Array[String]) {
		val cmd = Cli.getCommandLine(args)
		val (bitmap, only_developed, rule_number, input, output, width, height, help, pixelSize, background, foreground) = Cli.setParameters(cmd)
		if (help) { Cli.printHelp; return }
		val matrixIn = obtainMatrix(input, width, height)
		val matrixOut = transformMatrix(matrixIn, rule_number)
		val matrixIsNotDeveloped = !isDeveloped(matrixOut)
		if (only_developed && matrixIsNotDeveloped) return
		dispatchMatrix(matrixOut, bitmap, output, pixelSize, background, foreground)
	}

	/*
	 * 1. OBTAIN MATRIX
	 */

	private def obtainMatrix(input: String, width: Int, height: Int): Array[Array[Boolean]] = {
		val inputTroughStdin = (input == "-")
		val inputTroughFile = (input != "")
		if (inputTroughStdin) {
			val stdin = WcUtil.stdinToString
			WcUtil.stringToMatrix(stdin)
		} else if (inputTroughFile) {
			getMatrixFromFile(input)
		} else {
			// Empty matrix with seed at top center
			WcUtil.getNewMatrix(height, width)
		}
	}

	private def getMatrixFromFile(input: String): Array[Array[Boolean]] = {
		val fileIsImage = input.toLowerCase.endsWith(".png")
		if (fileIsImage) {
			val image = ImageIO.read(new File(input))
			WcUtil.imageToMatrix(image)
		} else {
			val source = scala.io.Source.fromFile(input)
			val matrixStr = source.mkString
			source.close()
			WcUtil.stringToMatrix(matrixStr)
		}
	}
	
	/*
	 * 2. TRANSFORM MATRIX
	 */

	def transformMatrix(matrix: Array[Array[Boolean]], ruleNo: Int): Array[Array[Boolean]] = {
		val ruleBinary = WcUtil.getBinary(ruleNo, RULE_LENGTH)
		val ruleArray = WcUtil.stringToBoolArray("0" + ruleBinary)
		val rule = new Rule(ruleArray)
		println(ruleNo + " " + ruleBinary + " " + ruleArray + " " + rule)
		processMatrix(matrix, rule)
		matrix
	}

	class Rule(rules: Array[Boolean]) {
		def getCell(neighbours: Array[Boolean]): Boolean = {
			val index = toInt(neighbours(0), neighbours(1), neighbours(2))
			rules(index)
		}
		def toInt(b1: Boolean, b2: Boolean, b3: Boolean): Int = toInt(b1) * 4 + toInt(b2) * 2 + toInt(b3)
		def toInt(b: Boolean): Int = if (b) 1 else 0
	}
	
	def processMatrix(matrix: Array[Array[Boolean]], rule: Rule) {
		for (y <- 0 to matrix.size - 2) {
			for (x <- 0 to matrix(0).size - 3) {
				val slice = matrix(y).slice(x, x + 3)
				val newCell = rule.getCell(slice)
				matrix(y + 1)(x + 1) = newCell
			}
		}
	}

	/*
	 * 3. IS DEVELOPED
	 */
	
	def isDeveloped(matrix: Array[Array[Boolean]]): Boolean = {
		val noOfTrues = WcUtil.countTrues(matrix)
		val matrixHeight = matrix.size
		noOfTrues > matrixHeight * 2
	}
	
	/*
	 * 4. DISPATCH MATRIX
	 */

	def dispatchMatrix(matrix: Array[Array[Boolean]], bitmap: Boolean, output: String, pixelSize: Int, background: Int, foreground: Int) {
		if (bitmap) {
			dispatchBitmap(matrix, output, pixelSize, background, foreground)
		} else {
			dispatchText(matrix, output)
		}
	}

	private def dispatchBitmap(matrix: Array[Array[Boolean]], output: String, pixelSize: Int, background: Int, foreground: Int): Unit = {
		val imgIn = WcUtil.matrixToImage(matrix, background, foreground)
		val img = WcUtil.magnifyImage(imgIn, pixelSize)
		if (output == "-") {
			ImageIO.write(img, "png", System.out)
		} else {
			ImageIO.write(img, "png", new File(output))
		}
	}
	
	private def dispatchText(matrix: Array[Array[Boolean]], output: String): Unit = {
		if (output == "-") {
			WcUtil.printMatrix(matrix)
		} else {
			val matrixStr = WcUtil.matrixToString(matrix, "#", " ")
			val out = new PrintWriter(output)
			out.println(matrixStr)
			out.close
		}
	}


}