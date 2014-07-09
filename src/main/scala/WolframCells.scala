import java.awt.image.BufferedImage
import org.apache.commons.cli.Options
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.GnuParser
import org.apache.commons.cli.OptionBuilder
import java.nio.ByteBuffer
import java.io.File
import javax.imageio.ImageIO
import java.io.ByteArrayOutputStream
import java.util.Arrays
import java.io.ByteArrayInputStream
import java.nio.charset.Charset
import java.io.PrintStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import org.apache.commons.cli.HelpFormatter
import com.sun.org.apache.xalan.internal.xsltc.compiler.Output

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
		val cmd = getCommandLine(args)
		val (bitmap, only_developed, rule_number, input, output, width, height, help, pixelSize, background, foreground) = setParameters(cmd)
		if (help) { printHelp; return }
		val matrixIn = obtainMatrix(input, width, height)
		val matrixOut = transformMatrix(matrixIn, rule_number)
		val matrixIsNotDeveloped = !isDeveloped(matrixOut)
		if (only_developed && matrixIsNotDeveloped) return
		dispatchMatrix(matrixOut, bitmap, output, pixelSize, background, foreground)
	}

	
	/*
	 * 1. GET COMMAND LINE
	 */
	
	private def getCommandLine(args: Array[String]): CommandLine = {
		val options = getOptions
		val parser: CommandLineParser = new GnuParser();
		val cmd: CommandLine = parser.parse(options, args);
		cmd
	}
	
	def getOptions: Options = {
		val options = new Options()
		options.addOption("b", "bitmap", false, "Outupt image instead of text.")
		options.addOption("h", "help", false, "Print this message.")
		options.addOption("o", "only-developed", false, "Output matrix only if there is more than one cell per row.")
		addOptionWithArg(options, 'r', "rule-number", "Rule to be used.")
		addOptionWithArg(options, 'w', "width", "Matrix width")
		addOptionWithArg(options, 'e', "height", "Matrix height.")
		addOptionWithArg(options, 'i', "input-matrix", "Specify filename containing matrix on which the " +
			" transformations will be executed. Should be in text format or in png. '-' for stdin")
		addOptionWithArg(options, 'f', "file", "Output filename.")
		addOptionWithArg(options, 'p', "pixel-size", "Cell size in pixels.")
		addOptionWithArg(options, 'c', "background-color", "Background color of output bitmap as single number. Default is black - 0. Warning: for input image 0 is assumed.")
		addOptionWithArg(options, 'g', "foreground-color", "Foreground color of output bitmap as single number. Default is blue - 255.")
		options
	}

	def addOptionWithArg(options: Options, shortName: Char, longName: String, description: String) {
		OptionBuilder.withLongOpt(longName)
		OptionBuilder.hasArg
		OptionBuilder.withDescription(description)
		val option = OptionBuilder.create(shortName)
		options.addOption(option);
	}
	
	/*
	 * 2. SET PARAMETERS
	 */
	
	private def setParameters(cmd: org.apache.commons.cli.CommandLine): (Boolean, Boolean, Int, String, String, Int, Int, Boolean, Int, Int, Int) = {
		val bitmap = cmd.hasOption("b")
		val only_developed = cmd.hasOption("o")
		val rule_number = if (cmd.hasOption("r")) cmd.getOptionValue("r").toInt else RULE_NO
		val input = if (cmd.hasOption("i")) cmd.getOptionValue("i") else INPUT
		val output = if (cmd.hasOption("f")) cmd.getOptionValue("f") else OUTPUT
		val width = if (cmd.hasOption("w")) cmd.getOptionValue("w").toInt else if (bitmap) BITMAP_WIDTH else TEXT_WIDTH
		val height = if (cmd.hasOption("e")) cmd.getOptionValue("e").toInt else if (bitmap) BITMAP_HEIGHT else TEXT_HEIGHT
		val help = cmd.hasOption("h")
		val pixelSize = if (cmd.hasOption("p")) cmd.getOptionValue("p").toInt else PIXEL_SIZE
		val background = if (cmd.hasOption("c")) cmd.getOptionValue("c").toInt else BACKGROUND
		val foreground = if (cmd.hasOption("g")) cmd.getOptionValue("g").toInt else FOREGROUND
		(bitmap, only_developed, rule_number, input, output, width, height, help, pixelSize, background, foreground)
	}
	
	/*
	 * 3. PRINT HELP
	 */
		
	def printHelp() {
		val formatter = new HelpFormatter()
		formatter.printHelp("wolfram-cells", getOptions)
	}

	/*
	 * 4. OBTAIN MATRIX
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
	 * 5. TRANSFORM MATRIX
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
	 * 6. IS DEVELOPED
	 */
	
	def isDeveloped(matrix: Array[Array[Boolean]]): Boolean = {
		val noOfTrues = WcUtil.countTrues(matrix)
		val matrixHeight = matrix.size
		return noOfTrues > matrixHeight * 2
	}
	
	/*
	 * 7. DISPATCH MATRIX
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
			ImageIO.write(img, "png", System.out);
		} else {
			ImageIO.write(img, "png", new File(output));
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