#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Wrapper Unix script
# ----------------------------------------------------------------------------
die() {
    echo "$*" >&2
    exit 1
}

# Resolve directory of this script
PRG="$0"
while [ -h "$PRG" ]; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`/"$link"
    fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`" >/dev/null
APPDIR="`pwd`"
cd "$SAVED" >/dev/null

JAVACMD="java"
if [ -n "$JAVA_HOME" ]; then
    JAVACMD="$JAVA_HOME/bin/java"
fi

WRAPPER_JAR="$APPDIR/.mvn/wrapper/maven-wrapper.jar"
if [ ! -e "$WRAPPER_JAR" ]; then
    echo "Downloading maven-wrapper.jar..."
    curl -o "$WRAPPER_JAR" "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar" 2>/dev/null || \
    wget -O "$WRAPPER_JAR" "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar" 2>/dev/null
fi

exec "$JAVACMD" -Dmaven.multiModuleProjectDirectory="$APPDIR" -jar "$WRAPPER_JAR" "$@"
