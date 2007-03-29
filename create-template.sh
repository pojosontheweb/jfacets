WOKO_VERSION=0.6

CURRENT_DIR=`pwd`

echo "Full path " $CURRENT_DIR

TEMPLATE_DIR=$CURRENT_DIR/woko-template-$WOKO_VERSION

TO_REMOVE=$CURRENT_DIR/woko-template-*
echo "Clean previous template if any (remove" $TO_REMOVE ")"

rm -rf $TO_REMOVE

echo "Creating template in dir " $TEMPLATE_DIR

mkdir $TEMPLATE_DIR

echo "Copying config files into src..."

mkdir $TEMPLATE_DIR/src
cp $CURRENT_DIR/java/src/*.properties $TEMPLATE_DIR/src
cp $CURRENT_DIR/java/src/*.xml $TEMPLATE_DIR/src
cp -r $CURRENT_DIR/java/demo-src/* $TEMPLATE_DIR/src
find woko-template-$WOKO_VERSION -type d -name CVS -exec rm -rf {} \;

echo "Copying exploded directory into WebContent..."

mkdir $TEMPLATE_DIR/WebContent
cp -r $CURRENT_DIR/exploded/* $TEMPLATE_DIR/WebContent

echo "Creating woko jar..."

CLASSES_DIR=$TEMPLATE_DIR/WebContent/WEB-INF/classes
JAR_NAME=woko-$WOKO_VERSION.jar

cd $CLASSES_DIR
rm -f *.properties
rm -f *.xml
rm -rf woko
jar cvf ../lib/$JAR_NAME *

echo "Removing" $CLASSES_DIR

cd $CURRENT_DIR
rm -rf $CLASSES_DIR
find . -type f -name .DS_Store -exec rm -f {} \;

echo "Zipping woko template..."

TEMPLATE_ZIP=woko-template-$WOKO_VERSION.zip
zip -r $TEMPLATE_ZIP woko-template-$WOKO_VERSION

echo "Cleaning temp folder..."

rm -rf $TEMPLATE_DIR

echo "*** Woko template generated ok in " $CURRENT_DIR/$TEMPLATE_ZIP
