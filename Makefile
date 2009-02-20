# Makefile for Jake
#
# 	provides a dependency system to avoid duplicate builds, tests 
# 	and packaging.
# 
#

VERSION="1.0-SNAPSHOT"
SHELL="bash"
MVN=mvn ${MVNEXTRAARGS}
GUITYPE="swing"
DEBUGGUITYPE="console"

all: install

help:
	@cat Makefile|grep '^# ' |sed 's/^# \([^: \t]*\): \(.*\)/\t\1\n\t\t\t\2/g'|sed 's/#\(.*\)#/\n[1;34m\1[0m\n/g'|sed 's/#//g'|sed 's/\t@\([^: ]*\)/     \*\t[1;32m\1[0m/g'
	@svn info |grep '^Revision: '|sed 's/Revision: / Repository revision: /g'

# starting #

# @start: start gui with simple install (no dependency system)
start: install
	cd gui; ${MVN} exec:java -Dcom.jakeapp.gui.test.usemock=no

# @depstart: start gui with dependency system
depstart: gui 
	cd gui; ${MVN} exec:java -Dcom.jakeapp.gui.test.usemock=no
# mockstart: start gui (with mock backend) with simple install
# 				(no dependency system)
mockstart: install
	cd gui; ${MVN} exec:java -Dcom.jakeapp.gui.test.usemock=yes

# instantquit: start gui and quit it immediatly (for debugging spring)
instantquit: gui
	cd gui; ${MVN} exec:java -Dcom.jakeapp.gui.test.instantquit

# quickstart: start gui with the projectfolder $PROJECTFOLDER
quickstart: gui
	cd gui; ${MVN} exec:java -Dexec.args=${PROJECTFOLDER}

# console: start console-gui (commander)
console: commander 
	cd commander; ${MVN} exec:java

# building & installing #

# @install: simply install everything (no dependency system)
install: 
	mvn -Dmaven.test.skip=true install


# gui: build gui component
gui: core
	@bash .build.sh ${VERSION} $@ "$^"

# commander: build commander component
commander: core
	@bash .build.sh ${VERSION} $@ "$^"

# core: build core component
core: fss ics ics-xmpp
	@bash .build.sh ${VERSION} $@ "$^"

# fss: build fss component
fss:
	@bash .build.sh ${VERSION} $@ "$^"

# ics: build ics component
ics:
	@bash .build.sh ${VERSION} $@ "$^"

# ics-xmpp: build ics-xmpp component
ics-xmpp: ics
	bash .build.sh ${VERSION} $@ "$^"

# cleaning #

# clean: clean build environment
clean:
	${MVN} clean

# mrproper: clean build environment and uninstall from local 
# 				repository
mrproper: clean
	rm -rf ~/.m2/repository/com/{jakeapp,doublesignal}/
	rm -f target/*-${VERSION}.jar

# lazyclean: clean build environment, but keep fss
lazyclean:
	cp fss/target/fss-${VERSION}.jar .backup.fss-${VERSION}.jar
	${MVN} clean
	mkdir fss/target/
	mv .backup.fss-${VERSION}.jar fss/target/fss-${VERSION}.jar

# others # 

# up: update to newest revision and scroll logs
up:
	@oldrev=$$(svn info |grep '^Revision: '|sed 's/Revision: //g'); svn up; newrev=$$(svn info |grep '^Revision: '|sed 's/Revision: //g'); [ "$$oldrev" == "$$newrev" ] || svn log -v -r$$oldrev:$$newrev|while read line; do echo "$$line"; sleep 0.3; echo "$$line"|grep -q -- "-----" && sleep 3; done

# 
# 
# You can attach mvn arguments by setting the MVNEXTRAARGS environment variable.
# 
# The dependency system does only work with the coreutils package, i.e., only on 
# Linux. 
# 

.PHONY: install gui core fss ics ics-xmpp commander start depstart mockstart instantquit quickstart console clean mrproper lazyclean up
