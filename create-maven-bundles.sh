mkdir bundles-tmp

# main pom
mkdir bundles-tmp/jfacets-all
cp ./jfacets-all/pom.xml ./bundles-tmp/jfacets-all
cd bundles-tmp/jfacets-all
jar cvf jfacets-all-3.0-bundle.jar *
mv jfacets-all-3.0-bundle.jar ..
cd ..
rm -rf jfacets-all
cd ..

cd jfacets-all/jfacets-core
mvn source:jar javadoc:jar repository:bundle-create
cp target/*-3.0-bundle.jar ../../bundles-tmp
cd ../..

cd jfacets-all/jfacets-groovy
mvn source:jar javadoc:jar repository:bundle-create
cp target/*-3.0-bundle.jar ../../bundles-tmp
cd ../..

cd jfacets-all/jfacets-acegi
mvn source:jar javadoc:jar repository:bundle-create
cp target/*-3.0-bundle.jar ../../bundles-tmp
cd ../..

cd jfacets-all/jfacets-web
mvn source:jar javadoc:jar repository:bundle-create
cp target/*-3.0-bundle.jar ../../bundles-tmp
cd ../..


