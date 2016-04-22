这个包内的jar包，在jdk1.6里面都包含了
如果环境是jdk1.6，可以删掉这些包

也可以修改
build.lib.thirdpartyServer.excludes=extra/*.jar
在打war包时，把这些包去除