#!/bin/bash -x
#
# This script saves many files/folders together into a single tar.gz file.
# The tar.gz filename is generated using the orginal file/folder name, and
# the current date-time. The trailing slash of a folder name, i.e. "home/",
# will be trimmed before creating the tar.gz file.
#
# Usage: bash backup.sh FILE...

while [ "${1+defined}" ]; do
	case $1 in
	*/)
		file=${1%/}
		;;
	*)
		file=$1
		;;
	esac
	echo $file
	tar czvf "$file-`date +"%Y%m%d-%H%M%S"`.tar.gz" --exclude-vcs "$file"
	shift
done

