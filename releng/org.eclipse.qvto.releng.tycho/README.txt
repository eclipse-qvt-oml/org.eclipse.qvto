The Tycho build automatically promotes downloads and updates, so no cron job help is necessary.
The Tycho build minimises the need for shell activity by auto-registering in P2 repos including the latest.

The qvto.aggrcon file is in the ssh://'committer-name'@git.eclipse.org:29418/simrel/org.eclipse.simrel.build.git repo.

The updates can be checked by looking for the new entry on https://www.eclipse.org/mmt/downloads/?project=qvto
or installing new software from e.g. http://download.eclipse.org/mmt/qvto/updates/milestones/3.10.2/S201805290850
or installing new software from e.g. http://download.eclipse.org/mmt/qvto/updates/releases/3.10.2

A new milestone build was formerly manually added to the composite repository by:

logon to build.eclipse.org
cd ~/downloads/mmt/qvto/updates/milestones/3.10.2
ant -f /shared/modeling/tools/promotion/manage-composite.xml add -Dchild.repository=S201805290850 [ -Dcomposite.name="QVTo 3.10.2 milestones" ]

The SimRel aggregator is configured by GIT\org.eclipse.simrel.build\mmt-qvto.aggrcon to use an explicit milestone entry

So edit qvto.aggrcon in to update 
location="https://download.eclipse.org/mmt/qvto/updates/milestones/3.10.2/S201805290850"
commit with a comment such as [qvto] 3.10.2M1 for 2019-09 and Push to Gerrit (refs/for/master)
The Push dialog identifies a Gerrit such as https://git.eclipse.org/r/149210
Open the Gerrit, Open the Buld job and its console
When the build succeeds, refresh the Gerrit, Click CodeReview+2, Click Submit.
Refresh 

RC builds are just aliases for regular S builds.
The final R build rebuilds the final RC build and is built as late as possible for contribution to the final SimRel build.
For the R  build update qvto.aggrcon to
location="http://download.eclipse.org/mmt/qvto/updates/releases/3.10.2"

After a few hours the mirrors can be checked by:
https://www.eclipse.org/downloads/download.php?file=/mmt/qvto/updates/releases/3.10.2&format=xml

Disable the Promoter job until GIT has been updated for the next release number.

Downloads are accessible at
cd ~/downloads/mmt/qvto/downloads/drops/3.10.2

Archives are accessible at
cd /home/data/httpd/archive.eclipse.org/mmt/qvto/downloads/drops
cd /home/data/httpd/archive.eclipse.org/mmt/qvto/updates/releases

--------
Jenkins config:

Restrict where this project can run: migration
GIT repo: /gitroot/mmt/org.eclipse.qvto.git

Build periodically: H 3 * * 0
Poll SCM schedule: H */6 * * 1-6

Run XVNC during build
Create a dedicated Xauthority file per build?

Build:

apache-maven-latest
--show-version clean verify -P$BUILD_TYPE -Psign $JAVADOC
releng/org.eclipse.qvto.releng.tycho/pom.xml
BUILD_TYPE=$BUILD_TYPE
BUILD_ALIAS=$BUILD_ALIAS

Publish JUnit test report: tests/*.test*/target/surefire-reports/*.xml,tests/*.test*/target/surefire-reports/*/*.xml

Archive the artefacts: releng/org.eclipse.qvto.releng.build-site/target/*.zip,releng/org.eclipse.qvto.releng.build-site/target/publisher.properties,releng/org.eclipse.qvto.releng.build-site/target/downloads.sh,releng/org.eclipse.qvto.releng.build-site/target/updates.sh

Trigger Promoter when stable using releng/org.eclipse.qvto.releng.build-site/target/publisher.properties

-- Drops maintenance -- https://download.eclipse.org/mmt/qvto/downloads/drops
ssh genie.qvt-oml@projects-storage.eclipse.org ls -la /home/data/httpd/download.eclipse.org/mmt/qvto/downloads/drops/3.10.1
ssh genie.qvt-oml@projects-storage.eclipse.org rm -rf  /home/data/httpd/download.eclipse.org/mmt/qvto/downloads/drops/3.10.1/N201909*

-- Updates maintenance -- https://download.eclipse.org/mmt/qvto/updates/releases
ssh genie.qvt-oml@projects-storage.eclipse.org ls -la /home/data/httpd/download.eclipse.org/mmt/qvto/updates/releases
ssh genie.qvt-oml@projects-storage.eclipse.org pwd ; cd /home/data/httpd/download.eclipse.org/mmt/qvto/updates/releases ; pwd ; ls -la
ssh genie.qvt-oml@projects-storage.eclipse.org cd /home/data/httpd/download.eclipse.org/mmt/qvto/updates/releases ; ant -f /shared/modeling/tools/promotion/manage-composite.xml remove -Dchild.repository=3.4.1

ssh genie.qvt-oml@projects-storage.eclipse.org cd /home/data/httpd/download.eclipse.org/mmt/qvto/updates/milestones ; ant -f /shared/modeling/tools/promotion/manage-composite.xml remove -Dchild.repository=3.4.1
ssh genie.qvt-oml@projects-storage.eclipse.org rm -rf /home/data/httpd/download.eclipse.org/mmt/qvto/updates/milestones/3.4.1

-- Drops archiving -- https://archive.eclipse.org/mmt/qvto/downloads/drops ---- and edit GIT\mmt\downloads\extras-qvto.php
ssh genie.qvt-oml@projects-storage.eclipse.org ls -la /home/data/httpd/archive.eclipse.org/mmt/qvto/downloads/drops
ssh genie.qvt-oml@projects-storage.eclipse.org cd /home/data/httpd/download.eclipse.org/mmt/qvto/downloads/drops ; mv 3.9.0 /home/data/httpd/archive.eclipse.org/mmt/qvto/downloads/drops

-- Updates archiving -- https://archive.eclipse.org/mmt/qvto/updates/releases
ssh genie.qvt-oml@projects-storage.eclipse.org ls -la /home/data/httpd/archive.eclipse.org/mmt/qvto/updates/releases
ssh genie.qvt-oml@projects-storage.eclipse.org rm -rf /home/data/httpd/archive.eclipse.org/mmt/qvto/updates/releases/zz*

ssh genie.qvt-oml@projects-storage.eclipse.org cd /home/data/httpd/download.eclipse.org/mmt/qvto/updates/releases ; ant -f /shared/modeling/tools/promotion/manage-composite.xml remove -Dchild.repository=3.4.0
ssh genie.qvt-oml@projects-storage.eclipse.org cd /home/data/httpd/download.eclipse.org/mmt/qvto/updates/releases ; mv 3.4.0 /home/data/httpd/archive.eclipse.org/mmt/qvto/updates/releases
ssh genie.qvt-oml@projects-storage.eclipse.org cd /home/data/httpd/archive.eclipse.org/mmt/qvto/updates/releases ; ant -f /shared/modeling/tools/promotion/manage-composite.xml add -Dchild.repository=3.4.0
