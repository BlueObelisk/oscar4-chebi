for file in bjoc/*.html; do
	tidy -config tidy.config -o $file.tidy $file
done
