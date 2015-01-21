#!/bin/sh -x
#
# This script converts pdf files to tiff image files. The tiff filename is
# created using the pdf filename by replacing the "pdf" to "tiff". 
#
# By default, the script produces black-and-white images. Use the --color option
# to produce CMYK images.
#
# For the black-and-white output, the resolution is 600x600.
# For the CMYK output, the resolution is 300x300.
#
# Usage: bash pdf2tif.sh [--color] FILE...
#   --color     produce the CMYK output

if [[ "$1" == "--color" ]]; then
  DEVICE=tiff32nc
  RES=300x300
  shift
else
  DEVICE=tiffg4
  RES=600x600
fi

while [ $# -gt 0 ]; do
  fullfile=$1
  filename=$(basename "$fullfile")
  extension="${filename##*.}"
  filename="${filename%.*}"
  gs -dNOPAUSE \
    -q \
    -r$RES \
    -sDEVICE=$DEVICE \
    -dBATCH \
    -sCompression=lzw \
    -sOutputFile="$filename.tiff" \
    "$filename.pdf"
  shift
done
