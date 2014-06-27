#! /bin/bash -x

# Might fail if $0 is a link
TAGGER_HOME=`dirname "$0"`
OUTPUT_DIR="$TAGGER_HOME/classes"
CLASSPATH="$TAGGER_HOME/lib/*:$TAGGER_HOME/classes"
SRC_DIR="$TAGGER_HOME/src"

# get javac
if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    JAVACCMD="$JAVA_HOME/bin/javac"
  else
    JAVACCMD="`which javac`"
  fi
fi

# create bin folder
if [ ! -d $OUTPUT_DIR ]; then
  mkdir $OUTPUT_DIR
fi

# clean bin folder
rm -f `find $OUTPUT_DIR -name '*.class'`

# compile
find $SRC_DIR -name '*.java' > sources_list.txt
$JAVACCMD -g -d $OUTPUT_DIR -classpath $CLASSPATH @sources_list.txt
rm -f sources_list.txt
