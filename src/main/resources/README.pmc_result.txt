Steps to recreate pmc_results.txt:

* go to http://www.ncbi.nlm.nih.gov/sites/entrez
* search for (JOURNAL:"beilstein journal of organic chemistry" )
* in Display, change "Send To" to "File"
* grep PMCID: pmc_result.txt | cut -d':' -f2 | cut -d'C' -f2 > pmcids.txt
