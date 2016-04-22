cmd /k ant -lib buildscripts/buildlib ^
		-lib buildscripts/pmd/lib ^
		-propertyfile build.properties ^
		-propertyfile version.properties ^
		-f build.xml all