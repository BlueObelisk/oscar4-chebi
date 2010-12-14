1. Steps to recreate pmc_results.txt:

* go to http://www.ncbi.nlm.nih.gov/sites/entrez
* search for (JOURNAL:"beilstein journal of organic chemistry" )
* in Display, change "Send To" to "File"
* grep PMCID: pmc_result.txt | cut -d':' -f2 | cut -d'C' -f2 > pmcids.txt

2. Download files

* groovy downloadPapers.groovy

3. Tidy them up

* sh tidy.sh
* cd bjoc/
* rename -f 's/\.html.tidy$/\.html/' *.html.tidy
