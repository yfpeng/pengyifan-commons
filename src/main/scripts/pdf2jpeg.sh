#!/bin/sh -x
#
# This script converts pdf files to jpeg image files. The jpeg filename is
# created using the pdf filename by replacing the "pdf" to "jpg". The resolution
# is 300x300.
#
# Usage: bash pdf2jpeg.sh FILE...

while [ $# -gt 0 ]; do
  fullfile=$1
  filename=$(basename "$fullfile")
  extension="${filename##*.}"
  filename="${filename%.*}"
  gs -dNOPAUSE \
    -q \
    -r300x300 \
    -sDEVICE=jpeg \
    -dBATCH \
    -sCompression=lzw \
    -sOutputFile="$filename.jpg" \
    "$filename.pdf"
  shift
done
