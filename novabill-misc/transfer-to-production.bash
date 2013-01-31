#!/bin/bash


# Before running this script export the location of your data externally.
# So for example:
#
#  joe@ubuntu:~$ export NOVABILL_DATA="/home/joe/some/path/to/target/novabill-server/"
#  joe@ubuntu:~$ ./transfer-to-production.bash
#
# 
local_copy=$NOVABILL_DATA
remote_copy="/home/novadart/DEPLOY/novabill-server"

/usr/bin/rsync -r -a -v -e "ssh -l novadart" --delete $local_copy 46.252.156.221:$remote_copy


