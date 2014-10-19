import org.apache.commons.cli._

/**
 * Created by minerva on 28.8.2014.
 */
object Cli {
  /*
   * 1. GET COMMAND LINE
   */

  def getCommandLine(args: Array[String]): CommandLine = {
    val options = getOptions
    val parser: CommandLineParser = new GnuParser()
    val cmd: CommandLine = parser.parse(options, args)
    cmd
  }

  private def getOptions: Options = {
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

  private def addOptionWithArg(options: Options, shortName: Char, longName: String, description: String) {
    OptionBuilder.withLongOpt(longName)
    OptionBuilder.hasArg
    OptionBuilder.withDescription(description)
    val option = OptionBuilder.create(shortName)
    options.addOption(option)
  }

  /*
   * 2. SET PARAMETERS 
   */

  def setParameters(cmd: org.apache.commons.cli.CommandLine): (Boolean, Boolean, Int, String, String, Int, Int, Boolean, Int, Int, Int) = {
    val bitmap = cmd.hasOption("b")
    val only_developed = cmd.hasOption("o")
    val rule_number = if (cmd.hasOption("r")) cmd.getOptionValue("r").toInt else WolframCells.RULE_NO
    val input = if (cmd.hasOption("i")) cmd.getOptionValue("i") else WolframCells.INPUT
    val output = if (cmd.hasOption("f")) cmd.getOptionValue("f") else WolframCells.OUTPUT
    val width = if (cmd.hasOption("w")) cmd.getOptionValue("w").toInt else if (bitmap) WolframCells.BITMAP_WIDTH else WolframCells.TEXT_WIDTH
    val height = if (cmd.hasOption("e")) cmd.getOptionValue("e").toInt else if (bitmap) WolframCells.BITMAP_HEIGHT else WolframCells.TEXT_HEIGHT
    val help = cmd.hasOption("h")
    val pixelSize = if (cmd.hasOption("p")) cmd.getOptionValue("p").toInt else WolframCells.PIXEL_SIZE
    val background = if (cmd.hasOption("c")) cmd.getOptionValue("c").toInt else WolframCells.BACKGROUND
    val foreground = if (cmd.hasOption("g")) cmd.getOptionValue("g").toInt else WolframCells.FOREGROUND
    (bitmap, only_developed, rule_number, input, output, width, height, help, pixelSize, background, foreground)
  }

/*
 * 3. PRINT HELP
 */

  def printHelp() {
    val formatter = new HelpFormatter()
    formatter.printHelp("wolfram-cells", Cli.getOptions)
  }
}
