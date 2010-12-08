
file = "pmcids.txt"

new File(file).eachLine { pmcid ->
  println "$pmcid"
  bjocFile = new File("bjoc/" + pmcid + ".html");
  if (bjocFile.exists()) {
    // skip this file
  } else {
    file = new FileOutputStream(bjocFile)
    def out = new BufferedOutputStream(file)
    address = "http://www.ncbi.nlm.nih.gov/sites/ppmc/articles/PMC$pmcid"
    try {
      out << new URL(address).openStream()
    } catch (Exception exception) {
      println "  failed: " + exception.getMessage()
    }
    out.close()
    sleep(3456)
  }
}